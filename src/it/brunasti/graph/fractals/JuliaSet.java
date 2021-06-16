package it.brunasti.graph.fractals;

import javax.swing.*;
import java.awt.*;

public class JuliaSet extends FractalBase {

    // escape radius  # choose R > 0 such that R**2 - R >= sqrt(cx**2 + cy**2)
    float radius = 0f;
    float cx = 0f;
    float cy = 0f;


    @Override
    void setPrams() {
        radius = 1.5f;
        cx = 0.3f;
        cy = 0.3f;

        fromX = -0f;
        toX = 0.1f;
        fromY = -0.95f;
        colorOption = 4;

//        fromX = -2f;
//        toX = 2f;
//        fromY = -1.5f;
//        colorOption = 4;

//        fromX = -0f;
//        toX = 0.1f;
//        fromY = -0.95f;
//        colorOption = 4;

        gridFlag = false;
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

        minY = -1.4f;

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
