package it.brunasti.graph.gravitational;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class AbstractGraphMain extends JFrame {


    public int CENTER_X = 500;
    public int CENTER_Y = 400;

    GraphUtils graphUtils;


    public AbstractGraphMain(String title) {
        super(title);
        create();
    }

    public AbstractGraphMain(String title, int center_x, int center_y) {
        super(title);
        CENTER_X = center_x;
        CENTER_Y = center_y;
        create();
    }

    private void create() {
        getContentPane().setBackground(Color.BLACK);
        setSize(CENTER_X *2, CENTER_Y *2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        graphUtils = new GraphUtils(CENTER_X, CENTER_Y);
    }

    Graphics2D drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.white);

        g2d.draw(new Rectangle2D.Float(5, 25, CENTER_X * 2 - 15, CENTER_Y * 2 - 30));

        g.drawLine(5, CENTER_Y, (CENTER_X * 2)-10, CENTER_Y);
        g.drawLine(CENTER_X, 25, CENTER_X, (CENTER_Y * 2) - 5);
        return g2d;
    }

    protected static void log(String msg) {
        System.out.println(msg);
    }

}
