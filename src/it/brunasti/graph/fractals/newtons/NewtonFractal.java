package it.brunasti.graph.fractals.newtons;

import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.swing.JScrollBar;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewtonFractal extends SwingWorker<BufferedImage, Void> {


    static final double DEFAULT_ZOOM       = 100.0;
    static final double DEFAULT_TOP_LEFT_X = -4.5;
    static final double DEFAULT_TOP_LEFT_Y =  4.0;


    private static final double TOLERANCE = Math.pow(10, -6);
    private static final int MAXITER = 100;

    double zoomFactor = DEFAULT_ZOOM;
    double topLeftX   = DEFAULT_TOP_LEFT_X;
    double topLeftY   = DEFAULT_TOP_LEFT_Y;

    private double getXPos(double x) {
        return (x / zoomFactor) + topLeftX;
    }

    private double getYPos(double y) {
        return (y / zoomFactor) - topLeftY;
    }

    private int width;
    private int height;
    private Map<Point, RootPoint> roots;
    private ArrayList<Complex> rootColors;

    private Polynomial polinomial;


    public NewtonFractal(int width, int height, Polynomial polinomial){
        this.width = width;
        this.height = height;
        this.polinomial = polinomial;
    }

    private float clamp01(float value) {
        return Math.max(0, Math.min(1, value));
    }

    private Color getColorFromRoot(RootPoint rootPoint){
        for (int i = 0; i < rootColors.size(); i++) {
            if(rootColors.get(i).equals(rootPoint.getPoint(), TOLERANCE)){

                float hue;
                float saturation;
                float brightness;

                hue = clamp01(Math.abs((float)(0.5f - rootPoint.getPoint().arg() / (Math.PI * 2.0f))));

                saturation =  clamp01(Math.abs(0.59f / (float) rootPoint.getPoint().abs()));

                brightness = 0.95f * Math.max(1.0f - (float)rootPoint.getNumIter() * 0.025f, 0.05f);

                if (rootPoint.getPoint().abs() < 0.1) {
                    saturation = 0.0f;
                }

                return Color.getHSBColor(hue, saturation, brightness);
            }
        }
        return Color.black;
    }

    private void applyNewtonMethod(int x, int y){
        Complex point = new Complex(getXPos(x), getYPos(y));

        RootApproximator rtApprox = new RootApproximator(polinomial, point);

        RootPoint rootPoint = rtApprox.getRootPoint();

        if(!containsRoot(rootPoint.getPoint())){
            rootColors.add(rootPoint.getPoint());
        }
        roots.put(new Point(x,y), rootPoint);
    }

    private boolean containsRoot(Complex root){
        for(Complex z : rootColors){
            if(z.equals(root, TOLERANCE)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected BufferedImage doInBackground() {
        setProgress(0);
        BufferedImage fractalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        rootColors = new ArrayList<>();
        roots = new HashMap<>();

        int totalSteps = 0;
        int calculationSteps =  width * height;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if (isCancelled()) {
                    return fractalImage;
                }

                applyNewtonMethod(x, y);

                Color c = getColorFromRoot(roots.get(new Point(x, y)));
                fractalImage.setRGB(x, y, c.getRGB());

                super.setProgress(Math.round(100.0f * (totalSteps++) / calculationSteps));
            }
        }
        return fractalImage;
    }

    @Override
    public void done() {
        FractalViewer.setFieldsEnabled(true);

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                Rectangle bounds = FractalViewer.previewScrollPane.getViewport().getViewRect();

                JScrollBar horizontal = FractalViewer.previewScrollPane.getHorizontalScrollBar();
                horizontal.setValue((horizontal.getMaximum() - bounds.width) / 2);

                JScrollBar vertical = FractalViewer.previewScrollPane.getVerticalScrollBar();
                vertical.setValue((vertical.getMaximum() - bounds.height) / 2);
            }
        });
    }

    private class RootApproximator {

        private Polynomial pol;
        private Polynomial dpol;
        private Complex guess;

        public RootApproximator(Polynomial pol, Complex guess){
            this.pol = pol;
            this.dpol = pol.derivative();
            this.guess = guess;
        }

        private double nextGuess(){
            Complex nextGuess = guess.subtract(pol.evaluate(guess).divide(dpol.evaluate(guess)));
            double distance = nextGuess.euclideanDistance(guess);
            guess = nextGuess;
            return distance;
        }

        public RootPoint getRootPoint(){
            double diff = 10;
            int iter = 0;
            while(diff > TOLERANCE && iter < MAXITER){
                iter++;
                diff = nextGuess();
            }

            return new RootPoint(this.guess, iter);
        }
    }

    private class RootPoint {
        private Complex point;
        private int numIter;

        public RootPoint(Complex point, int numIter){
            this.point = point;
            this.numIter = numIter;
        }
        public Complex getPoint() {
            return this.point;
        }

        public int getNumIter() {
            return this.numIter;
        }

    }
}