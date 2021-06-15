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


    @Override
    void setPrams() {
        fromX = minX;
        toX = maxX;
        fromY = minY;

        colorOption = 3;

        gridFlag = true;
        gridSize = 50;
    }

    @Override
    void drawSet(Graphics g) {
        var loopX = 0;
        for (var px=0; px<FRAME_SIZE_X; px++) {
            loopX ++;
            var loopY = 0;
            for (var py=0; py<FRAME_SIZE_Y; py++) {
                var iteration = 0;
                float x = 0;
                float y = 0;
                float x0 = ((px * (toX - fromX)) / FRAME_SIZE_X) + fromX;
                float y0 = ((py * (toY - fromY)) / FRAME_SIZE_Y) + fromY;

                while (((x*x + y*y) < 4) && (iteration < MAX_ITERATION)) {
                    float xtemp = x*x - y*y + x0;
                    y = 2*x*y + y0;
                    x = xtemp;
                    iteration = iteration + 1;
                }

                g.setColor(pickColor(iteration));
                g.drawLine(px, py, px, py);

                loopY++;
                if ((loopY >= gridSize) && (loopX >= gridSize)) {
                    log("  x0:" + x0 + " y0:" + y0 + " iter:" + iteration);
                    loopY = 0;
                    if (gridFlag) {
                        g.setColor(Color.blue);
                        g.drawLine(px, py, px, py);
                    }
                }
            }
            if (loopX >= gridSize) {
                log("  -------");
                loopX = 0;
            }
        }
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
