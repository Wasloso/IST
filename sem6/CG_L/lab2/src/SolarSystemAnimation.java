import javax.swing.*;
import java.awt.*;

class SolarSystemPanel extends JPanel {
    // TODO: MAybe adjust the orbit distances better

    private final CelestialBody sun = new CelestialBody(50, Color.YELLOW, 400, 400);
    private final CelestialBody mercury = new CelestialBody(39, 8, 0.1, Color.ORANGE, sun);
    private final CelestialBody venus = new CelestialBody(72, 10, 0.1, Color.RED, sun);
    private final CelestialBody earth = new CelestialBody(100, 15, 0.1, Color.BLUE, sun);
    private final CelestialBody moon = new CelestialBody(20, 8, 1, Color.LIGHT_GRAY, earth);
    private final CelestialBody mars = new CelestialBody(152, 12, 0.1, Color.RED, sun);
    private final CelestialBody jupiter = new CelestialBody(200, 25, 0.05, Color.DARK_GRAY, sun);
    private final CelestialBody saturn = new CelestialBody(250, 22, 0.04, Color.DARK_GRAY, sun);
    private final CelestialBody uranus = new CelestialBody(300, 20, 0.03, Color.GREEN, sun);
    private final CelestialBody neptune = new CelestialBody(350, 18, 0.02, Color.CYAN, sun);

    public SolarSystemPanel() {
        Timer timer = new Timer(25, e -> {
            mercury.updatePosition();
            venus.updatePosition();
            earth.updatePosition();
            moon.updatePosition();
            mars.updatePosition();
            jupiter.updatePosition();
            saturn.updatePosition();
            uranus.updatePosition();
            neptune.updatePosition();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.BLACK);
        sun.draw(g2d);
        mercury.draw(g2d);
        venus.draw(g2d);
        mars.draw(g2d);
        earth.draw(g2d);
        moon.draw(g2d);
        jupiter.draw(g2d);
        saturn.draw(g2d);
        uranus.draw(g2d);
        neptune.draw(g2d);
    }
}

class CelestialBody extends JComponent {
    private int orbitRadius;
    private final int size;
    private double velocity;
    private final Color color;
    private double angle = 0;
    private int x, y;
    private int centerX, centerY;
    private CelestialBody parent;

    public CelestialBody(int orbitRadius, int size, double baseSpeed, Color color, CelestialBody parent) {
        this.orbitRadius = orbitRadius;
        this.size = size;
        this.velocity = baseSpeed / Math.sqrt(orbitRadius);
        this.color = color;
        this.parent = parent;
        updatePosition();

    }

    public CelestialBody(int size, Color color, int centerX, int centerY) {
        this.size = size;
        this.color = color;
        this.x = centerX;
        this.y = centerY;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void updatePosition() {
        this.centerX = parent.getX();
        this.centerY = parent.getY();
        angle = (angle + velocity) % 360;
        x = centerX + (int) (orbitRadius * Math.cos(angle));
        y = centerY + (int) (orbitRadius * Math.sin(angle));
    }

    public void updatePosition(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        angle += velocity;
        x = centerX + (int) (orbitRadius * Math.cos(angle));
        y = centerY + (int) (orbitRadius * Math.sin(angle));
    }

    public void draw(Graphics2D g2d) {
        drawOrbit(g2d);
        g2d.setColor(color);
        g2d.fillOval(x - size / 2, y - size / 2, size, size);
    }

    private void drawOrbit(Graphics2D g2d) {
        g2d.setColor(Color.lightGray);
        g2d.drawOval(centerX - orbitRadius,
                centerY - orbitRadius,
                2 * orbitRadius, 2 * orbitRadius);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getOrbitRadius() {
        return orbitRadius;
    }

    public double getVelocity() {
        return velocity;
    }
}

public class SolarSystemAnimation {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Solar System");
        frame.add(new SolarSystemPanel());
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}