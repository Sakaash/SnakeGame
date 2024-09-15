import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int DELAY = 85;
    final int[] x = new int[WIDTH];
    final int[] y = new int[HEIGHT];
    int bodyParts = 3;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    private Timer timer;
    private final Random random;
    private boolean moved;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
        moved = true;
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY+15, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running){
//            Optional this is used to draw a grid to visualize the space
            for (int i = 0; i < WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for(int i = 0; i < bodyParts; i++) {
                if(i==0){
                    g.setColor(Color.GREEN);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(40,210,15));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 40));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString(""+applesEaten,(WIDTH-fm.stringWidth(""+applesEaten)),g.getFont().getSize());
        }
        else
            gameOver(g);

    }

    public void newApple() {
        appleX = random.nextInt( (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt( (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
        moved = true;
    }
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        for(int i = bodyParts; i > 0; i--) {
            if(x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        if(x[0]<0){
            running = false;
        }
        else if(x[0]>=WIDTH){
            running = false;
        }
        else if(y[0]<0){
            running = false;
        }
        else if(y[0]>=HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Monospaced", Font.BOLD, 60));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - fm.stringWidth("Game Over")) / 2, (int) (HEIGHT / 2.2));
        g.setFont(new Font("Monospaced", Font.BOLD, 30));
        FontMetrics fm2 = getFontMetrics(g.getFont());
        g.drawString("Your Score: " + applesEaten, (WIDTH - fm2.stringWidth("Your Score:  ")) / 2
                , (int) (HEIGHT / 1.9));

        FontMetrics fm3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to Retry.", WIDTH - fm3.stringWidth("Press SPACE to Retry.")/2, (int)(HEIGHT/1.5));
        g.setFont(new Font("Monospaced", Font.BOLD, 30));
        g.setColor(Color.WHITE);
    }
    public void resetGame() {
        bodyParts = 3;
        applesEaten = 0;
        direction = 'D';
        moved = true;
        running = true;

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        newApple();

        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e){
            if(moved) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_UP -> {
                        if (direction != 'D') {
                            direction = 'U';
                            moved = false;
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != 'U') {
                            direction = 'D';
                            moved = false;
                        }
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (direction != 'R') {
                            direction = 'L';
                            moved = false;
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != 'L') {
                            direction = 'R';
                            moved = false;
                        }
                    }

                }
            }
            if(!running && e.getKeyCode() == KeyEvent.VK_SPACE){
                resetGame();
            }

            }
        }
    }


