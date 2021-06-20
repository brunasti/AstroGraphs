package it.brunasti.graph.gravitational;

import java.awt.*;

public class Body {

    public double x = 0;
    public double y = 0;
    public double bx = 0;
    public double by = 0;
    public double vx = 0;
    public double vy = 0;

    public double sx = 0;
    public double sy = 0;

    public double m = 1;

    public Color c = Color.red;
    public String name;

    public int round = 0;

    public Body[] others = new Body[0];

}
