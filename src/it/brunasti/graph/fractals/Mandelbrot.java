package it.brunasti.graph.fractals;

import javax.swing.*;
import java.awt.*;

public class Mandelbrot extends FractalBase {
/**
for each pixel (Px, Py) on the screen do
    x0 := scaled x coordinate of pixel (scaled to lie in the Mandelbrot X scale (-2.5, 1))
    y0 := scaled y coordinate of pixel (scaled to lie in the Mandelbrot Y scale (-1, 1))
    x := 0.0
    y := 0.0
    iteration := 0
    max_iteration := 1000
    while (x*x + y*y ≤ 2*2 AND iteration < max_iteration) do
        xtemp := x*x - y*y + x0
        y := 2*x*y + y0
        x := xtemp
        iteration := iteration + 1

    color := palette[iteration]
    plot(Px, Py, color)
 */


    @Override
    void setPrams() {
        fromX = -0.53f;
        toX = -0.528f;
        fromY = 0.5f;

        colorOption = 3;
    }

    @Override
    int computeIteration(int px, int py) {
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
        return iteration;
    }

    public Mandelbrot() throws HeadlessException {
        super("Mandelbrot");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Mandelbrot().setVisible(true);
            }
        });
    }

}
