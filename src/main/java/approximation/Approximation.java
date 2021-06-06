package approximation;

import helpers.Functions;
import helpers.GaussianElimination;
import io.ChartDrawer;
import io.Output;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Stream;


public class Approximation {

    private ArrayList<Pair<Double, Double>> listOfValue;
    private int n;
    private Functions functions;
    private Output output;

    public Approximation(ArrayList<Pair<Double, Double>> listOfValue, Output output){
        n = listOfValue.size();
        this.listOfValue = listOfValue;
        functions = new Functions();
        this.output = output;
    }

    public void solve(){
        double s1, s2, s3, s4, s5, smin;
        s1 = linear();
        s2 = polynomial();
        s3 = exponential();
        s4 = powerLaw();
        s5 = logarithmic();
        double r1, r2, r3, r4, r5, rmin;
        r1 = Math.sqrt(s1 / n);
        r2 = Math.sqrt(s2 / n);
        r3 = Math.sqrt(s3 / n);
        r4 = Math.sqrt(s4 / n);
        r5 = Math.sqrt(s5 / n);
        rmin = Math.min(r1, Math.min(r2, Math.min(r3, Math.min(r4, r5))));
        smin = Math.min(s1, Math.min(s2, Math.min(s3, Math.min(s4, s5))));
        if(Double.compare(r1, rmin) == 0){
            System.out.println("Лучшее решение: Linear");
            output.printBest(functions, listOfValue, 1);
            ChartDrawer drawer = new ChartDrawer(listOfValue, functions, 1);
        }
        else if (Double.compare(r2, rmin) == 0) {
            System.out.println("Лучшее решение: Polynomial");
            output.printBest(functions, listOfValue, 2);
            ChartDrawer drawer = new ChartDrawer(listOfValue, functions, 2);
        }
        else if (Double.compare(r3, rmin) == 0) {
            System.out.println("Лучшее решение: Exponential");
            output.printBest(functions, listOfValue, 3);
            ChartDrawer drawer = new ChartDrawer(listOfValue, functions, 3);
        }
        else if (Double.compare(r4, rmin) == 0) {
            System.out.println("Лучшее решение: Power Law");
            output.printBest(functions, listOfValue, 4);
            ChartDrawer drawer = new ChartDrawer(listOfValue, functions, 4);
        }
        else if (Double.compare(r5, rmin) == 0) {
            System.out.println("Лучшее решение: Logarithmic");
            output.printBest(functions, listOfValue, 5);
            ChartDrawer drawer = new ChartDrawer(listOfValue, functions, 5);
        }
    }

    private double linear(){
        double sx = 0;
        double sxx = 0;
        double sy = 0;
        double sxy = 0;
        for(int  i = 0; i < n; i++){
            sx += listOfValue.get(i).getKey();
            sxx += Math.pow(listOfValue.get(i).getKey(), 2);
            sy += listOfValue.get(i).getValue();
            sxy += listOfValue.get(i).getKey() * listOfValue.get(i).getValue();
        }
        double sry = sy / n;
        double srx = sx / n;
        double a = (sxy * n - sx * sy) / (sxx * n - sx * sx);
        double b = (sxx * sy - sx * sxy) / (sxx * n - sx * sx);
        functions.setLinear(a, b);
        double s = 0;
        double fi = 0;
        double fifi = 0;
        double xy = 0, xx = 0, yy = 0;
        for(int  i = 0; i < n; i++){
            s += Math.pow(listOfValue.get(i).getValue() - functions.getLinear(listOfValue.get(i).getKey()), 2);
            fi += functions.getLinear(listOfValue.get(i).getKey());
            fifi += Math.pow(functions.getLinear(listOfValue.get(i).getKey()), 2);
            xx += Math.pow(srx - listOfValue.get(i).getKey(), 2);
            yy += Math.pow(sry - listOfValue.get(i).getValue(), 2);
            xy += (srx - listOfValue.get(i).getKey()) * (sry - listOfValue.get(i).getValue());
        }
        double sko = Math.sqrt(s / n);
        double rr = 1 - (s / (fifi - (1d/n)*Math.pow(fi, 2)));
        double r = xy / Math.sqrt(xx*yy);
        output.print(a +"*x + "+ b, sko, rr, r, "Linear");
        return s;
    }

