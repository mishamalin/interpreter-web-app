package interpreter.parser;

import interpreter.lexer.ILexer;
import interpreter.lexer.Tag;
import interpreter.lexer.Token;

import java.io.*;
import java.util.*;


public class Parser {
    private final ILexer lexer;
    private Token next;
    private final Hashtable<String, Double> vars = new Hashtable<>();

    public Parser(ILexer l) {
        lexer = l;
    }

    void error(String s) {
        throw new Error("рядом со строкой номер " + lexer.getLine() + ": " + s);
    }

    public void move() {
        next = lexer.scan();
    }


    void match(int t) {
        if (next.tag == t) move();
        else error("Ошибка синтаксиса.");
    }



    double expr() {
        double x = term();

        while (next.tag == '+' || next.tag == '-') {
            Token op = next;
            move();
            double temp = term();
            x = op.tag == '+' ? x + temp : x - temp;
        }
        return x;
    }

    double term() {
        double x = unary();

        while (next.tag == '*' || next.tag == '/') {

            Token op = next;
            move();
            double temp = unary();
            x = op.tag == '*' ? x * temp : x / temp;

        }
        return x;
    }

    double unary()  {
        if (next.tag == '-') {
            move();
            double temp = factor();
            return -temp;
        }
        else return factor();
    }

    double number() {
        return Double.parseDouble(next.toString());
    }


    double factor() {
        double x = 0;
        switch (next.tag) {
            case '(':
                move();
                x = expr();
                match(')');
                return x;
            case Tag.NUM:
                x = number();
                move();
                return x;
            case Tag.ID:
                Double id = vars.get(next.toString());
                if (id == null) {
                    error(next.toString() + " необъявлено");
                }
                move();
                return id;
            default:
                System.out.println(x);
                error("Ошибка синтаксиса.");
                return x;
        }
    }


    private String statement(){
        switch (next.tag) {
            case Tag.ASSIGN:
                return assign();
            case Tag.ID:
            case Tag.NUM:
            case '(':
                return String.valueOf(expr());
            default:
                return "";
        }
    }

    private String assign() {
        String id = next.toString();
        move();
        double x = expr();
        vars.put(id, x);
        return id + " := " + x;
    }


    private String statements() {
        StringBuilder sb = new StringBuilder();
        String result = statement();
        sb.append(result).append("\n");

        while (next.tag == ';') {
            match(';');
            result = statement();
            sb.append(result).append("\n");
        }

        return sb.toString();
    }


    public String program(String src_string) {
        lexer.setSource(src_string);
        move();
        return statements();
    }

}
