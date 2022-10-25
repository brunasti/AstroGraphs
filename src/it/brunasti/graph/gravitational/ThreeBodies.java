package it.brunasti.graph.gravitational;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreeBodies extends JFrame {

    static final int CENTER_X = 640;
    static final int CENTER_Y = 435;

    GraphUtils graphUtils;

    double k = 0.0000003;
    double sun = 60;

    transient Body[] bodies;

    int loops = 350000000;

    void setup() {
        bodies = new Body[5];

        Body body;

        body = new Body();
        body.name = "venus";
        body.mass = 0.0025;
        body.positionX = 2;
        body.positionY = 35;
        body.velocityX = 0.0006435;
        body.velocityY = -0.0000;
        body.color = Color.green;
//        bodies[0] = b;

        body = new Body();
        body.name = "earth";
        body.mass = 0.003;
        body.positionX = 0;
        body.positionY = 50;
        body.velocityX = 0.000496;
        body.velocityY = -0.0000;
        body.color = Color.blue;
//        bodies[1] = b;

        body = new Body();
        body.name = "mars";
        body.mass = 0.001;
        body.positionX = 0;
        body.positionY = 80;
        body.velocityX = 0.0004365;
        body.velocityY = -0.0000;
        body.color = Color.red;
        bodies[2] = body;

        body = new Body();
        body.name = "jupiter";
        body.mass = 5;
        body.positionX = 0;
        body.positionY = 250;
        body.velocityX = 0.000245;
        body.velocityY = 0;
        body.color = Color.yellow;
        bodies[3] = body;

        body = new Body();
        body.name = "saturn";
        body.mass = 3;
        body.positionX = 0;
        body.positionY = 500;
        body.velocityX = 0.000142;
        body.velocityY = 0;
        body.color = Color.magenta;
        bodies[4] = body;

        for (int x=0; x<bodies.length; x++) {
            if (bodies[x] != null) {
                bodies[x].otherBodies = bodies;
            }
        }
    }

    public ThreeBodies() {
        super("Three Bodies Problem");

        getContentPane().setBackground(Color.BLACK);
        setSize(CENTER_X *2, CENTER_Y *2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        graphUtils = new GraphUtils(CENTER_X, CENTER_Y);
    }

    void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.white);

        g2d.draw(new Rectangle2D.Float(5, 25, (CENTER_X * 2f) - 15, (CENTER_Y * 2f) - 30));

        g.drawLine(5, CENTER_Y, (CENTER_X * 2)-10, CENTER_Y);
        g.drawLine(CENTER_X, 25, CENTER_X, (CENTER_Y * 2) - 5);

        Shape theCircle = new Ellipse2D.Double(CENTER_X - 200, CENTER_Y - 200, 2.0 * 200, 2.0 * 200);
        g2d.draw(theCircle);
    }

    void drawOrbitStep(Graphics g, Body body, int loops) throws CrashException {
        if (body != null) {
            try {
                double ax = body.positionX;
                double ay = body.positionY;
                double vx = body.velocityX;
                double vy = body.velocityY;
                double eX = body.prePositionX;
                double eY = body.prePositionY;
                Body[] others = body.otherBodies;

                if ((eX < 0) && (ax > 0)) {
                    body.round = body.round + 1;
                    log("  " + body.name + " - round  " + body.round + " : " + vx + "|" + vy + " - L:" + loops);
                }
                if ((eX > 0) && (ax < 0)) {
                    log("  opposite " + body.name + " - Y : " + ay);
                }
                body.prePositionX = ax;
                body.prePositionY = ay;


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
                                double dx = ax - others[i].positionX;
                                double dy = ay - others[i].positionY;
                                d = Math.sqrt((dx * dx) + (dy * dy));
                                if (d < 1) {
                                    log("  " + body.name + "  Crashed on " + others[i].name + " L:" + loops);
                                    throw new CrashException(body.name + " on " + others[i].name + " at loop " + loops);
                                }

                                f = -(k * others[i].mass) / (d * d);

                                fy = fy + f * (dy / d);
                                fx = fx + f * (dx / d);
                            }
                        }
                    }
                }

                body.positionX = ax + vx;
                body.positionY = ay + vy;

                body.velocityX = vx + fx;
                body.velocityY = vy + fy;

                graphUtils.drawRoute(g, body.positionX, body.positionY, body.prePositionX, body.prePositionY, body.color);
            } catch (ArithmeticException ae) {
                log(ae.getMessage());
            }
        }
    }


    void drawOrbit(Graphics g) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss z");
        long startTime = System.currentTimeMillis();
        Date date = new Date(startTime);
        log("Start : "+formatter.format(date));
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
        log("End : "+formatter.format(date));
        long deltaTime = endTime - startTime;
        date = new Date(deltaTime);
        log("Took : "+formatter.format(date));
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
