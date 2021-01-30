package interpreter.lexer;

public class Token {
    public final int tag;
    public String lexeme;

    public Token(int t, String l) {
        tag = t;
        lexeme = l;
    }

    @Override
    public String toString() {
        String s;
        switch (tag) {
            case Tag.NUM:
                s = String.valueOf(lexeme);
                break;
            case Tag.ID:
            case Tag.ASSIGN:
                s = lexeme;
                break;
            default:
                s = "" + (char)tag;
        }
        return s;
    }
}
