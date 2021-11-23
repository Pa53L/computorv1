import solver.Parser;
import solver.Solver;

import java.util.Locale;

public class App {
    public static void main(String[] args) {
        if (args.length > 0) {
            Parser parsedEquation = new Parser(String.join(" ", args));
            System.out.println("Reduced form: " + parsedEquation.getReducedForm());
            System.out.println("Polynomial degree: " + parsedEquation.getMaxDegree());
            Solver solver = new Solver(parsedEquation);
            System.out.println(solver.getSolution());
        } else {
            System.err.println("Please, write a polynomial second or lower degree equation.");
        }
    }


}
