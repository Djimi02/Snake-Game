import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean isRunning = true;
    javax.swing.Timer timer;
    Random random;
    private boolean areWallsEanabled;

    public GamePanel(boolean areWallsEanabled) {
        random = new Random();
        this.areWallsEanabled = areWallsEanabled;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        this.addKeyListener(this);
        startGame();
    }

    public void requestFocus1() {
        requestFocus();
    }

    private void startGame() {
        createNewApple();
        isRunning = true;
        timer = new javax.swing.Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {

        if (!isRunning) {
            gameOver(g);
            return;
        }

        // Draw lines of the grid
        for (int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
        }

        // Draw apple
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        // Draw snake
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) { // Draw the head
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(new Color(45, 180, 0));
            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }

        // Draw the score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Apples: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Apples: " + applesEaten)) / 2, g.getFont().getSize());
    }

    private void createNewApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break; 
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;  
        }
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            createNewApple();
        }
    }

    private void checkCollisions() {
        // Checks if snake bite itself
        for (int i = 1; i < bodyParts; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                isRunning = false;
            }
        }

        // Checks if snake hit left wall
        if (x[0] < 0 && areWallsEanabled) {
            isRunning = false;
        } else if (x[0] < 0 && !areWallsEanabled) {
            x[0] = SCREEN_WIDTH - UNIT_SIZE;
        }

        // Checks if snake hit right wall
        if (x[0] >= SCREEN_WIDTH && areWallsEanabled) {
            isRunning = false;
        } else if (x[0] >= SCREEN_WIDTH && !areWallsEanabled) {
            x[0] = 0;
        }

        // Checks if snake hit top wall
        if (y[0] < 0 && areWallsEanabled) {
            isRunning = false;
        } else if (y[0] < 0 && !areWallsEanabled) {
            y[0] = SCREEN_HEIGHT - UNIT_SIZE;
        }

        // Checks if snake hit left wall
        if (y[0] >= SCREEN_HEIGHT && areWallsEanabled) {
            isRunning = false;
        } else if (y[0] >= SCREEN_HEIGHT && !areWallsEanabled) {
            y[0] = 0;
        }

        if (!isRunning) {
            timer.stop();
        }
    }

    private void gameOver(Graphics g) {
        // Draw the score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Apples: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Apples: " + applesEaten)) / 2, g.getFont().getSize());
        
        // Drawing game over label
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public javax.swing.Timer getTimer() {
        return this.timer;
    }

    public void setIsRunning(boolean bol) {
        this.isRunning = bol;
    }

    public void setAreWallsEanabled(boolean bol) {
        this.areWallsEanabled = bol;
    }
}
