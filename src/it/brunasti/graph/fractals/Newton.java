package it.brunasti.graph.fractals;

import javax.swing.*;
import java.awt.*;

public class Newton extends FractalBase {
/**

 //z^3-1
 float2 Function (float2 z)
 {
     return cpow(z, 3) - float2(1, 0); //cpow is an exponential function for complex numbers
 }

 //3*z^2
 float2 Derivative (float2 z)
 {
     return 3 * cmul(z, z); //cmul is a function that handles multiplication of complex numbers
 }

 It is now just a matter of implementing the Newton method using the given functions.

 float2 roots[3] = //Roots (solutions) of the polynomial
 {
     float2(1, 0),
     float2(-.5, sqrt(3)/2),
     float2(-.5, -sqrt(3)/2)
 };

 color colors[3] =  //Assign a color for each root
 {
     red,
     green,
     blue
 }

 For each pixel (x, y) on the target, do:
 {
     zx = scaled x coordinate of pixel (scaled to lie in the Mandelbrot X scale (-2.5, 1))
     zy = scaled y coordinate of pixel (scaled to lie in the Mandelbrot Y scale (-1, 1))

     float2 z = float2(zx, zy); //Z is originally set to the pixel coordinates

     for (int iteration = 0;  iteration < maxIteration;  iteration++;)
     {
         z -= cdiv(Function(z), Derivative(z)); //cdiv is a function for dividing complex numbers

         float tolerance = 0.000001;

         for (int i = 0; i < roots.Length; i++)
         {
             float2 difference = z - roots[i];

             //If the current iteration is close enough to a root, color the pixel.
             if (abs(difference.x) < tolerance && abs (difference.y) < tolerance)
             {
                 return colors[i]; //Return the color corresponding to the root
             }
         }
     }
     return black; //If no solution is found
 }

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

    public Newton() throws HeadlessException {
        super("Mandelbrot");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Newton().setVisible(true);
            }
        });
    }

}
