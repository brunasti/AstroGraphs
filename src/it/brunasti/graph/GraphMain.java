package it.brunasti.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class GraphMain extends JFrame {

    final static int CX = 500;
    final static int CY = 400;

    public GraphMain() {
        super("Multi dimensional gravitational fields");

        getContentPane().setBackground(Color.BLACK);
        setSize(CX*2, CY*2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    void drawRoute(Graphics g, double ax, double ay, double bx, double by) {
        long a_x = CX + Math.round(ax);
        long a_y = CY - Math.round(ay);
        long b_x = CX + Math.round(bx);
        long b_y = CY - Math.round(by);

        int i_x = Math.toIntExact(a_x);
        int i_y = Math.toIntExact(a_y);
        int j_x = Math.toIntExact(b_x);
        int j_y = Math.toIntExact(b_y);

        g.drawLine(i_x, i_y, j_x, j_y);
    }

    void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.white);

        g2d.draw(new Rectangle2D.Float(5, 25, CX * 2 - 15, CY * 2 - 30));

        g.drawLine(5, CY, (CX * 2)-10, CY);
        g.drawLine(CX, 25, CX, (CY * 2) - 5);

        Shape theCircle = new Ellipse2D.Double(CX - Y, CY - Y, 2.0 * Y, 2.0 * Y);
        g2d.draw(theCircle);

        g.drawString("K : " + k, 10, 40);
        g.drawString("Vx: " + Vx, 10, 55);
        g.drawString("Vy: " + Vy, 10, 70);
        g.drawString("L : " + loops, 10, 85);
    }

    // Equilibrio - around
    double k = 0.1;
    double m = 0.0001;
    double sun = 1;

    double X = 0;
    double Y = 300;
    double Vx = 0.0001225;
    double Vy = 0;
    int loops = 350000000;

//    // Nice 003 - around
//    double k = 0.001;
//    double m = 0.0001;
//    double sun = 1;
//
//    double X = 0;
//    double Y = 300;
//    double Vx = 0.00001825;
//    double Vy = 0;
//    int loops = 650000000;
//
//    // Nice 002 - around
//    double k = 0.001;
//    double m = 0.0001;
//    double sun = 1;
//
//    double X = 0;
//    double Y = 300;
//    double Vx = 0.0000182;
//    double Vy = 0;
//    int loops = 550000000;
//
//    // Nice 001 - around
//    double k = 0.001;
//    double m = 0.0001;
//    double sun = 1;
//
//    double X = 0;
//    double Y = 300;
//    double Vx = 0.000018;
//    double Vy = 0;
//    int loops = 550000000;
//
//    // Equilibrio - around
//    double k = 0.001;
//    double m = 0.0001;
//    double sun = 1;
//

    void drawOrbit(Graphics g, int gType) {

        try {
            double ax = X;
            double ay = Y;
            double vx = Vx;
            double vy = Vy;

            double bx = 0;
            double by = 0;

            for (int i=0; i<loops; i++) {
                bx = ax;
                by = ay;

                double d = Math.sqrt((ax * ax) + (ay * ay));
                if (d < 5) {
                    System.out.println("Crash!....");
                    break;
                }
                double f = 0;

                double fK = Math.pow(Y,(gType-2));

                switch (gType) {
                    case 1:
                        f = -(k * m * sun) / (d / fK);
                        break;
                    case 2:
                        f = -(k * m * sun) / (d * d);
                        break;
                    case 3:
                        f = -(k * m * sun) / (d * d * d / fK);
                        break;
                    case 4:
                        f = -(k * m * sun) / (d * d * d * d / fK);
                        break;
                    case 5:
                        f = -(k * m * sun) / (d * d * d * d * d / fK);
                        break;
                    case 6:
                        f = -(k * m * sun) / (d * d * d * d * d * d / fK);
                        break;
                }

                double fx = 0;
                double fy = 0;

                fy = f * (ay / d);
                fx = f * (ax / d);

                ax = ax + vx;
                ay = ay + vy;

                vx = vx + fx;
                vy = vy + fy;

                drawRoute(g, ax, ay, bx, by);
            }
            System.out.println("Done "+gType);

        } catch (ArithmeticException ae) {
            System.out.println(ae.getMessage());
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
        g.setColor(Color.blue);
        drawOrbit(g,1);
//        g.setColor(Color.green);
//        drawOrbit(g,2);
        g.setColor(Color.yellow);
        drawOrbit(g,3);
        g.setColor(Color.orange);
        drawOrbit(g,4);
        g.setColor(Color.pink);
        drawOrbit(g,5);
        g.setColor(Color.red);
        drawOrbit(g,6);
        drawGrid(g);
        System.out.println("DONE");
    }

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GraphMain().setVisible(true);
            }
        });
    }

}
