package it.brunasti.graph.gravitational;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class SolarSystem extends JFrame {

    GraphUtils graphUtils;

    static final int CENTER_X = 640;
    static final int CENTER_Y = 435;

    double gravitationalConstant = 0.00000001;
    double sunMass = 1000;

    transient Body[] bodies;

    int loops = 30000000;

    void setup() {
        bodies = new Body[15];

        Body body;

        body = new Body();
        body.name = "venus";
        body.mass = 0.0025;
        body.positionX = 0;
        body.positionY = 35;
        body.velocityX = 0.000535;
        body.velocityY = -0.0000;
        body.color = Color.green;
        bodies[0] = body;

        body = new Body();
        body.name = "earth";
        body.mass = 0.003;
        body.positionX = 0;
        body.positionY = 50;
        body.velocityX = 0.000446;
        body.velocityY = -0.0000;
        body.color = Color.blue;
        bodies[1] = body;

        body = new Body();
        body.name = "mars";
        body.mass = 0.001;
        body.positionX = 0;
        body.positionY = 75;
        body.velocityX = 0.000365;
        body.velocityY = -0.0000;
        body.color = Color.red;
        bodies[2] = body;

        body = new Body();
        body.name = "A1";
        body.mass = 0.00001;
        body.positionX = 0;
        body.positionY = 140;
        body.velocityX = 0.000255;
        body.velocityY = 0;
        body.color = Color.lightGray;
        bodies[3] = body;

        body = new Body();
        body.name = "A2";
        body.mass = 0.00001;
        body.positionX = 5;
        body.positionY = 140;
        body.velocityX = 0.00026;
        body.velocityY = 0;
        body.color = Color.lightGray;
        bodies[4] = body;

        body = new Body();
        body.name = "A3";
        body.mass = 0.00001;
        body.positionX = 0;
        body.positionY = 145;
        body.velocityX = 0.000265;
        body.velocityY = 0;
        body.color = Color.lightGray;
        bodies[5] = body;


        body = new Body();
        body.name = "jupiter";
        body.mass = 3;
        body.positionX = 0;
        body.positionY = 250;
        body.velocityX = 0.0002;
        body.velocityY = 0;
        body.color = Color.yellow;
        bodies[7] = body;

        body = new Body();
        body.name = "saturn";
        body.mass = 1.3;
        body.positionX = 0;
        body.positionY = 480;
        body.velocityX = 0.000148;
        body.velocityY = 0;
        body.color = Color.magenta;
        bodies[8] = body;


        body = new Body();
        body.name = "C1";
        body.mass = 0.0000001;
        body.positionX = 0;
        body.positionY = 345;
        body.velocityX = 0.0000265;
        body.velocityY = 0;
        body.color = Color.lightGray;
        bodies[10] = body;

        body = new Body();
        body.name = "C2";
        body.mass = 0.0000001;
        body.positionX = 300;
        body.positionY = 200;
        body.velocityX = 0.0000365;
        body.velocityY = -0.00002;
        body.color = Color.lightGray;
        bodies[11] = body;

        body = new Body();
        body.name = "C3";
        body.mass = 0.0000001;
        body.positionX = 400;
        body.positionY = -200;
        body.velocityX = 0.0000265;
        body.velocityY = 0.000051;
        body.color = Color.lightGray;
        bodies[12] = body;

        for (int x=0; x<bodies.length; x++) {
            if (bodies[x] != null) {
                bodies[x].otherBodies = bodies;
            }
        }
    }


    public SolarSystem() {
        super("Solar System");

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

    void computeAndDrawOrbitStep(Graphics g, Body body, int loops) throws CrashException {
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
                log("  "+body.name+" -        round "+body.round+" - Y :  "+ay+" : "+vx+"|"+vy+" - L:"+loops);
            }
            if ((eX > 0) && (ax < 0)) {
                log("  "+body.name+" - oppose round "+body.round+" - Y : "+ay+" : "+vx+"|"+vy+" - L:"+loops);
            }
            body.prePositionX = ax;
            body.prePositionY = ay;


            double d = Math.sqrt((ax * ax) + (ay * ay));
            if (d < 1) {
                log("  "+body.name+"  Crash!....");
                throw new CrashException(body.name);
            }
            double f;
            f = -(gravitationalConstant * sunMass) / (d * d);

            double fx;
            double fy;

            fy = f * (ay / d);
            fx = f * (ax / d);

            if (others != null) {
                for (int i=0; i< others.length; i++) {
                    if ((others[i] != null) && (others[i] != body)) {
                        double dx = ax - others[i].positionX;
                        double dy = ay - others[i].positionY;
                        d = Math.sqrt((dx * dx) + (dy * dy));
                        if (d < 0.01) {
                            log("  " + body.name + "  Crashed on " + others[i].name + " L:" + loops);
                            throw new CrashException(body.name + " on " + others[i].name + " at loop " + loops);
                        }

                        f = -(gravitationalConstant * others[i].mass) / (d * d);

                        fy = fy + f * (dy / d);
                        fx = fx + f * (dx / d);
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


    void computeAndDrawOrbits(Graphics g) {
        try {
            for (int i=0; i<loops; i++) {
                for (int b = 0; b<bodies.length; b++) {
                    if (bodies[b] != null) {
                        computeAndDrawOrbitStep(g, bodies[b], i);
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
        computeAndDrawOrbits(g);
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
