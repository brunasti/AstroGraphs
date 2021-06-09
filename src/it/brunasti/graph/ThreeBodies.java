package it.brunasti.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ThreeBodies extends JFrame {

    static final int CX = 640;
    static final int CY = 430;

    double k = 0.0000001;
    double sun = 100;

    transient Body[] bodies;

    int loops = 150000000;


    void setup() {
        bodies = new Body[4];

        Body b;

        b = new Body();
        b.name = "jupiter";
        b.m = 1;
        b.x = 0;
        b.y = 240;
        b.sy = 240;
        b.vx = 0.000205;
        b.vy = 0;
        b.c = Color.blue;
        bodies[0] = b;

        b = new Body();
        b.name = "saturn";
        b.m = 3;
        b.x = 0;
        b.y = 350;
        b.sy = 350;
        b.vx = 0.000170;
        b.vy = 0;
        b.c = Color.magenta;
        bodies[3] = b;

        b = new Body();
        b.name = "mars";
        b.m = 0.01;
        b.x = 0;
        b.y = 130;
        b.sx = -0;
        b.sy = 130;
        b.vx = 0.000290;
        b.vy = -0.0000;
        b.c = Color.red;
        bodies[1] = b;

        b = new Body();
        b.name = "earth";
        b.m = 0.05;
        b.x = 0;
        b.y = 70;
        b.sx = -0;
        b.sy = 70;
        b.vx = 0.000350;
        b.vy = -0.0000;
        b.c = Color.darkGray;
        bodies[2] = b;


        Body[] others;

        others= new Body[3];
        others[0] = bodies[0];
        others[1] = bodies[2];
        others[2] = bodies[3];
        bodies[1].others = others;

        others= new Body[3];
        others[0] = bodies[0];
        others[1] = bodies[1];
        others[2] = bodies[3];
        bodies[2].others = others;

        others= new Body[3];
        others[0] = bodies[1];
        others[1] = bodies[2];
        others[2] = bodies[3];
        bodies[0].others = others;

        others= new Body[3];
        others[0] = bodies[0];
        others[1] = bodies[1];
        others[2] = bodies[2];
        bodies[3].others = others;

    }


    public ThreeBodies() {
        super("Three Bodies Problem");

        getContentPane().setBackground(Color.BLACK);
        setSize(CX*2, CY*2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    void drawRoute(Graphics g, double ax, double ay, double bx, double by, Color c) {
        g.setColor(c);

        long aX = CX + Math.round(ax);
        long aY = CY - Math.round(ay);
        long bX = CX + Math.round(bx);
        long bY = CY - Math.round(by);

        int iX = Math.toIntExact(aX);
        int iY = Math.toIntExact(aY);
        int jX = Math.toIntExact(bX);
        int jY = Math.toIntExact(bY);

        g.drawLine(iX, iY, jX, jY);
        g.setColor(Color.white);
        g.drawLine(iX, iY, iX, iY);

    }

    void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.white);

        g2d.draw(new Rectangle2D.Float(5, 25, (CX * 2f) - 15, (CY * 2f) - 30));

        g.drawLine(5, CY, (CX * 2)-10, CY);
        g.drawLine(CX, 25, CX, (CY * 2) - 5);

        Shape theCircle = new Ellipse2D.Double(CX - bodies[0].sy, CY - bodies[0].sy, 2.0 * bodies[0].sy, 2.0 * bodies[0].sy);
        g2d.draw(theCircle);
    }

    void drawOrbitStep(Graphics g, Body body, int loops) throws CrashException {
        try {
            double ax = body.x;
            double ay = body.y;
            double vx = body.vx;
            double vy = body.vy;
            double eX = body.bx;
            double eY = body.by;
            Body[] others = body.others;

            if ((eX < 0) && (ax > 0)) {
                body.round = body.round + 1;
                log("  "+body.name+" - round  "+body.round+" : "+vx+"|"+vy+" - L:"+loops);
            }
            body.bx = ax;
            body.by = ay;


            double d = Math.sqrt((ax * ax) + (ay * ay));
            if (d < 5) {
                log("  "+body.name+"  Crash!....");
                throw new CrashException(body.name);
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
                        log("  "+body.name+"  Crashed on "+others[i].name+" L:"+loops);
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
            log(ae.getMessage());
        }
    }


    void drawOrbit(Graphics g) {
        try {
            for (int i=0; i<loops; i++) {
                for (int b = 0; b<bodies.length; b++) {
                    drawOrbitStep(g, bodies[b], i);
                }
            }
        } catch (ArithmeticException ae) {
            log(ae.getMessage());
        } catch (CrashException ce) {
            log("Crash!......."+ce.getMessage());
        }
    }

    @Override
    public void paint(Graphics g) {
        setup();
        super.paint(g);
        drawGrid(g);
        drawOrbit(g);
        drawGrid(g);
        log("DONE");
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThreeBodies().setVisible(true);
            }
        });
    }

}
