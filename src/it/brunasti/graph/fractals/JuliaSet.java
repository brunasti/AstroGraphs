package it.brunasti.graph.fractals;

import javax.swing.*;
import java.awt.*;

public class JuliaSet extends FractalBase {
/**
 R = escape radius  # choose R > 0 such that R**2 - R >= sqrt(cx**2 + cy**2)

 for each pixel (x, y) on the screen, do:
 {
 zx = scaled x coordinate of pixel # (scale to be between -R and R)
 # zx represents the real part of z.
 zy = scaled y coordinate of pixel # (scale to be between -R and R)
 # zy represents the imaginary part of z.

 iteration = 0
 max_iteration = 1000

 while (zx * zx + zy * zy < R**2  AND  iteration < max_iteration)
 {
 xtemp = zx * zx - zy * zy
 zy = 2 * zx * zy  + cy
 zx = xtemp + cx

 iteration = iteration + 1
 }

 if (iteration == max_iteration)
 return black;
 else
 return iteration;
 }
 */

    // escape radius  # choose R > 0 such that R**2 - R >= sqrt(cx**2 + cy**2)
    float r = 4f;
    float cx = 1f;
    float cy = 0f;


    @Override
    void setPrams() {
        r = 4f;
        cx = 0.3f;
        cy = 0.1f;

        fromX = minX;
        toX = maxX;
        fromY = minY;

        colorOption = 0;

        gridFlag = true;
        gridSize = 20;
    }


    /*
zx = scaled x coordinate of pixel # (scale to be between -R and R)
 # zx represents the real part of z.
 zy = scaled y coordinate of pixel # (scale to be between -R and R)
 # zy represents the imaginary part of z.

 iteration = 0
 max_iteration = 1000

 while (zx * zx + zy * zy < R**2  AND  iteration < max_iteration)
 {
 xtemp = zx * zx - zy * zy
 zy = 2 * zx * zy  + cy
 zx = xtemp + cx

 iteration = iteration + 1
 }

 if (iteration == max_iteration)
 return black;
 else
 return iteration;
     */
    @Override
    int computeIteration(int px, int py) {
        var iteration = 0;

        float x = 0;
        float y = 0;
        float zx = ((px * (toX - fromX)) / FRAME_SIZE_X) + fromX;
        float zy = ((py * (toY - fromY)) / FRAME_SIZE_Y) + fromY;

        while (((zx*zx + zy*zy) < r*r) && (iteration < MAX_ITERATION)) {
            float xtemp = zx*zx - zy*zy;
            zy = 2*zx*zy + cy;
            zx = xtemp + cx;
            iteration = iteration + 1;
        }
        return iteration;
    }

    public JuliaSet() throws HeadlessException {
        super("Julia Set");
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
