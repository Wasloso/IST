import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnookerTableAnimation {
    public static void main(String[] args) {
        SnookerTableWindow frame = new SnookerTableWindow();
        SnookerTablePanel panel = new SnookerTablePanel();
        frame.add(panel);
        frame.setVisible(true);
        javax.swing.SwingUtilities.invokeLater(() -> panel.generateBalls(100));
        panel.runAnimation();
    }
}

class Ball extends JComponent {
    int radius = 14;
    double x, y;
    double velocityX;
    double velocityY;
    Color color;

    public Ball(int x, int y, double initVelocityX, double initVelocityY, Color color) {
        this.x = x;
        this.y = y;
        this.velocityX = initVelocityX;
        this.velocityY = initVelocityY;
        this.color = color;

    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) x, (int) y, radius * 2, radius * 2);
    }

    public void updatePosition(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

    }

    public boolean checkCollision(Ball other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < this.radius + other.radius;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, radius * 2, radius * 2);
    }

    public void loseVelocity(double deltaTime) {
        velocityX *= 0.995;
        velocityY *= 0.995;
    }
}

class SnookerTablePanel extends JPanel {
    int FPS = 60;
    long lastTime = System.nanoTime();
    ArrayList<Ball> balls = new ArrayList<>();
    Color bg = new Color(10, 108, 3);

    public void generateBalls(int count) {
        Random random = new Random();
        int width = Math.max(getWidth(), 800);
        int height = Math.max(getHeight(), 600);
        ArrayList<Color> colors = new ArrayList<>(
                Arrays.asList(Color.RED, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.BLACK,
                        Color.WHITE));

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            double speed = 75 + random.nextDouble() * 75;

            double angle = Math.toRadians(random.nextDouble() * 360);

            double velocityX = speed * Math.cos(angle);
            double velocityY = speed * Math.sin(angle);
            Color color = colors.get(random.nextInt(colors.size() - 1));

            balls.add(new Ball(x, y, velocityX, velocityY, color));
        }
    }

    public void runAnimation() {
        Timer timer = new Timer(1000 / FPS, e -> {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1e9; // convert to seconds
            lastTime = currentTime;

            for (Ball ball : balls) {
                handleWallColission(ball);
                for (Ball ball2 : balls) {
                    if (ball == ball2)
                        continue;
                    if (checkCollision(ball, ball2)) {
                        bounce(ball, ball2);
                    }
                }
                ball.updatePosition(deltaTime);
                ball.loseVelocity(deltaTime);
            }
            repaint();
        });
        timer.start();
    }

    private void handleWallColission(Ball ball) {
        if (ball.y <= 0) {
            ball.y = 0;
            ball.velocityY *= -1;
        } else if (ball.y + 2 * ball.radius >= getHeight()) {
            ball.y = getHeight() - 2 * ball.radius;
            ball.velocityY *= -1;
        }

        if (ball.x <= 0) {
            ball.x = 0;
            ball.velocityX *= -1;
        } else if (ball.x + 2 * ball.radius >= getWidth()) {
            ball.x = getWidth() - 2 * ball.radius;
            ball.velocityX *= -1;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(bg);
        balls.forEach(b -> b.draw(g2d));
    }

    public boolean checkCollision(Ball b1, Ball b2) {
        double dx = b1.x - b2.x;
        double dy = b1.y - b2.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < b1.radius + b2.radius;
    }

    public void bounce(Ball b1, Ball b2) {
        double dx = b2.x - b1.x;
        double dy = b2.y - b1.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0)
            return;

        double nx = dx / distance;
        double ny = dy / distance;

        double vx = b2.velocityX - b1.velocityX;
        double vy = b2.velocityY - b1.velocityY;

        double dotProduct = vx * nx + vy * ny;

        if (dotProduct > 0)
            return;

        double impulse = 2 * dotProduct / 2;
        b1.velocityX += impulse * nx;
        b1.velocityY += impulse * ny;
        b2.velocityX -= impulse * nx;
        b2.velocityY -= impulse * ny;
    }
}

class SnookerTableWindow extends JFrame {
    public SnookerTableWindow() {
        setTitle("Snooker Table");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
