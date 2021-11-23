package solver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Map<String, Double> lexemes;
    private final String reducedForm;
    private final int maxDegree;
    double a = 0.0;
    double b = 0.0;
    double c = 0.0;

    public Parser(String equation) {
        this.lexemes = regexParser(equation);
        this.reducedForm = reduce();
        this.maxDegree = findMaxDegree();
        defineCoefficients();
    }

    private Map<String, Double> regexParser(String str) {
        Map<String, Double> lexemes = new LinkedHashMap<>();
        int sign = 1;
        String[] splitInput = str.split("=");
        if (splitInput.length != 2) {
            throw new IllegalArgumentException("Incorrect input. Please, write a polynomial second or lower degree equation.");
        }
        Pattern pattern = Pattern.compile("([+-])?(\\d+(\\.)?(\\d+)?)?((\\*)?([Xx]\\^?\\d?))?");

        for (int i = 0; i < splitInput.length; i++) {
            if (i == 1) {
                sign = -1;
            }
            Matcher matcher = pattern.matcher(splitInput[i]);
            while (matcher.find()) {
                String tmp = matcher.group();
                String[] potentialLexeme = tmp.split("\\*");
                if (potentialLexeme.length == 2) {
                    if (lexemes.containsKey(potentialLexeme[1])) {
                        lexemes.put(potentialLexeme[1], lexemes.get(potentialLexeme[1]) + sign * Double.parseDouble(potentialLexeme[0]));
                    } else {
                        lexemes.put(potentialLexeme[1], sign * Double.parseDouble(potentialLexeme[0]));
                    }
                } else {
                    if (!tmp.isEmpty() && !tmp.isBlank()) {
                        if (lexemes.containsKey("freeMember")) {
                            lexemes.put("freeMember", lexemes.get("freeMember") + sign * Double.parseDouble(tmp));
                        } else {
                            lexemes.put("freeMember", sign * Double.parseDouble(tmp));
                        }
                    }
                }
            }
        }
        return lexemes;
    }

    private String reduce() {
        StringBuilder sb = new StringBuilder("");
        for (Map.Entry<String, Double> entry : lexemes.entrySet()) {
            if (entry.getKey().equals("freeMember")) {
                addCoefficient(sb, entry);
            } else if (entry.getKey().startsWith("X")) {
                addCoefficient(sb, entry);
                sb.append(" * ");
                sb.append(entry.getKey());
            }
        }

        return sb.append(" = 0").toString();
    }

    private void addCoefficient(StringBuilder sb, Map.Entry<String, Double> entry) {
        String value = getStringCoef(entry.getValue());
        if (entry.getValue() >= 0.0) {
            if (sb.toString().equals("")) {
                sb.append(value);
            } else {
                sb.append(" + ").append(value);
            }
        } else {
            if (sb.toString().equals("")) {
                sb.append(value);
            } else {
                sb.append(" - ").append(value);
            }
        }
    }

    private String getStringCoef(double value) {
        if (value >= 0.0) {
            if ((int) value - value < 0.000001) {
                return String.format("%.0f", value);
            } else {
                return String.format("%.1f", value);
            }
        } else {
            if ((int) value - value < 0.000001) {
                return String.format("%.0f", value * -1);
            } else {
                return String.format("%.1f", value * -1);
            }
        }
    }

    private int findMaxDegree() {
        int degree = 0;
        for (Map.Entry<String, Double> entry : lexemes.entrySet()) {
            if (entry.getKey().startsWith("X")) {
                String tmp = entry.getKey().substring(2);
                for (int i = 0; i < tmp.length(); i++) {
                    if (tmp.charAt(i) < '0' || tmp.charAt(i) > '9') {
                        throw new IllegalArgumentException("Incorrect degree");
                    }
                }
                int d = Integer.parseInt(entry.getKey().substring(2));
                if (d > this.maxDegree && entry.getValue() != 0.0) {
                    degree = d;
                }
            }
        }
        return degree;
    }

    private void defineCoefficients() {
        for (Map.Entry<String, Double> entry : lexemes.entrySet()) {
            if (entry.getKey().equals("X^0") || entry.getKey().equals("freeMember")) {
                c += entry.getValue();
            } else if (entry.getKey().equals("X^1")) {
                b += entry.getValue();
            } else if (entry.getKey().equals("X^2")) {
                a += entry.getValue();
            }
        }
    }

    public String getReducedForm() {
        return reducedForm;
    }

    public int getMaxDegree() {
        return maxDegree;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }
}
