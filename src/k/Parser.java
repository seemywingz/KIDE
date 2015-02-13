package k;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kevin on 1/31/15.
 */
public class Parser extends ScrollableOutput {

    protected String parseErrors, errorPrefix="\nParse Error on line ";
    protected ArrayList<Token> tokens = null;
    protected Token currentToken;
    protected int tokenIndex = 0;

    Parser(IDEPanel idePanel1) {
        super(idePanel1);
        w = Utils.ScreenWidth/3;

        initTextArea("KIDE: Parser...",35,w/11,false,
                     ScrollableOutput.mkKeyAdapter(keyBuffer,actionMap));

        // create a new pane next to the lex output
        initScrollPane(new Rectangle(idePanel1.lex.getScrollPane().getX()+idePanel1.lex.getScrollPane().getWidth(),2,w,h));
    }//..


   public void parseProgram(ArrayList<Token> tokens){
       parseErrors = "";
       this.tokens = tokens;
       tokenIndex = 0;
       getNextToken();
       if(isExpectedToken(currentToken,TokenType.LEFTCURL)){
           parseBlock();
       }
   }//..

    private boolean parseBlock(){

        switch (currentToken.getType()){
            case LEFTCURL:
                getNextToken();
                return parseStatement();
            default:
                return isExpectedToken(currentToken,TokenType.RIGHTCURL);
        }
    }//..

    protected boolean parseStatement(){
       switch (currentToken.getType()){
           case TYPE:
               getNextToken();
               return parseVarDecl();
           case ID:
               getNextToken();
               if(isExpectedToken(currentToken,TokenType.ASSIGNMENT)) {
                   getNextToken();
                   return parseExpr();
               }return false;
           default:
               return parseBlock();
       }
    }//..


    protected boolean parseExpr(){
        switch (currentToken.getType()){
            case ID:
                return true;
            case DIGIT:
                return parseIntExpr();
            default:
                return false;

        }

    }//..

    protected boolean parseIntExpr(){
                if(peekNextToken().getType() == TokenType.INTOP){
                    getNextToken();
                    isExpectedToken(currentToken,TokenType.DIGIT);
                }else
                    return isExpectedToken(currentToken,TokenType.DIGIT);
        return false;
    }//..

    protected boolean parseVarDecl(){
        if(isExpectedToken(currentToken,TokenType.ID)){
            return true;
        }
        return false;
    }//..

    protected boolean isExpectedToken(Token token, TokenType expected){
        if(token.getType() == expected)
            return true;
        else {
            addParseError(token.getLineNum(),expected,token.getType());
            return false;
        }
    }//..

    protected Token peekNextToken(){
        return tokens.get(tokenIndex+1);
    }//..

    protected void getNextToken(){

        if(tokenIndex <= tokens.size()-1){
            currentToken = tokens.get(tokenIndex);
            if(tokenIndex == tokens.size()-1){
                if(currentToken.getType()!=TokenType.EOF)
                    eofWarning();
            }
            tokenIndex++;
        }
    }//..

    protected void eofWarning(){
        parseErrors +="\nWARNING: file needs to end with '$', token added";
        tokens.add(new Token(TokenType.EOF,"$",tokens.get(tokens.size()-1).getLineNum()));
        idePanel.editor.getTextArea().append("$");
    }//..

    protected void addParseError(int line,TokenType expected,TokenType found){
        parseErrors+=errorPrefix+(line+1)+": expected <"+expected+"> found <"+found+">";
        idePanel.editor.addErrorLineNumber(line);
    }//..

    public String getParseErrors() {
        return parseErrors;
    }//..

}// Parser
