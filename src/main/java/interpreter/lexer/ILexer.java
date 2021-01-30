package interpreter.lexer;

public interface ILexer {
    int getLine();
    Token scan();

    void setSource(String src_string);
}
