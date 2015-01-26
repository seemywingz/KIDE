package k;

/**
 * Created by kevin on 1/24/15.
 *
 * Enumerator for token types
 *
 *
 */
public enum TokenType {

    NUMBER("-?[0-9]+"),
    BINARYOP("[*|/|+|-]"),
    BOOLEAN("(TRUE|FALSE)"),
    WHITESPACE("[ \t\f\r\n]+");

    public final String pattern;

    private TokenType(String pattern) {
        this.pattern = pattern;
    }

}// TokenType
