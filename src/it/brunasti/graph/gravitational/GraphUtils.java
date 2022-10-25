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

        g.setColor(Color.white);
        drawSegment(g, ax, ay, ax, ay);
    }


}
