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
        fromX = -0.7f;
        toX = 0f;
        fromY = 0.5f;

        colorOption = 1;
    }

    void setup() {
        float r = (FRAME_SIZE_Y+0f)/FRAME_SIZE_X;

        toY = (r)*(toX - fromX)+ fromY;
        log("X "+ fromX +" ; "+ toX);
        log("Y "+ fromY +" ; "+ toY);
        log("  Frame ratio :"+r);

        if (fromX < MIN_X) {
            log("  - Starting X out of scope");
        }
        if (toX > MAX_X) {
            log("  - Ending X out of scope");
        }
        if (fromY < MIN_Y) {
            log("  - Starting Y out of scope");
        }
        if (toY > MAX_Y) {
            log("  - Ending Y out of scope");
        }

        if ((toX < MIN_X) || (fromX > MAX_X)) {
            log("  - All X out of scope");
            System.exit(-1);
        }
        if ((toY < MIN_Y) || (fromY > MAX_Y)) {
            log("  - All Y out of scope");
            System.exit(-1);
        }
    }

    @Override
    void drawSet(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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
                if ((loopY >= GRID_SIZE) && (loopX >= GRID_SIZE)) {
                    log("  x0:" + x0 + " y0:" + y0 + " iter:" + iteration);
                    loopY = 0;
                    if (FLAG_GRID) {
                        g.setColor(Color.blue);
                        g.drawLine(px, py, px, py);
                    }
                }
            }
            if (loopX >= GRID_SIZE) {
                log("  -------");
                loopX = 0;
            }
        }
    }

    public Mandelbrot() throws HeadlessException {
        super("Mandelbrot");

        getContentPane().setBackground(Color.BLACK);
        setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
