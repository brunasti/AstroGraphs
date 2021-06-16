package it.brunasti.graph.fractals;

import javax.swing.*;
import java.awt.*;

public class JuliaSet extends FractalBase {

    // escape radius  # choose R > 0 such that R**2 - R >= sqrt(cx**2 + cy**2)
    float radius = 20f;
    float cx = 0.4f;
    float cy = 0.0f;


    @Override
    void setPrams() {
//        radius = 2.5f;
//        cx = 0.4f;
//        cy = 0.2f;

//        // Garofani
//        radius = 2.5f;
//        cx = 0.4f;
//        cy = 0.3f;
//
//        fromX = -0.7f;
//        toX = 0.5f;
//        fromY = -1f;
//        colorOption = 4;
////        gridFlag = true;

//        // Red spirals
//        radius = 2.5f;
//        cx = 0.5f;
//        cy = 0.3f;
//
//        fromX = -0.7f;
//        toX = 0.3f;
//        fromY = -0.9f;
//        colorOption = 4;


//        // Red Wave
//        radius = 1.5f;
//        cx = 0.3f;
//        cy = 0.3f;
//
//        fromX = -0f;
//        toX = 0.05f;
//        fromY = -0.95f;
//        colorOption = 4;

//        fromX = -0f;
//        toX = 0.1f;
//        fromY = -0.95f;
//        colorOption = 4;

//        gridFlag = false;
        gridSize = 20;
    }


    // escape radius  # choose R > 0 such that R**2 - R >= sqrt(cx**2 + cy**2)

    @Override
    void setup() {
        super.setup();
        log("radius "+radius);
        log("cx:"+cx+" cy:"+cy);
        log("  -> "+(radius*radius - radius)+" >= "+Math.sqrt((cx*cx) + (cy*cy)));

        if (radius<0) {
            log("  - radius negative");
            System.exit(-1);
        }
        if ((radius*radius - radius) < Math.sqrt((cx*cx) + (cy*cy))) {
            log("  - radius too small : "+radius+" -> "+(radius*radius - radius)+" >= "+Math.sqrt((cx*cx) + (cy*cy)));
            System.exit(-1);
        }
    }


    @Override
    int computeIteration(int px, int py) {
        var iteration = 0;

        float zx = ((px * (toX - fromX)) / FRAME_SIZE_X) + fromX;
        float zy = ((py * (toY - fromY)) / FRAME_SIZE_Y) + fromY;

        while (((zx*zx + zy*zy) < (radius * radius)) && (iteration < MAX_ITERATION)) {
            float xtemp = zx*zx - zy*zy;
            zy = 2*zx*zy + cy;
            zx = xtemp + cx;
            iteration = iteration + 1;
        }
        return iteration;
    }

    public JuliaSet() throws HeadlessException {
        super("Julia Set");

        minX = -2f;
        maxX = 2f;

        fromX = minX;
        toX = maxX;

        minY = -1.4f;

        fromY = minY;

        colorOption = 4;

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JuliaSet().setVisible(true);
            }
        });
    }

}
