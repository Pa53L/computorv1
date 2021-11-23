import solver.Parser;
import solver.Solver;

import java.util.Locale;

public class App {
    public static void main(String[] args) {
        if (args.length > 0) {
            String equation = String.join(" ", args);
            equation = freeForm(equation);
            equation = equation.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
            Parser parsedEquation = new Parser(equation);
            System.out.println("Reduced form: " + parsedEquation.getReducedForm());
            System.out.println("Polynomial degree: " + parsedEquation.getMaxDegree());
            Solver solver = new Solver(parsedEquation);
            System.out.println(solver.getSolution());
        } else {
            System.err.println("Please, write a polynomial second or lower degree equation.");
        }
    }

    private static String freeForm(String equation) {
        if (equation.startsWith("X"))
            equation = "1 * " + equation;
        if (equation.endsWith("X"))
            equation += "^1";
        equation = equation.replaceAll("= X", "= 1 * X");
        equation = equation.replaceAll("\\+ X", "+ 1 * X");
        equation = equation.replaceAll("- X", "- 1 * X");
        equation = equation.replaceAll("-X", "- 1 * X");
        equation = equation.replaceAll("X \\+", "X^1 +");
        equation = equation.replaceAll("X -", "X^1 -");
        equation = equation.replaceAll("X-", "X^1 -");
        equation = equation.replaceAll("X =", "X^1 =");
        return equation;
    }
}
