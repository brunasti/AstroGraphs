package it.brunasti.graph.gravitational;

import java.awt.*;

public class GraphUtils {

    int centerX;
    int centerY;

    public GraphUtils(int cx, int cy) {
        centerX = cx;
        centerY = cy;
    }

    void drawSegment(Graphics g, double ax, double ay, double bx, double by) {
        long a_x = centerX + Math.round(ax);
        long a_y = centerY - Math.round(ay);
        long b_x = centerX + Math.round(bx);
        long b_y = centerY - Math.round(by);

        int i_x = Math.toIntExact(a_x);
        int i_y = Math.toIntExact(a_y);
        int j_x = Math.toIntExact(b_x);
        int j_y = Math.toIntExact(b_y);

        g.drawLine(i_x, i_y, j_x, j_y);
    }


    void drawRoute(Graphics g, double ax, double ay, double bx, double by, Color c) {
        g.setColor(c);
        drawSegment(g, ax, ay, bx, by);

//        long aX = CX + Math.round(ax);
//        long aY = CY - Math.round(ay);
//        long bX = CX + Math.round(bx);
//        long bY = CY - Math.round(by);
//
//        int iX = Math.toIntExact(aX);
//        int iY = Math.toIntExact(aY);
//        int jX = Math.toIntExact(bX);
//        int jY = Math.toIntExact(bY);
//
//        g.drawLine(iX, iY, jX, jY);
//        g.setColor(Color.white);
//        g.drawLine(iX, iY, iX, iY);

        drawSegment(g, ax, ay, ax, ay);
    }


}
