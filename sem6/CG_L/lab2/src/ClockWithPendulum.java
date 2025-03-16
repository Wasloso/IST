import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.awt.*;
import javax.swing.*;

public class ClockWithPendulum {

    public static void main(String[] args) {
        double period = 5;
        double amplitude = 0.5;
        if (args.length == 2) {
            period = Double.parseDouble(args[0]);
            amplitude = Double.parseDouble(args[1]);
        }
        amplitude = Math.max(Math.min(amplitude, Math.PI / 2), -Math.PI / 2);

        SmpWindow wnd = new SmpWindow(period, amplitude);

        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wnd.setSize(400, 400);
        wnd.setMinimumSize(new Dimension(300, 300));
        wnd.setVisible(true);

        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            wnd.repaint();
        }
    }
}

class DrawWndPane extends JPanel {

    final int GAUGE_LEN = 10;
    int center_x, center_y;
    int r_outer, r_inner;
    GregorianCalendar calendar;
    double period;
    double amplitude;
    long startTime;
    int ballRadius;

    DrawWndPane(double period, double amplitude, int ballRadius) {
        super();
        setBackground(new Color(200, 200, 255));
        calendar = new GregorianCalendar();
        this.period = period;
        this.amplitude = amplitude;
        this.ballRadius = ballRadius;
        this.startTime = System.currentTimeMillis();

    }

    public void DrawGauge(double angle, Graphics g) {
        int xw, yw, xz, yz;

        angle = Math.toRadians(angle);
        xw = (int) (center_x + r_inner * Math.sin(angle));
        yw = (int) (center_y - r_inner * Math.cos(angle));
        xz = (int) (center_x + r_outer * Math.sin(angle));
        yz = (int) (center_y - r_outer * Math.cos(angle));

        g.drawLine(xw, yw, xz, yz);
    }

    public void DrawHand(double angle, int length, Graphics g) {
        int xw, yw, xz, yz;

        angle = Math.toRadians(angle);
        xw = (int) (center_x + length * Math.sin(angle));
        yw = (int) (center_y - length * Math.cos(angle));

        angle += Math.PI;
        xz = (int) (center_x + GAUGE_LEN * Math.sin(angle));
        yz = (int) (center_y - GAUGE_LEN * Math.cos(angle));

        g.drawLine(xw, yw, xz, yz);
    }

    public void DrawDial(Graphics g) {
        g.drawOval(center_x - r_outer,
                center_y - r_outer,
                2 * r_outer, 2 * r_outer);

        for (int i = 0; i <= 11; i++)
            DrawGauge(i * 30.0, g);
    }

    public void DrawPendulum(double angle, int length, Graphics g) {

        int anchorX = center_x;
        int anchorY = center_y + r_outer;

        int ballX = (int) (anchorX + length * Math.sin(angle));
        int ballY = (int) (anchorY + length * Math.cos(angle));

        g.drawLine(anchorX, anchorY, ballX, ballY);

        g.fillOval(ballX - ballRadius / 2, ballY - ballRadius / 2, ballRadius, ballRadius);
    }

    public void paint(Graphics g) {
        paintComponent(g);
    }

    public void paintComponent(Graphics g) {
        int minute, second, hour;

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize();

        center_x = size.width / 2;
        r_outer = Math.min((size.height - ballRadius / 2) / 6, size.width / 2);
        center_y = r_outer;
        r_inner = r_outer - GAUGE_LEN;

        Date time = new Date();
        calendar.setTime(time);

        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR);
        if (hour > 11)
            hour = hour - 12;
        second = calendar.get(Calendar.SECOND);

        DrawDial(g);

        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(5));
        DrawHand(360.0 * (hour * 60 + minute) / (60.0 * 12), (int) (0.75 * r_inner), g);

        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        DrawHand(360.0 * (minute * 60 + second) / (3600.0), (int) (0.97 * r_outer), g);

        g2d.setColor(new Color(0, 0, 0));
        g2d.setStroke(new BasicStroke(1));
        DrawHand(second * 6.0, (int) (0.97 * r_inner), g);
        double angle = amplitude * Math.sin((2 * Math.PI * (System.currentTimeMillis() - startTime) / 1000.0) / period);
        DrawPendulum(angle, 4 * r_outer, g);
    }
}

class SmpWindow extends JFrame {
    public SmpWindow(double period, double amplitude) {
        Container contents = getContentPane();
        contents.add(new DrawWndPane(period, amplitude, 15));
        setTitle("Clock");
    }
}