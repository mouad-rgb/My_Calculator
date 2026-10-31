package com.example.myapplication;

public class ExpressionParser {

    private final String s;
    private int pos;
    private double x;

    public ExpressionParser(String expression) {
        s = expression.toLowerCase()
                .replace(" ", "")
                .replace(",", ".")
                .replace("\u03C0", "pi");
    }

    public double eval(double xValue) {
        x = xValue;
        pos = 0;
        double value = parseExpression();
        if (pos < s.length()) throw new RuntimeException("Unexpected: " + s.charAt(pos));
        return value;
    }

    private double parseExpression() {
        double value = parseTerm();
        while (pos < s.length()) {
            char c = s.charAt(pos);
            if (c == '+') {
                pos++;
                value += parseTerm();
            } else if (c == '-') {
                pos++;
                value -= parseTerm();
            } else {
                break;
            }
        }
        return value;
    }

    private double parseTerm() {
        double value = parseFactor();
        while (pos < s.length()) {
            char c = s.charAt(pos);
            if (c == '*') {
                pos++;
                value *= parseFactor();
            } else if (c == '/') {
                pos++;
                value /= parseFactor();
            } else if (Character.isLetterOrDigit(c) || c == '(' || c == '.') {
                value *= parseFactor();
            } else {
                break;
            }
        }
        return value;
    }

    private double parseFactor() {
        if (pos < s.length() && s.charAt(pos) == '+') {
            pos++;
            return parseFactor();
        }
        if (pos < s.length() && s.charAt(pos) == '-') {
            pos++;
            return -parseFactor();
        }
        double base = parsePrimary();
        if (pos < s.length() && s.charAt(pos) == '^') {
            pos++;
            double exponent = parseFactor();
            return Math.pow(base, exponent);
        }
        return base;
    }

    private double parsePrimary() {
        if (pos >= s.length()) throw new RuntimeException("Unexpected end");

        char c = s.charAt(pos);

        if (c == '(') {
            pos++;
            double value = parseExpression();
            if (pos >= s.length() || s.charAt(pos) != ')') throw new RuntimeException("Missing )");
            pos++;
            return value;
        }

        if (Character.isDigit(c) || c == '.') {
            int start = pos;
            while (pos < s.length() && (Character.isDigit(s.charAt(pos)) || s.charAt(pos) == '.')) pos++;
            return Double.parseDouble(s.substring(start, pos));
        }

        if (Character.isLetter(c)) {
            int start = pos;
            while (pos < s.length() && Character.isLetter(s.charAt(pos))) pos++;
            String name = s.substring(start, pos);

            switch (name) {
                case "x": return x;
                case "pi": return Math.PI;
                case "e": return Math.E;
            }

            if (pos >= s.length() || s.charAt(pos) != '(') throw new RuntimeException("Missing ( after " + name);
            pos++;
            double arg = parseExpression();
            if (pos >= s.length() || s.charAt(pos) != ')') throw new RuntimeException("Missing )");
            pos++;

            switch (name) {
                case "sin": return Math.sin(arg);
                case "cos": return Math.cos(arg);
                case "tan": return Math.tan(arg);
                case "asin": return Math.asin(arg);
                case "acos": return Math.acos(arg);
                case "atan": return Math.atan(arg);
                case "log": return Math.log10(arg);
                case "ln": return Math.log(arg);
                case "sqrt": return Math.sqrt(arg);
                case "abs": return Math.abs(arg);
                case "exp": return Math.exp(arg);
                default: throw new RuntimeException("Unknown function: " + name);
            }
        }

        throw new RuntimeException("Unexpected: " + c);
    }
}