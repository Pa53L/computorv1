package solver;

public class Solver {
    private final Parser parser;
    private final String reducedForm;

    public String getReducedForm() {
        return reducedForm;
    }

    public Solver(Parser parsedEquation) {
        this.parser = parsedEquation;
        reducedForm = reducedFormAndDegree();
//        System.out.println(reducedForm);
        calculateRoots();
    }

    private String reducedFormAndDegree(){
        StringBuffer sb = new StringBuffer();
        int i = 0;
        int maxDegree = parser.getMaxDegree();
        double[] coefficients = parser.getCoefficients();
        sb.append("Reduced form: ").append("\n");
        while (maxDegree >= i) {
            if (coefficients[i] < 0)
                sb.append("- ");
            else if (coefficients[i] > 0 && i > 0 && sum(i, coefficients))
                sb.append("+ ");
            if (coefficients[i] < 0 && coefficients[i] != (int) coefficients[i])
                sb.append(-coefficients[i]).append(" ");
            else if ((coefficients[i] > 0 && coefficients[i] != (int) coefficients[i]))
                sb.append(coefficients[i]).append(" ");
            else if ((coefficients[i] < 0 && coefficients[i] == (int) coefficients[i]))
                sb.append((int) -coefficients[i]).append(" ");
            else if ((coefficients[i] > 0 && coefficients[i] == (int) coefficients[i]))
                sb.append((int) coefficients[i]).append(" ");
            if (maxDegree == 0 && coefficients[maxDegree] != 0)
                sb.append("* X^0 ");
            if (coefficients[i] != 0 && i == 1 )
                sb.append("* X ");
            if (coefficients[i] != 0 && i > 1)
                sb.append("* X^").append(i).append(" ");
            i++;
        }
        if (maxDegree == 0 && coefficients[maxDegree] == 0) {
            sb.append((int) coefficients[maxDegree]).append(" * X^").append(maxDegree).append(" ");
        }
        sb.append("= 0").append("\n");
        sb.append("Polynomial degree: ").append(maxDegree).append("\n");
        return sb.toString();
    }

    private static boolean sum(int i, double[] coefficients){
        int k = 0;
        int sum = 0;
        while(k < i){
            sum += coefficients[k];
            k++;
        }
        return sum != 0;
    }

    private void calculateRoots() {
        int maxDegree = parser.getMaxDegree();
        double[] coefficient = parser.getCoefficients();
        switch (maxDegree) {
            case 0:
                if ((coefficient[maxDegree] == 0)) {
                    System.out.println("Each real number is a solution ...");
                } else {
                    System.out.println("No solution");
                }
                break;
            case 1:
                System.out.format("The solution is: %.2f\n", (-coefficient[0]/coefficient[1]));
                break;
            case 2:
                solveEquation();
                break;
            default:
                System.out.println("The polynomial degree is strictly greater than 2, I can't solve.");
        }
    }

    private void solveEquation(){
        double[] coefficient = parser.getCoefficients();
        double determinant;
        determinant = coefficient[1] * coefficient[1] - 4 * coefficient[2] * coefficient[0];

        if (determinant > 0) {
            System.out.println("Discriminant is strictly positive, the two solutions are:");
            System.out.format("%.6f\n", ((-coefficient[1]) + sqrt(determinant)) / (2 * coefficient[2]));
            System.out.format("%.6f\n", ((-coefficient[1]) - sqrt(determinant)) / (2 * coefficient[2]));
        } else if (determinant == 0) {
            System.out.println("Discriminant is equal to zero, the solution is:");
            System.out.format("%.6f\n", (-coefficient[1]) / (2 * coefficient[2]));
        }
        else {
            System.out.println("Determinant is less than zero, the solution are complex number and distinct:");
            double real = (-coefficient[1])/ (2 * coefficient[2]);
            double imaginary = sqrt(-determinant) / (2 * coefficient[2]);
            System.out.format("%.6f+%.6fi\n", real, imaginary);
            System.out.format("%.6f-%.6fi\n", real, imaginary);
        }
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
}
