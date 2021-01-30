package interpreter.lexer;


import java.util.List;

public class Lexer implements ILexer {
    private int line = 1;
    private String source;
    private int pos = 0;

    public Lexer() {
        source = "";
    }

    public void setSource(String source) {
        this.source = source;
        this.pos = 0;
        this.line = 1;
    }

    public int getLine() {
        return line;
    }

    private boolean eol() {
        return pos >= source.length();
    }

    private char next() {
        if (!eol()) {
            pos++;
        }
        return getPeek();
    }

    private char getPeek() {
        char ch = 0;
        if (!eol()) {
            ch = source.charAt(pos);
        }
        return ch;
    }


    public Token scan() {
        char peek = getPeek();
        Token token;
        StringBuilder sb;

        // Пропуск пробельных символов
        do {
            if (peek == '\n') line++;
            else if (peek != ' ' && peek != '\t' && peek != '\r') break;
            peek = next();
        } while (true);

        // Распознать число
        if (Character.isDigit(peek)) {
            sb = new StringBuilder();
            do {
                sb.append(getPeek());
                peek = next();
            } while (Character.isDigit(peek) || peek == '.');

            token = new Token(Tag.NUM, sb.toString());
            return token;
        }

        // Распознать идентификатор или присваивание
        if (Character.isLetter(peek)) {
            sb = new StringBuilder();

            do {
                sb.append(getPeek());
                peek = next();
            } while (Character.isLetterOrDigit(peek) || peek == '_');

            int stop = pos;

            while (Character.isWhitespace(peek)) {
                peek = next();
            }

            if (peek == '=') { // Распознать присваивание
                next();
                token = new Token(Tag.ASSIGN, sb.toString());
            } else { // Распознать идентификатор
                pos = stop;
                token = new Token(Tag.ID, sb.toString());
            }

            return token;
        }

        // Распознать оператор
        List<Character> operators = List.of('+', '-', '*', '/', '^', '(', ')', ';');
        int index = operators.indexOf(peek);

        // Неизвестный символ
        if (index == -1) {
            token = new Token(0, " ");
        } else {
            token = new Token(peek, String.valueOf(peek));
        }
        next();
        return token;
    }
}
