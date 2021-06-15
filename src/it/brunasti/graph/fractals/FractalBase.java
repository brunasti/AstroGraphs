package it.brunasti.graph.fractals;

import javax.swing.*;
import java.awt.*;

public class FractalBase extends JFrame {
    static final int FRAME_SIZE_X = 1200;
    static final int FRAME_SIZE_Y = 850;
    static final int MAX_ITERATION = 1000;

    float minX = -2.5f;
    float maxX = 1f;

    float minY = -1f;
    float maxY = 1f;

    boolean gridFlag = false;
    int gridSize = 400;

    Color[] colors = new Color[MAX_ITERATION+1];


    int colorOption = 1;

    float fromX = minX;
    float toX = maxX;

    float fromY = minY;
    float toY;

    void setPrams() {
        // Put the parameter set in this overwritten method
    }

    int computeIteration(int px, int py) {
        // Put the fractal calculation logic in this overwritten method
        var iteration = px + py;
        if (iteration > MAX_ITERATION) {
            iteration = MAX_ITERATION;
        }
        return iteration;
    }

    void drawSet(Graphics g) {
        var loopX = 0;
        for (var px=0; px<FRAME_SIZE_X; px++) {
            loopX ++;
            var loopY = 0;
            for (var py=0; py<FRAME_SIZE_Y; py++) {
                var iteration = computeIteration(px, py);

                g.setColor(pickColor(iteration));
                g.drawLine(px, py, px, py);

                loopY++;
                if ((loopY >= gridSize) && (loopX >= gridSize)) {
                    loopY = 0;
                    if (gridFlag) {
                        g.setColor(Color.blue);
                        g.drawLine(px, py, px, py);
                    }
                }
            }
            if (loopX >= gridSize) {
                loopX = 0;
            }
        }
    }


    void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawSet(g);
    }

    Color pickColor(int iteration) {
        return colors[iteration];
    }

    void setColorsOption1() {
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

    void setColorsOption2() {
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

    void setColorsOption3() {
        setColorsOption2();
        var l = colors.length;
        var tmp = new Color[l];
        for (var i=0; i< l; i++) {
            tmp[l-i-1] = colors[i];
        }
        colors = tmp;
    }

    void setColorsDefault() {
        for (var i=0; i< colors.length; i++) {
            var iter = i;
            if (iter > 255) {
                iter = 255;
            }
            colors[i] = new Color(iter,iter,iter);
        }
    }

    void setColors(int colorOption) {
        switch (colorOption) {
            case 1:
                setColorsOption1();
                break;
            case 2:
                setColorsOption2();
                break;
            case 3:
                setColorsOption3();
                break;
            default:
                setColorsDefault();
                break;
        }
    }

    void setup() {
        float r = (FRAME_SIZE_Y+0f)/FRAME_SIZE_X;

        toY = (r)*(toX - fromX)+ fromY;
        log("X "+ fromX +" ; "+ toX);
        log("Y "+ fromY +" ; "+ toY);
        log("  Frame ratio :"+r);

        if (fromX < minX) {
            log("  - Starting X out of scope");
        }
        if (toX > maxX) {
            log("  - Ending X out of scope");
        }
        if (fromY < minY) {
            log("  - Starting Y out of scope");
        }
        if (toY > maxY) {
            log("  - Ending Y out of scope");
        }

        if ((toX < minX) || (fromX > maxX)) {
            log("  - All X out of scope");
            System.exit(-1);
        }
        if ((toY < minY) || (fromY > maxY)) {
            log("  - All Y out of scope");
            System.exit(-1);
        }
    }

    public FractalBase(String title) throws HeadlessException {
        super(title);

        getContentPane().setBackground(Color.BLACK);
        setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public FractalBase() throws HeadlessException {
        this("Fractal Base");
    }




    static void log(String msg) {
        System.out.println(msg);
    }

    @Override
    public void paint(Graphics g) {
        setPrams();
        setup();
        setColors(colorOption);
        super.paint(g);
        draw(g);
        log("DONE");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FractalBase().setVisible(true);
            }
        });
    }

}
