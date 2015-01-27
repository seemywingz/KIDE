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

    DIGIT("0","1","2","3","4","5","6","7","8","9"),
    INTOP("+","-","/","*"),
    BOOLOP("==","!="),
    ASSIGNMENT("="),
    SPACE("\\ ",""),
    CHAR("a","b","c","d","e","f","g","h","i","j","k","l","m",
         "n","o","p","q","r","s","t","u","v","w","x","y","z"),
    TYPE("int","string","boolean"),
    IF("if"),
    ENDIF("endif"),
    THEN("then"),
    WHILE("while"),
    BEGIN("begin"),
    END("end."),
    COLON(":"),
    SEMICOLON(";"),
    LEFTPAREN("("),
    RIGHTPAREN(")"),
    LEFTCURL("{"),
    RIGHTCURL("}"),
    WHITESPACE("\n");

    public final List<String> pattern;

    private TokenType(String ...pattern) {
        this.pattern = Arrays.asList(pattern);
    }//..

    public static TokenType getByValue(String val) {
        for (TokenType c : values()) {
            if (c.getValues().contains(val)) {
                return c;
            }
        }
        return null;
    }//..

    public List<String> getValues() {
        return pattern;
    }

//    public static boolean isToken(String test){
//        TokenType tokens[] = TokenType.values();
//        for(TokenType t:tokens){
//            if(t.pattern.equals(test)){
//                return true;
//            }
//        }
//        return false;
//    }//..

}// TokenType