    private double polynomial(){
        double[][] A = new double[3][3];
        double[] b = new double[3];
        A[0][0] = n;
        double sumX = 0;
        double sumXSq = 0;
        double sumXCb = 0;
        double sumXFourth = 0;
        double sumY = 0;
        double sumYX = 0;
        double sumYXSq = 0;
        for (int i = 0; i < n; i++) {
            sumX += listOfValue.get(i).getKey();
            sumXSq += Math.pow(listOfValue.get(i).getKey(), 2);
            sumXCb += Math.pow(listOfValue.get(i).getKey(), 3);
            sumXFourth += Math.pow(listOfValue.get(i).getKey(), 4);
            sumY += listOfValue.get(i).getValue();
            sumYX += listOfValue.get(i).getKey() * listOfValue.get(i).getValue();
            sumYXSq += Math.pow(listOfValue.get(i).getKey(), 2) * listOfValue.get(i).getValue();
        }
        A[0][1] = A[1][0] = sumX;
        A[0][2] = A[1][1] = A[2][0] = sumXSq;
        A[1][2] = A[2][1] = sumXCb;
        A[2][2] = sumXFourth;
        b[0] = sumY;
        b[1] = sumYX;
        b[2] = sumYXSq;
        double[] solution = GaussianElimination.lsolve(A, b);
        double a0 = solution[0];
        double a1 = solution[1];
        double a2 = solution[2];
        functions.setPolynomial(a0, a1, a2);
        double s = 0;
        double fi = 0;
        double fifi = 0;
        for(int  i = 0; i < n; i++){
            s += Math.pow(listOfValue.get(i).getValue() - functions.getPolynomial(listOfValue.get(i).getKey()), 2);
            fi += functions.getPolynomial(listOfValue.get(i).getKey());
            fifi += Math.pow(functions.getPolynomial(listOfValue.get(i).getKey()), 2);
        }
        double r = Math.sqrt(s / n);
        double rr = 1 - (s / (fifi - (1d/n)*Math.pow(fi, 2)));
        output.print(a2 +"*x^2 + "+ a1 + "*x + " + a0, r, rr, Double.NaN, "Polynomial");
        return s;
    }

    private double exponential(){
        double sx = 0;
        double slny = 0;
        double sxx = 0;
        double sxlny = 0;
        double sy = 0;
        for(int  i = 0; i < n; i++){
            sx += listOfValue.get(i).getKey();
            sxx += Math.pow(listOfValue.get(i).getKey(), 2);
            slny += Math.log(listOfValue.get(i).getValue());
            sxlny += listOfValue.get(i).getKey() * Math.log(listOfValue.get(i).getValue());
            sy += listOfValue.get(i).getValue();
        }
        double sry = sy / n;
        double srx = sx / n;
        double lna = (slny * sxx - sxlny * sx) / (sxx * n - sx * sx);
        double b = (n * sxlny - slny * sx) / (sxx * n - sx * sx);
        lna = Math.exp(lna);
        functions.setExponential(lna, b);
        double s = 0;
        double fi = 0;
        double fifi = 0;
        double xy = 0, xx = 0, yy = 0;
        for(int  i = 0; i < n; i++){
            s += Math.pow(listOfValue.get(i).getValue() - functions.getExponential(listOfValue.get(i).getKey()),2);
            fi += functions.getExponential(listOfValue.get(i).getKey());
            fifi += Math.pow(functions.getExponential(listOfValue.get(i).getKey()), 2);
            xx += Math.pow(srx - listOfValue.get(i).getKey(), 2);
            yy += Math.pow(sry - listOfValue.get(i).getValue(), 2);
            xy += (srx - listOfValue.get(i).getKey()) * (sry - listOfValue.get(i).getValue());
        }
        double sko = Math.sqrt(s / n);
        double rr = 1 - (s / (fifi - (1d/n)*Math.pow(fi, 2)));
        double r = xy / Math.sqrt(xx*yy);
        output.print(lna +"*e^("+ b + "*x)", sko, rr, r, "Exponential");
        return s;
    }

