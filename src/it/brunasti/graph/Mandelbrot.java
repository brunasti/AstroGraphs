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

    void setup() {
    }

    void drawSet(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (int px=0; px<FRAME_SIZE_X; px++) {
            for (int py=0; py<FRAME_SIZE_Y; py++) {
                int iteration = 0;
                float x = 0;
                float y = 0;
                float x0 = ((px * 3.5f) / FRAME_SIZE_X) - 2.5f;
                float y0 = ((py * 2f) / FRAME_SIZE_Y) - 1f;

                while (((x*x + y*y) < 4) && (iteration < MAX_ITERATION)) {
                    float xtemp = x*x - y*y + x0;
                    y = 2*x*y + y0;
                    x = xtemp;
                    iteration = iteration + 1;
                }

                log("  px:"+px+" py:"+py+" iter:"+iteration);
                if (iteration > 255) {
                    iteration = 255;
                }
                g.setColor(new Color(iteration, iteration, iteration));
                g.drawLine(px, py, px, py);
            }
        }
//        g.setColor(Color.white);
//
//        g2d.draw(new Rectangle2D.Float(5, 25, (CX * 2f) - 15, (CY * 2f) - 30));
//
//        g.drawLine(5, CY, (CX * 2)-10, CY);
//        g.drawLine(CX, 25, CX, (CY * 2) - 5);
//
//        Shape theCircle = new Ellipse2D.Double(CX - 200, CY - 200, 2.0 * 200, 2.0 * 200);
//        g2d.draw(theCircle);
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
