package it.brunasti.graph;

import javax.swing.*;
import java.awt.*;

public class Mandelbrot extends JFrame {
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

    static final int FRAME_SIZE_X = 1200;
    static final int FRAME_SIZE_Y = 850;
    static final int MAX_ITERATION = 1000;

    static final float MIN_X = -2.5f;
    static final float MAX_X = 1f;

    static final float MIN_Y = -1f;
    static final float MAX_Y = 1f;

    static final float FROM_X = -1f;
    static final float TO_X = 0f;

    static final float FROM_Y = -0.5f;
    static float TO_Y = (FRAME_SIZE_Y/FRAME_SIZE_X)*(TO_X-FROM_X)+FROM_Y;

    static final boolean FLAG_GRID = true;
    static final int GRID_SIZE = 100;



    void setup() {
        float r = Float.valueOf(0f+(FRAME_SIZE_Y+0f)/FRAME_SIZE_X);
        TO_Y = (r)*(TO_X-FROM_X)+FROM_Y;
        log("  r:"+r);
        log("("+FRAME_SIZE_Y+"/"+FRAME_SIZE_X+")*("+TO_X+"-"+FROM_X+")+"+FROM_Y);
        log("X "+FROM_X+" ; "+TO_X);
        log("Y "+FROM_Y+" ; "+TO_Y);
    }

    void drawSet(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        long c = 0;
        int loop_x = 0;
        for (int px=0; px<FRAME_SIZE_X; px++) {
            loop_x ++;
            int loop_y = 0;
            for (int py=0; py<FRAME_SIZE_Y; py++) {
                int iteration = 0;
                float x = 0;
                float y = 0;
                float x0 = ((px * (TO_X - FROM_X)) / FRAME_SIZE_X) + FROM_X;
                float y0 = ((py * (TO_Y - FROM_Y)) / FRAME_SIZE_Y) + FROM_Y;

                while (((x*x + y*y) < 4) && (iteration < MAX_ITERATION)) {
                    float xtemp = x*x - y*y + x0;
                    y = 2*x*y + y0;
                    x = xtemp;
                    iteration = iteration + 1;
                }

                if (iteration > 255) {
                    iteration = 255;
                }
                g.setColor(new Color(iteration, iteration, iteration));
                g.drawLine(px, py, px, py);

                loop_y++;
                if ((loop_y >= GRID_SIZE) && (loop_x >= GRID_SIZE)) {
                    log("  x0:" + x0 + " y0:" + y0 + " iter:" + iteration);
                    loop_y = 0;
                    c++;
                    if (FLAG_GRID) {
                        g.setColor(Color.blue);
                        g.drawLine(px, py, px, py);
                    }
                }
            }
            if (loop_x >= GRID_SIZE) {
                log("  -------");
                loop_x = 0;
            }
        }
        log(" Total log : "+c);
    }

    public Mandelbrot() throws HeadlessException {
        super("Mandelbrot");

        getContentPane().setBackground(Color.BLACK);
        setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }




    private static void log(String msg) {
        System.out.println(msg);
    }

    @Override
    public void paint(Graphics g) {
        setup();
        super.paint(g);
        drawSet(g);
        log("DONE");
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
