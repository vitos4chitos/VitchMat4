package helpers;

public class Functions {

    private double a1, b1, a3, b3, a4, b4, a5, b5, a00, a01, a02;

    public void setLinear(double a, double b){
        a1 = a;
        b1 = b;
    }

    public void setPolynomial(double a0, double a1, double a2){
        a00 = a0;
        a01 = a1;
        a02 = a2;
    }

    public void setExponential(double a, double b){
        a3 = a;
        b3 = b;
    }

    public void setPowerLaw(double a, double b){
        a4 = a;
        b4 = b;
    }

    public void setLogarithmic(double a, double b){
        a5 = a;
        b5 = b;
    }

    public double getLinear(double x){
        return (a1 * x + b1);
    }

    public double getPolynomial(double x){
        return a02 * Math.pow(x, 2) + a01 * x + a00;
    }

    public double getExponential(double x){
        return a3 * Math.exp(b3 * x);
    }

    public double getPowerLaw(double x){
        return a4 * Math.pow(x, b4);
    }

    public double getLogarithmic(double x){
        return a5 * Math.log(x) + b5;
    }

    public double getValue(int n, double x){
        switch (n){
            case 1:
                return getLinear(x);
            case 2:
                return getPolynomial(x);
            case 3:
                return getExponential(x);
            case 4:
                return getPowerLaw(x);
            case 5:
                return getLogarithmic(x);
            default:
                return 0;
        }
    }
}
