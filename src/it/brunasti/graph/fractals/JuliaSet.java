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

    static final int FRAME_SIZE_X = 1200;
    static final int FRAME_SIZE_Y = 850;
    static final int MAX_ITERATION = 1000;

//    static final float MIN_X = -2.5f;
//    static final float MAX_X = 1f;
//
//    static final float MIN_Y = -1f;
//    static final float MAX_Y = 1f;

    static final boolean FLAG_GRID = false;
    static final int GRID_SIZE = 50;

    static final Color[] colors = new Color[MAX_ITERATION+1];


    static int colorOption = 1;

    static float fromX = -0.72f;
    static float toX = -0.7f;

    static float fromY = -0.28f;
    static float toY;


    static void setPrams() {
//        fromX = MIN_X;
//        toX = MAX_X;
//        fromY = MIN_Y;

        colorOption = 1;
    }


    Color pickColor(int iteration) {
        return colors[iteration];
    }

    static void setColorsOption1() {
        var r=0;
        var g=0;
        var b=0;
        for (var i=0; i< colors.length; i++) {
            g=0;
            b=0;
            r = i;
            if (r>255) {
                g = i - 255;
                r = 255;
                if (g > 255) {
                    b = g - 255;
                    g = 255;
                    if (b > 255) {
                        b = 255;
                    }
                }
            }
            colors[i] = new Color(r,g,b);
        }
    }

    static void setColorsOption2() {
        var r=0;
        var g=0;
        var b=0;
        for (var i=0; i< colors.length; i++) {
            g=0;
            b=0;
            r = i*2;
            if (i>122) {
                r = 255-r;
                if (r < 0) {
                    r = 0;
                }

                if (i > 255) {
                    g = (i-255) * 2;

                    if (i > 377) {
                        g = 255 - g;
                        if (g < 0) {
                            g = 0;
                        }
                    }

                    if (i > 560) {
                        b = (i-560) * 2;

                        if (i > 682) {
                            b = 255 - b;
                            if (b < 0) {
                                b = 0;
                            }
                        }


                    }

                }

            }
            colors[i] = new Color(r,g,b);
        }
    }

    static void setColorsDefault() {
        for (var i=0; i< colors.length; i++) {
            var iter = i;
            if (iter > 255) {
                iter = 255;
            }
            colors[i] = new Color(iter,iter,iter);
        }
    }

    static void setColors(int colorOption) {
        switch (colorOption) {
            case 1:
                setColorsOption1();
                break;
            case 2:
                setColorsOption2();
                break;
            default:
                setColorsDefault();
                break;
        }
    }

    static void setup() {
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

    public JuliaSet() throws HeadlessException {
        super("Mandelbrot");

        getContentPane().setBackground(Color.BLACK);
        setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }




//    private static void log(String msg) {
//        System.out.println(msg);
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        setPrams();
//        setup();
//        setColors(colorOption);
//        super.paint(g);
//        drawSet(g);
//        log("DONE");
//    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JuliaSet().setVisible(true);
            }
        });
    }

}
