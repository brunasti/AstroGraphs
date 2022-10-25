package it.brunasti.graph.gravitational;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreeBodies extends JFrame {

    static final int CX = 640;
    static final int CY = 435;

    double k = 0.0000003;
    double sun = 60;

    transient Body[] bodies;

    int loops = 350000000;

    void setup() {
        bodies = new Body[5];

        Body b;

        b = new Body();
        b.name = "venus";
        b.m = 0.0025;
        b.x = 2;
        b.y = 35;
        b.sx = 2;
        b.sy = 35;
        b.vx = 0.0006435;
        b.vy = -0.0000;
        b.c = Color.green;
//        bodies[0] = b;

        b = new Body();
        b.name = "earth";
        b.m = 0.003;
        b.x = 0;
        b.y = 50;
        b.sx = -0;
        b.sy = 50;
        b.vx = 0.000496;
        b.vy = -0.0000;
        b.c = Color.blue;
//        bodies[1] = b;

        b = new Body();
        b.name = "mars";
        b.m = 0.001;
        b.x = 0;
        b.y = 80;
        b.sx = -0;
        b.sy = 75;
        b.vx = 0.0004365;
        b.vy = -0.0000;
        b.c = Color.red;
        bodies[2] = b;

        b = new Body();
        b.name = "jupiter";
        b.m = 5;
        b.x = 0;
        b.y = 250;
        b.sy = 250;
        b.vx = 0.000245;
        b.vy = 0;
        b.c = Color.yellow;
        bodies[3] = b;

        b = new Body();
        b.name = "saturn";
        b.m = 3;
        b.x = 0;
        b.y = 500;
        b.sy = 500;
        b.vx = 0.000142;
        b.vy = 0;
        b.c = Color.magenta;
        bodies[4] = b;


        for (int x=0; x<bodies.length; x++) {
            if (bodies[x] != null) {
                bodies[x].others = bodies;
            }
        }
//        Body[] others;
//
//        others= new Body[3];
//        others[0] = bodies[0];
//        others[1] = bodies[1];
//        others[2] = bodies[2];
//        others[3] = bodies[3];
//        others[4] = bodies[4];
//        bodies[1].others = others;
//
//        others= new Body[3];
//        others[0] = bodies[0];
//        others[1] = bodies[1];
//        others[2] = bodies[3];
//        bodies[2].others = others;
//
//        others= new Body[3];
//        others[0] = bodies[1];
//        others[1] = bodies[2];
//        others[2] = bodies[3];
//        bodies[0].others = others;
//
//        others= new Body[3];
//        others[0] = bodies[0];
//        others[1] = bodies[1];
//        others[2] = bodies[2];
//        bodies[3].others = others;

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

        Shape theCircle = new Ellipse2D.Double(CX - bodies[3].sy, CY - bodies[3].sy, 2.0 * bodies[3].sy, 2.0 * bodies[3].sy);
        g2d.draw(theCircle);
    }

    void drawOrbitStep(Graphics g, Body body, int loops) throws CrashException {
        if (body != null) {
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
                    log("  " + body.name + " - round  " + body.round + " : " + vx + "|" + vy + " - L:" + loops);
                }
                if ((eX > 0) && (ax < 0)) {
                    log("  opposite " + body.name + " - Y : " + ay);
                }
                body.bx = ax;
                body.by = ay;


                double d = Math.sqrt((ax * ax) + (ay * ay));
                if (d < 2) {
                    log("  " + body.name + "  Crash!....");
                    throw new CrashException(body.name);
                }
                double f;
                f = -(k * sun) / (d * d);

                double fx;
                double fy;

                fy = f * (ay / d);
                fx = f * (ax / d);

                if (others != null) {
                    for (int i = 0; i < others.length; i++) {
                        if (others[i] != body) {
                            if (others[i] != null) {
                                double dx = ax - others[i].x;
                                double dy = ay - others[i].y;
                                d = Math.sqrt((dx * dx) + (dy * dy));
                                if (d < 1) {
                                    log("  " + body.name + "  Crashed on " + others[i].name + " L:" + loops);
                                    throw new CrashException(body.name + " on " + others[i].name + " at loop " + loops);
                                }

                                f = -(k * others[i].m) / (d * d);

                                fy = fy + f * (dy / d);
                                fx = fx + f * (dx / d);
                            }
                        }
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
    }


    void drawOrbit(Graphics g) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss z");
        long startTime = System.currentTimeMillis();
        Date date = new Date(startTime);
        System.out.println("Start : "+formatter.format(date));
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
        long endTime = System.currentTimeMillis();
        date = new Date(endTime);
        System.out.println("End : "+formatter.format(date));
        long deltaTime = endTime - startTime;
        date = new Date(deltaTime);
        System.out.println("Took : "+formatter.format(date));
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
