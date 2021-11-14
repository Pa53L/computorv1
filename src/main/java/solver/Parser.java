package solver;

public class Parser {
    private double[] coefficients;

    public double[] getCoefficients() {
        return coefficients;
    }

    private int maxDegree;

    public int getMaxDegree() {
        return maxDegree;
    }

    private String equation;

    public Parser(String equation) {
        this.equation = manageFreeFormEntrie(equation);
        String tmp = this.equation;
        this.maxDegree = defineDegree(tmp);
        this.coefficients = new double[this.maxDegree + 1];
        this.equation = this.equation.replaceAll("\\s+", "");
        defineCoefficients(this.equation, this.coefficients);
        correctDegree();
    }

    private static String manageFreeFormEntrie(String equation) {
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
        return checkConstantTerm(equation);
    }

    private static String checkConstantTerm(String equation) {
        int i;
        int min;
        String member;
        String rest = "";

        while (!equation.isEmpty()) {
            min = Integer.MAX_VALUE;
            i = equation.indexOf('-');
            if (i != -1) {
                min = i;
            }
            i = equation.indexOf('+');
            if (i != -1 && i < min)
                min = i;
            i = equation.indexOf('=');
            if (i != -1 && i < min)
                min = i;
            if (min != Integer.MAX_VALUE) {
                member = equation.substring(0, min);
            } else {
                member = equation;
                equation = "";
            }
            if (!member.contains("X") && !member.isEmpty() && !member.equals(" "))
                member += "* X^0 ";
            rest += member;
            if (min != 2147483647) {
                equation = equation.substring(min);
                rest += equation.substring(0, 1);
                equation = equation.substring(1);
            }
        }
        return rest;
    }


    // TODO разобраться и переписать defineDegree
    private int defineDegree(String equation) {
        String tmp = equation;
        int t;
        int degree = -1;

        while (!tmp.isEmpty()) {
            t = tmp.indexOf("^");
            tmp = tmp.substring(t + 1);
            t = tmp.indexOf(" ");
            int i;
            if (t == -1) {
                i = Integer.parseInt(tmp);
            } else {
                i = Integer.parseInt(tmp.substring(0, t));
            }
            if (i > degree) {
                degree = i;
            }
            if (t != -1)
                tmp = tmp.substring(t + 1);
            else
                break;
        }
        return degree;
    }

    private double[] defineCoefficients(String equation, double[] coefficients) {
        int t;
        double coef;
        int degree;
        int sign = 1;

        while (!equation.isEmpty()) {
            t = equation.indexOf("*");
            if (equation.indexOf("=") == 0) {
                sign = -1;
                equation = equation.substring(1);
                t--;
            }
            coef = sign * Double.parseDouble(equation.substring(0, t));
            equation = equation.substring(t + 3);
            t = 0;
            while (t != equation.length() && (equation.charAt(t) != '+') && (equation.charAt(t) != '=') && (equation.charAt(t) != '-')) {
                t++;
            }
            degree = Integer.parseInt(equation.substring(0, t));
            equation = equation.substring(t);
            coefficients[degree] += coef;
        }
        return coefficients;
    }

    private void correctDegree() {
        while (maxDegree != 0 && coefficients[maxDegree] == 0) {
            maxDegree--;
        }
    }

}
