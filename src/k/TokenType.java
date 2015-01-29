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
    STRING("string"),
    ID("id"),
    INTOP("+","-","/","*"),
    BOOLOP("=="),
    EXCLAMATION("!"),
    ASSIGNMENT("="),
    SPACE("\\ ",""),
    CHAR("a","b","c","d","e","f","g","h","i","j","k","l","m",
         "n","o","p","q","r","s","t","u","v","w","x","y","z"),
    TYPE("int","string","boolean"),
    IF("if"),
    ENDIF("endif"),
    THEN("then"),
    WHILE("while"),
    ENDWHILE("endwhile"),
    BEGIN("begin:"),
    END("end."),
    DO("do"),
    PRINT("print"),
    BOOLEAN("boolean"),
    TRUE("true"),
    FALSE("false"),
    SEMICOLON(";"),
    LEFTPAREN("("),
    RIGHTPAREN(")"),
    LEFTCURL("{"),
    RIGHTCURL("}");

    public final List<String> pattern;

    private TokenType(String ...pattern) {
        this.pattern = Arrays.asList(pattern);
    }//..

    public static TokenType getByValue(String val) {

        if(Utils.isInt(val)){
            return TokenType.DIGIT;
        }

        for (TokenType c : values()) {
            if (c.getValues().contains(val)) {
                return c;
            }
        }

//        return null;
        return TokenType.ID;
    }//..

    public List<String> getValues() {
        return pattern;
    }//..


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
