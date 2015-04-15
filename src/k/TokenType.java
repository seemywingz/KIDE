package k;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kevin on 1/24/15.
 *
 * Enumerator for token types
 *
 *
 */
public enum TokenType {

    DIGIT("d1g1t"),
    STRING("letters!@#"),
    CHAR("ch4r"),
    INTOP("+"),
    QUOTE("\""),
    BOOLOP("==","!="),
    BOOLVAL("true","false"),
    EXCLAMATION("!"),
    ASSIGNMENT("="),
    TYPE("int","string","boolean"),
    IF("if"),
    WHILE("while"),
    PRINT("print"),
    LEFTPAREN("("),
    RIGHTPAREN(")"),
    LEFTCURL("{"),
    RIGHTCURL("}"),
    SEMICOLON(";"),
    SPACE("\n"," ","","\t"),
    EOF("$"),
    ID("a","b","c","d","e","f","g","h","i","j","k","l","m",
         "n","o","p","q","r","s","t","u","v","w","x","y","z"),
    UNSUPPORTED(",","<",">",":","-","%",
                 "@","#","^","&","-","/","*","[","]"),
    PROGRAM("PROGRAM"),
    STATEMENT("STATEMENT"),
    STATEMENTLIST("STATEMENTLIST"),
    IF_STATEMENT("IF_STATEMENT"),
    PRINT_STATEMENT("PRINT_STATEMENT"),
    WHILE_STATEMENT("WHILE_STATEMENT"),
    ASSIGNMENT_STATEMENT("ASSIGNMENT_STATEMENT"),
    EXPR("EXPR"),
    INT_EXPR("INT_EXPR"),
    BOOLEAN_EXPR("BOOLEAN_EXPR"),
    STRING_EXPR("STRING_EXPR"),
    VARDECL("VARDECL"),
    BLOCK("BLOCK"),
    COMPARE("COMPARE"),
    ROOT("ROOT");

    public final List<String> pattern;

    private TokenType(String ...pattern) {
        this.pattern = Arrays.asList(pattern);
    }//..

    public static TokenType getByValue(String val) {
        if(Utils.isInt(val)){
            return TokenType.DIGIT;
        }
        for (TokenType t : values()) {
            if (t.getPattern().contains(val)) {
                return t;
            }
        }
        return UNSUPPORTED;
    }//..

    public List<String> getPattern() {
        return pattern;
    }//..
}// TokenType
