import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.Iterator;
import java.util.Random;

class RaindropsPanel extends JPanel {
    int intensity;
    ArrayList<Raindrop> raindrops = new ArrayList<>();
    Random random = new Random();
    long lastTime = System.nanoTime();
    int FPS = 120;

    RaindropsPanel(int intensity) {
        this.intensity = intensity;
        Timer timer = new Timer(1000 / FPS, e -> updateRaindrops());
        timer.start();
    }

    private void updateRaindrops() {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastTime) / 1_000_000_000.0; // Convert to seconds
        lastTime = currentTime;
        for (int i = 0; i < intensity; i++) {
            raindrops.add(new Raindrop(random.nextInt(getWidth()), -random.nextInt(50)));
        }

        Iterator<Raindrop> iterator = raindrops.iterator();
        while (iterator.hasNext()) {
            Raindrop r = iterator.next();
            r.updatePosition(deltaTime);
            if (r.y > getHeight()) {
                iterator.remove();
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.WHITE);
        raindrops.forEach(r -> r.draw(g2d));
    }
}

class Raindrop extends JComponent {
    Color color = Color.BLUE;
    double velocity = 100;
    double x, y;
    int size = 4;

    public Raindrop(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updatePosition(double deltaTime) {
        velocity += velocity * (1.05 * deltaTime);
        y += (velocity * deltaTime);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) x, (int) y, size, size * 2);
    }
}

class RaindropsFrame extends JFrame {
    public RaindropsFrame() {
        setTitle("Raindrops");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

public class RaindropsAnimation {
    public static void main(String[] args) {
        // default
        int intensity = 10;
        if (args.length > 0) {
            intensity = Integer.parseInt(args[0]);
        }
        RaindropsFrame frame = new RaindropsFrame();
        frame.add(new RaindropsPanel(intensity));
        frame.setVisible(true);

    }

}