package it.brunasti.graph.gravitational;

import java.awt.*;

public class Body {

    public double positionX = 0;
    public double positionY = 0;
    public double prePositionX = 0;
    public double prePositionY = 0;
    public double velocityX = 0;
    public double velocityY = 0;

    public double mass = 1;

    public Color color = Color.red;
    public String name;

    public int round = 0;

    public Body[] otherBodies = new Body[0];
}
