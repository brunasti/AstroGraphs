package it.brunasti.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ThreeBodies extends JFrame {

    final static int CX = 500;
    final static int CY = 400;

    public ThreeBodies() {
        super("Three Bodies Problem");

        getContentPane().setBackground(Color.BLACK);
        setSize(CX*2, CY*2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    void drawRoute(Graphics g, double ax, double ay, double bx, double by, Color c) {
        g.setColor(c);

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

        Shape theCircle = new Ellipse2D.Double(CX - jup.sy, CY - jup.sy, 2.0 * jup.sy, 2.0 * jup.sy);
        g2d.draw(theCircle);
    }

    // Equilibrio - around
    double k = 0.000001;
    double m = 0.01;
    double sun = 100;

    Body jup;
    Body earth;


    int loops = 4500000;

    void setup() {
        jup = new Body();

        jup.m = 1;
        jup.x = 0;
        jup.y = 100;
        jup.sy = 100;

        jup.vx = 0.0008;
        jup.vy = 0;
        jup.name = "jupiter";

        earth = new Body();

        earth.m = 0.001;
        earth.x = 250;
        earth.y = 0;
        earth.sy = 250;
        earth.vx = -0.0000;
        earth.vy = -0.0004;
        earth.c = Color.blue;
        earth.name = "earth";
    }


    void drawOrbitStep(Graphics g, Body jup, int i) throws CrashException {
        try {
            double ax = jup.x;
            double ay = jup.y;
            double vx = jup.vx;
            double vy = jup.vy;
            double bx = jup.bx;
            double by = jup.by;

            if ((bx < 0) && (ax > 0)) {
                jup.round = jup.round + 1;
                System.out.println("  "+jup.name+" - round  "+jup.round+" : "+vx+"|"+vy+" - L"+i);
            }
            jup.bx = ax;
            jup.by = ay;

            double d = Math.sqrt((ax * ax) + (ay * ay));
            if (d < 5) {
                System.out.println("  "+jup.name+"  Crash!....");
                throw new CrashException(jup.name);
//                break;
            }
            double f = 0;
            f = -(k * sun) / (d * d);

            double fx = 0;
            double fy = 0;

            fy = f * (ay / d);
            fx = f * (ax / d);

            jup.x = ax + vx;
            jup.y = ay + vy;

            jup.vx = vx + fx;
            jup.vy = vy + fy;

            drawRoute(g, jup.x, jup.y, jup.bx, jup.by, jup.c);
        } catch (ArithmeticException ae) {
            System.out.println(ae.getMessage());
        }
    }


    void drawOrbit(Graphics g) {
        try {
            for (int i=0; i<loops; i++) {
                drawOrbitStep(g,jup,i);
                drawOrbitStep(g,earth,i);
            }
        } catch (ArithmeticException ae) {
            System.out.println(ae.getMessage());
        } catch (CrashException ce) {
            System.out.println("Crash!......."+ce.getMessage());
        }
    }

    public void paint(Graphics g) {
        setup();
        super.paint(g);
        drawGrid(g);
        drawOrbit(g);
        drawGrid(g);
        System.out.println("DONE");
    }

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThreeBodies().setVisible(true);
            }
        });
    }

}
