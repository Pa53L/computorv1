import solver.Parser;
import solver.Solver;

public class Computorv1 {


    public static void main(String[] args) {
        if (args.length > 0) {
            String equation = String.join(" ", args);
            Parser parsedEquation = new Parser(equation);
            Solver solver = new Solver(parsedEquation);
            printResult(solver);
        } else {
            System.err.println("Please, write a polynomial second or lower degree equation.");
        }
    }

    private static void printResult(Solver solver) {
        System.out.println(solver.getReducedForm());
    }






}
