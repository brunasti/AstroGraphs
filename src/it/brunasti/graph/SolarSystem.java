package it.brunasti.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class SolarSystem extends JFrame {

    static final int CX = 640;
    static final int CY = 435;

    double k = 0.00000001;
    double sun = 1000;

    transient Body[] bodies;

    int loops = 100000000;

    void setup() {
        bodies = new Body[15];

        Body b;

        b = new Body();
        b.name = "venus";
        b.m = 0.0025;
        b.x = 0;
        b.y = 35;
        b.sx = -0;
        b.sy = 35;
        b.vx = 0.000535;
        b.vy = -0.0000;
        b.c = Color.green;
        bodies[0] = b;

        b = new Body();
        b.name = "earth";
        b.m = 0.003;
        b.x = 0;
        b.y = 50;
        b.sx = -0;
        b.sy = 50;
        b.vx = 0.000446;
        b.vy = -0.0000;
        b.c = Color.blue;
        bodies[1] = b;

        b = new Body();
        b.name = "mars";
        b.m = 0.001;
        b.x = 0;
        b.y = 75;
        b.sx = -0;
        b.sy = 75;
        b.vx = 0.000365;
        b.vy = -0.0000;
        b.c = Color.red;
        bodies[2] = b;

//        b = new Body();
//        b.name = "A1";
//        b.m = 0.00001;
//        b.x = 0;
//        b.y = 140;
//        b.sy = 140;
//        b.vx = 0.000255;
//        b.vy = 0;
//        b.c = Color.lightGray;
//        bodies[3] = b;
//
//        b = new Body();
//        b.name = "A2";
//        b.m = 0.00001;
//        b.x = 5;
//        b.y = 140;
//        b.sy = 140;
//        b.vx = 0.00026;
//        b.vy = 0;
//        b.c = Color.lightGray;
//        bodies[4] = b;
//
//        b = new Body();
//        b.name = "A3";
//        b.m = 0.00001;
//        b.x = 0;
//        b.y = 145;
//        b.sy = 145;
//        b.vx = 0.000265;
//        b.vy = 0;
//        b.c = Color.lightGray;
//        bodies[5] = b;


        b = new Body();
        b.name = "jupiter";
        b.m = 3;
        b.x = 0;
        b.y = 250;
        b.sy = 250;
        b.vx = 0.0002;
        b.vy = 0;
        b.c = Color.yellow;
        bodies[7] = b;

//        b = new Body();
//        b.name = "saturn";
//        b.m = 1.3;
//        b.x = 0;
//        b.y = 480;
//        b.sy = 480;
//        b.vx = 0.000148;
//        b.vy = 0;
//        b.c = Color.magenta;
//        bodies[8] = b;


        b = new Body();
        b.name = "C1";
        b.m = 0.0000001;
        b.x = 0;
        b.y = 345;
        b.sy = 345;
        b.vx = 0.0000265;
        b.vy = 0;
        b.c = Color.lightGray;
        bodies[10] = b;

        b = new Body();
        b.name = "C2";
        b.m = 0.0000001;
        b.x = 300;
        b.y = 200;
        b.sx = 300;
        b.sy = 200;
        b.vx = 0.0000365;
        b.vy = -0.00002;
        b.c = Color.lightGray;
        bodies[11] = b;

        b = new Body();
        b.name = "C3";
        b.m = 0.0000001;
        b.x = 400;
        b.y = -200;
        b.sx = 400;
        b.sy = -200;
        b.vx = 0.0000265;
        b.vy = 0.000051;
        b.c = Color.lightGray;
        bodies[12] = b;


        for (int x=0; x<bodies.length; x++) {
            if (bodies[x] != null) {
                bodies[x].others = bodies;
            }
        }

    }


    public SolarSystem() {
        super("Solar System");

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

        Shape theCircle = new Ellipse2D.Double(CX - 200, CY - 200, 2.0 * 200, 2.0 * 200);
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
                log("  "+body.name+" -        round "+body.round+" - Y :  "+ay+" : "+vx+"|"+vy+" - L:"+loops);
            }
            if ((eX > 0) && (ax < 0)) {
                log("  "+body.name+" - oppose round "+body.round+" - Y : "+ay+" : "+vx+"|"+vy+" - L:"+loops);
            }
            body.bx = ax;
            body.by = ay;


            double d = Math.sqrt((ax * ax) + (ay * ay));
            if (d < 1) {
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
                    if ((others[i] != null) && (others[i] != body)) {
                        double dx = ax - others[i].x;
                        double dy = ay - others[i].y;
                        d = Math.sqrt((dx * dx) + (dy * dy));
                        if (d < 0.01) {
                            log("  " + body.name + "  Crashed on " + others[i].name + " L:" + loops);
                            throw new CrashException(body.name + " on " + others[i].name + " at loop " + loops);
                        }

                        f = -(k * others[i].m) / (d * d);

                        fy = fy + f * (dy / d);
                        fx = fx + f * (dx / d);
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


    void drawOrbit(Graphics g) {
        try {
            for (int i=0; i<loops; i++) {
                for (int b = 0; b<bodies.length; b++) {
                    if (bodies[b] != null) {
                        drawOrbitStep(g, bodies[b], i);
                    }
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
                new SolarSystem().setVisible(true);
            }
        });
    }

}
