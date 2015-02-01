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

    DIGIT("digit"),
    STRING("letters"),
    ID("id"),
    INTOP("+"),
    QUOTE("\""),
    BOOLOP("boolop"),
    BOOLVAL("true","false"),
    EXCLAMATION("!"),
    ASSIGNMENT("="),
    TYPE("int","string","boolean"),
    IF("if"),
    WHILE("while"),
    BEGIN("begin:"),
    END("end."),
    PRINT("print"),
    SEMICOLON(";"),
    LEFTPAREN("("),
    RIGHTPAREN(")"),
    LEFTCURL("{"),
    RIGHTCURL("}"),
    SPACE("\n"," ","","\t"),
    EOF("\4"),
    UNSUPPORTED(",","<",">",":","-","%",
                 "@","#","^","&","-","/","*");
//    CHAR("a","b","c","d","e","f","g","h","i","j","k","l","m",
//         "n","o","p","q","r","s","t","u","v","w","x","y","z"),

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
        return TokenType.ID;
    }//..

    public List<String> getPattern() {
        return pattern;
    }//..
}// TokenType
