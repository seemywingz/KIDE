package k;

/**
 * Created by KevAdmin on 4/16/2015.
 */
public class Symbol {

    String varName;
    Token token;

    Symbol(Token token, String varName){
        this.varName = varName;
        this.token = token;
    }

    public TokenType getType(){
        return token.getType();
    }

    public <Any> Any getData(){
        return token.getData();
    }//..

}// Symbol
