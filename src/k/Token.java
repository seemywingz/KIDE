package k;

/**
 * Created by kevin.jayne1 on 1/29/2015.
 */
public class Token {

    private TokenType type;
    String data;
    private int lineNum;

    Token(TokenType type, String data,int lineNum){
        this.type = type;this.data=data;
        this.lineNum = lineNum;
    }

    public TokenType getType() {
        return type;
    }//..

    public int getLineNum() {
        return lineNum;
    }//..
}// Token
