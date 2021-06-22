package it.brunasti.graph.fractals.newtons;

public class Polynomial {
    double[] coefficients;

    public Polynomial(double[] coefficients){
        this.coefficients = coefficients;
    }

    public Polynomial derivative() {
        if(coefficients == null) {
            return null;
        }
        double[] newCoefficents = new double[coefficients.length - 1];

        for(int i = 0; i < newCoefficents.length; i++){
            newCoefficents[i] = coefficients[i + 1] * (i + 1);
        }

        return new Polynomial(newCoefficents);
    }

    public Complex evaluate(Complex z) {
        Complex result = new Complex();
        for(int i = 0; i < coefficients.length; i++) {
            if(i == 0) {
                result = result.add(coefficients[0]);
                continue;
            }
            if(coefficients[i] == 0) {
                continue;
            }
            result = result.add(z.pow(i).multiply(coefficients[i]));
        }
        return result;
    }
}
