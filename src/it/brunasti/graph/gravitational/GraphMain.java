package it.brunasti.graph.gravitational;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class GraphMain extends JFrame {


    final static int CENTRE_X = 500;
    final static int CENTRE_Y = 400;

    GraphUtils graphUtils;

    public GraphMain() {
        super("Multi dimensional gravitational fields");

        getContentPane().setBackground(Color.BLACK);
        setSize(CENTRE_X *2, CENTRE_Y *2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        graphUtils = new GraphUtils(CENTRE_X, CENTRE_Y);
    }

    public void paint(Graphics g) {
        drawAllOrbits(g);
    }

    private void drawAllOrbits(Graphics g) {
        super.paint(g);
        drawGrid(g);

        g.setColor(Color.blue);
        drawOrbit(g,1);
        g.setColor(Color.green);
        drawOrbit(g,2);
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


    // Equilibrio - around
    double grevitationalConstant = 0.00001;
    double sunMass = 1;

    double initialPlanetX = 0;
    double initialPlanetY = 300;
    double initialVelocityX = 0.000181;
    double initialVelocityY = 0;
    int loops = 75000000;

    void drawOrbit(Graphics g, int gravitationType) {

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
                force = -(grevitationalConstant * sunMass) / (distance / correctionFactor);
                break;
            case 2:
                force = -(grevitationalConstant * sunMass) / (distance * distance);
                break;
            case 3:
                force = -(grevitationalConstant * sunMass) / (distance * distance * distance / correctionFactor);
                break;
            case 4:
                force = -(grevitationalConstant * sunMass) / (distance * distance * distance * distance / correctionFactor);
                break;
            case 5:
                force = -(grevitationalConstant * sunMass) / (distance * distance * distance * distance * distance / correctionFactor);
                break;
            case 6:
                force = -(grevitationalConstant * sunMass) / (distance * distance * distance * distance * distance * distance / correctionFactor);
                break;
        }
        return force;
    }

    void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.white);

        g2d.draw(new Rectangle2D.Float(5, 25, CENTRE_X * 2 - 15, CENTRE_Y * 2 - 30));

        g.drawLine(5, CENTRE_Y, (CENTRE_X * 2)-10, CENTRE_Y);
        g.drawLine(CENTRE_X, 25, CENTRE_X, (CENTRE_Y * 2) - 5);

        Shape theCircle = new Ellipse2D.Double(CENTRE_X - initialPlanetY, CENTRE_Y - initialPlanetY, 2.0 * initialPlanetY, 2.0 * initialPlanetY);
        g2d.draw(theCircle);

        g.drawString("K : " + grevitationalConstant, 10, 40);
        g.drawString("Vx: " + initialVelocityX, 10, 55);
        g.drawString("Vy: " + initialVelocityY, 10, 70);
        g.drawString("L : " + loops, 10, 85);
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GraphMain().setVisible(true);
            }
        });
    }

}
