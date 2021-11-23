package solver;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Solver {
    private String solution = "";
    private final int maxDegree;
    double a;
    double b;
    double c;

    public Solver(Parser parsedEquation) {
        this.a = parsedEquation.getA();
        this.b = parsedEquation.getB();
        this.c = parsedEquation.getC();
        this.maxDegree = parsedEquation.getMaxDegree();
        calculateRoots();
    }

    private void calculateRoots() {
        switch (maxDegree) {
            case 0:
                if (a == 0) {
                    this.solution = "Each real number is a solution ...";
                } else {
                    this.solution = "No solution";
                }
                break;
            case 1:
                this.solution = String.format("The solution is: %.2f\n", (-c/b));
                break;
            case 2:
                solveEquation();
                break;
            default:
                this.solution = "The polynomial degree is strictly greater than 2, I can't solve.";
        }
    }

    private void solveEquation(){
        double determinant;
        determinant = b * b - 4 * a * c;

        if (determinant > 0) {
            this.solution += "Discriminant is strictly positive, the two solutions are:\n";
            double positiveRoot = ((-b) - sqrt(determinant)) / (2 * a);
            double negativeRoot = ((-b) + sqrt(determinant)) / (2 * a);
            int positivePlaces = findPlaces(positiveRoot);
            int negativePlaces = findPlaces(negativeRoot);
            positiveRoot = withBigDecimal(positiveRoot, positivePlaces);
            negativeRoot = withBigDecimal(negativeRoot, negativePlaces);
            this.solution += String.format("%f\n", positiveRoot);
            this.solution += String.format("%f\n", negativeRoot);
        } else if (determinant == 0) {
            double root = (-b) / (2 * a);
            int singleRootPlaces = findPlaces(root);
            root = withBigDecimal(root, singleRootPlaces);
            this.solution = "Discriminant is equal to zero, the solution is:\n";
            this.solution += String.format("%f\n", root);
        } else {
            this.solution = "Determinant is less than zero, the solution are complex number and distinct:";
            double real = (-b)/ (2 * a);
            double imaginary = sqrt(-determinant) / (2 * a);
            this.solution += String.format("%.6f+%.6fi\n", real, imaginary);
            this.solution += String.format("%.6f-%.6fi\n", real, imaginary);
        }
    }

    private int findPlaces(double d) {
        double rounding = 0.1;
        if (d < 0.0) {
            d *= -1;
        }
        for (int i = 0; i < 5; i++) {
            if (d - (int) d < rounding) {
                return i;
            } else {
                rounding /= 10;
            }
        }
        return 6;
    }

    private double withBigDecimal(double value, int places) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private double sqrt(double number) {
        double t;
        double squareRoot = number / 2;
        do {
            t = squareRoot;
            squareRoot = (t + (number / t)) / 2;
        } while ((t - squareRoot) != 0);
        return squareRoot;
    }

    public String getSolution() {
        return solution;
    }
}