    private double powerLaw(){
        double slnxlnx = 0;
        double slny = 0;
        double slnx = 0;
        double slnxlny = 0;
        double sx = 0;
        double sy = 0;
        for(int  i = 0; i < n; i++){
            slnx += Math.log(listOfValue.get(i).getKey());
            slnxlnx += Math.pow(Math.log(listOfValue.get(i).getKey()), 2);
            slny += Math.log(listOfValue.get(i).getValue());
            slnxlny += Math.log(listOfValue.get(i).getKey()) * Math.log(listOfValue.get(i).getValue());
            sx += listOfValue.get(i).getKey();
            sy += listOfValue.get(i).getValue();
        }
        double sry = sy / n;
        double srx = sx / n;
        double lna = (slny * slnxlnx - slnxlny * slnx) / (slnxlnx * n - slnx * slnx);
        double b = (n * slnxlny - slny * slnx) / (slnxlnx * n - slnx * slnx);
        lna = Math.exp(lna);
        functions.setPowerLaw(lna, b);
        double s = 0;
        double fi = 0;
        double fifi = 0;
        double xy = 0, xx = 0, yy = 0;
        for(int  i = 0; i < n; i++){
            s += Math.pow(listOfValue.get(i).getValue() - functions.getPowerLaw(listOfValue.get(i).getKey()), 2);
            fi += functions.getPowerLaw(listOfValue.get(i).getKey());
            fifi += Math.pow(functions.getPowerLaw(listOfValue.get(i).getKey()), 2);
            xx += Math.pow(srx - listOfValue.get(i).getKey(), 2);
            yy += Math.pow(sry - listOfValue.get(i).getValue(), 2);
            xy += (srx - listOfValue.get(i).getKey()) * (sry - listOfValue.get(i).getValue());
        }
        double sko = Math.sqrt(s / n);
        double rr = 1 - (s / (fifi - (1d/n)*Math.pow(fi, 2)));
        double r = xy / Math.sqrt(xx*yy);
        output.print(lna +"*x^("+ b + ")", sko, rr, r, "Power Law");
        return s;
    }

    private double logarithmic(){
        double slnxlnx = 0;
        double sy = 0;
        double slnx = 0;
        double slnxy = 0;
        double sx = 0;
        for(int  i = 0; i < n; i++){
            slnx += Math.log(listOfValue.get(i).getKey());
            slnxlnx += Math.pow(Math.log(listOfValue.get(i).getKey()), 2);
            sy += listOfValue.get(i).getValue();
            slnxy += Math.log(listOfValue.get(i).getKey()) * listOfValue.get(i).getValue();
            sx += listOfValue.get(i).getKey();
        }
        double sry = sy / n;
        double srx = sx / n;
        double a = (slnxy * n - slnx * sy) / (slnxlnx * n - slnx * slnx);
        double b = (slnxlnx * sy - slnx * slnxy) / (slnxlnx * n - slnx * slnx);
        functions.setLogarithmic(a, b);
        double s = 0;
        double fi = 0;
        double fifi = 0;
        double xy = 0, xx = 0, yy = 0;
        for(int  i = 0; i < n; i++){
            s += Math.pow(listOfValue.get(i).getValue() - functions.getLogarithmic(listOfValue.get(i).getKey()), 2);
            fi += functions.getLogarithmic(listOfValue.get(i).getKey());
            fifi += Math.pow(functions.getLogarithmic(listOfValue.get(i).getKey()), 2);
            xx += Math.pow(srx - listOfValue.get(i).getKey(), 2);
            yy += Math.pow(sry - listOfValue.get(i).getValue(), 2);
            xy += (srx - listOfValue.get(i).getKey()) * (sry - listOfValue.get(i).getValue());
        }
        double sko = Math.sqrt(s / n);
        double rr = 1 - (s / (fifi - (1d/n)*Math.pow(fi, 2)));
        double r = xy / Math.sqrt(xx*yy);
        output.print(a +"*ln(x) + "+ b, sko, rr, r, "Logarithmic");
        return s;
    }
}
