package it.brunasti.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ThreeBodies extends JFrame {

    final static int CX = 640;
    final static int CY = 430;

    double k = 0.0000001;
    double sun = 100;

    Body jup;
    Body earth;

    int loops = 100000000;


    void setup() {
        jup = new Body();
        jup.name = "jupiter";
        jup.m = 5;
        jup.x = 0;
        jup.y = 340;
        jup.sy = 340;
        jup.vx = 0.000173;
        jup.vy = 0;

        earth = new Body();
        earth.name = "earth";
        earth.m = 0.001;
        earth.x = 0;
        earth.y = 350;
        earth.sx = -0;
        earth.sy = 345;
        earth.vx = 0.000450;
        earth.vy = -0.0000;
        earth.c = Color.blue;
    }


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
        g.setColor(Color.white);
        g.drawLine(i_x, i_y, i_x, i_y);

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

    void drawOrbitStep(Graphics g, Body body, int loops, Body[] others) throws CrashException {
        try {
            double ax = body.x;
            double ay = body.y;
            double vx = body.vx;
            double vy = body.vy;
            double bx = body.bx;
            double by = body.by;

            if ((bx < 0) && (ax > 0)) {
                body.round = body.round + 1;
                System.out.println("  "+body.name+" - round  "+body.round+" : "+vx+"|"+vy+" - L:"+loops);
            }
            body.bx = ax;
            body.by = ay;


            double d = Math.sqrt((ax * ax) + (ay * ay));
            if (d < 5) {
                System.out.println("  "+body.name+"  Crash!....");
                throw new CrashException(body.name);
//                break;
            }
            double f;
            f = -(k * sun) / (d * d);

            double fx;
            double fy;

            fy = f * (ay / d);
            fx = f * (ax / d);

            if (others != null) {
                for (int i=0; i< others.length; i++) {
                    double dx = ax - others[i].x;
                    double dy = ay - others[i].y;
                    d = Math.sqrt((dx * dx) + (dy * dy));
                    if (d < 1) {
                        System.out.println("  "+body.name+"  Crashed on "+others[i].name+" L:"+loops);
                        throw new CrashException(body.name+" on "+others[i].name+" at loop "+loops);
                    }

                    f = -(k * others[i].m) / (d * d);

                    fy = fy + f * (dy / d);
                    fx = fx + f * (dx / d);
                }
            }

            body.x = ax + vx;
            body.y = ay + vy;

            body.vx = vx + fx;
            body.vy = vy + fy;

            drawRoute(g, body.x, body.y, body.bx, body.by, body.c);
        } catch (ArithmeticException ae) {
            System.out.println(ae.getMessage());
        }
    }


    void drawOrbit(Graphics g) {
        try {
            Body[] others = new Body[1];
            others[0] = jup;
            for (int i=0; i<loops; i++) {
                drawOrbitStep(g,jup,i, null);
                drawOrbitStep(g,earth,i, others);
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
