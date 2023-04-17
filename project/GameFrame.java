import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GameFrame extends JFrame implements ActionListener{
    private JMenuBar menuBar;

    private JMenu controlMenu;
    private JMenu editMenu;

    private JMenuItem startGame;
    private JMenuItem pauseGame;
    private JMenuItem stopGame;
    private JCheckBoxMenuItem areWallsEnabledMenuItem;

    private GamePanel activePanel;

    public GameFrame() {
        activePanel = new GamePanel(true);
        this.add(activePanel);

        this.setTitle("SnakeGame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        menuBar = new JMenuBar();

        controlMenu = new JMenu("Controlls");
        editMenu = new JMenu("Edit");

        startGame = new JMenuItem("Start new game");
        pauseGame = new JMenuItem("(Un)Pause current game");
        stopGame = new JMenuItem("Stop current game");

        areWallsEnabledMenuItem = new JCheckBoxMenuItem("Enable boundaries");
        areWallsEnabledMenuItem.setSelected(true);

        startGame.addActionListener(this);
        pauseGame.addActionListener(this);
        stopGame.addActionListener(this);

        areWallsEnabledMenuItem.addActionListener(this);

        controlMenu.add(startGame);
        controlMenu.add(pauseGame);
        controlMenu.add(stopGame);

        editMenu.add(areWallsEnabledMenuItem);

        menuBar.add(controlMenu);
        menuBar.add(editMenu);

        this.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGame) {
            this.getContentPane().removeAll();
            this.getContentPane().validate();
            activePanel.getTimer().stop();
            activePanel = new GamePanel(areWallsEnabledMenuItem.isSelected());
            this.add(activePanel);
            this.getContentPane().revalidate();
            this.activePanel.requestFocus1();
            this.pack();
            this.setVisible(true);
        } else if (e.getSource() == pauseGame) {
            if (activePanel.getTimer().isRunning()) {
                activePanel.getTimer().stop();
            } else {
                activePanel.getTimer().start();
            }
        } else if (e.getSource() == stopGame) {
            activePanel.setIsRunning(false);
            activePanel.getTimer().stop();
        } else if (e.getSource() == areWallsEnabledMenuItem) {
            activePanel.setAreWallsEanabled(((AbstractButton)(e.getSource())).isSelected());
        }
    }
}
