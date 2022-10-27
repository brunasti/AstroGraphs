package it.brunasti.graph.gravitational;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class DifferentGravitationTest extends AbstractGraphMain {

    double gravitationalConstant = 0.00001;
    double sunMass = 1;

    double initialPlanetX = 0;
    double initialPlanetY = 300;
    double initialVelocityX = 0.000181;
    double initialVelocityY = 0;
    int loops = 75000000;


    public DifferentGravitationTest() {
        super("Multi dimensional gravitational fields", 500, 430);
    }

    @Override
    public void paint(Graphics g) {
        drawAllOrbits(g);
    }

    private void drawAllOrbits(Graphics g) {
        super.paint(g);
        drawGrid(g);

        g.setColor(Color.blue);
        computeAndDrawOrbits(g,1);
        g.setColor(Color.green);
        computeAndDrawOrbits(g,2);
        g.setColor(Color.yellow);
        computeAndDrawOrbits(g,3);
        g.setColor(Color.orange);
        computeAndDrawOrbits(g,4);
        g.setColor(Color.pink);
        computeAndDrawOrbits(g,5);
        g.setColor(Color.red);
        computeAndDrawOrbits(g,6);

        drawGrid(g);
        System.out.println("DONE");
    }

    void computeAndDrawOrbits(Graphics g, int gravitationType) {

        try {
            double startX = initialPlanetX;
            double startY;
            double velocityX = initialVelocityX;
            double velocityY = initialVelocityY;

            double endX = initialPlanetX;
            double endY = initialPlanetY;

            int orbitsCounter = 0;

            for (int i=0; i<loops; i++) {
                if ((endX < 0) && (startX > 0)) {
                    orbitsCounter++;
                    System.out.println("  - orbit "+orbitsCounter+" "+velocityX+"|"+velocityY+" - L"+i);
                }
                startX = endX;
                startY = endY;

                double distance = Math.sqrt((startX * startX) + (startY * startY));
                if (distance < 5) {
                    System.out.println("Crash!....");
                    break;
                }

                double correctionFactor = Math.pow(initialPlanetY,(gravitationType-2));

                double force = forceCalculation(gravitationType, correctionFactor, distance);

                double forceY = force * (startY / distance);
                double forceX = force * (startX / distance);

                endX = startX + velocityX;
                endY = startY + velocityY;

                velocityX = velocityX + forceX;
                velocityY = velocityY + forceY;

                graphUtils.drawSegment(g, startX, startY, endX, endY);
            }
            System.out.println("Done "+gravitationType);

        } catch (ArithmeticException ae) {
            System.out.println(ae.getMessage());
        }
    }

    double forceCalculation(int gravitationType, double correctionFactor, double distance) {
        double force = 0;
        switch (gravitationType) {
            case 1:
                force = -(gravitationalConstant * sunMass) / (distance / correctionFactor);
                break;
            case 2:
                force = -(gravitationalConstant * sunMass) / (distance * distance);
                break;
            case 3:
                force = -(gravitationalConstant * sunMass) / (distance * distance * distance / correctionFactor);
                break;
            case 4:
                force = -(gravitationalConstant * sunMass) / (distance * distance * distance * distance / correctionFactor);
                break;
            case 5:
                force = -(gravitationalConstant * sunMass) / (distance * distance * distance * distance * distance / correctionFactor);
                break;
            case 6:
                force = -(gravitationalConstant * sunMass) / (distance * distance * distance * distance * distance * distance / correctionFactor);
                break;
        }
        return force;
    }

    Graphics2D drawGrid(Graphics g) {
        Graphics2D g2d = super.drawGrid(g);
        Shape theCircle = new Ellipse2D.Double(CENTER_X - initialPlanetY, CENTER_Y - initialPlanetY, 2.0 * initialPlanetY, 2.0 * initialPlanetY);
        g2d.draw(theCircle);

        g.drawString("K : " + gravitationalConstant, 10, 40);
        g.drawString("Vx: " + initialVelocityX, 10, 55);
        g.drawString("Vy: " + initialVelocityY, 10, 70);
        g.drawString("L : " + loops, 10, 85);

        return g2d;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DifferentGravitationTest().setVisible(true);
            }
        });
    }

}
