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
    protected boolean noParseErrors = true;

    Parser(IDEPanel idePanel1) {
        super(idePanel1);
        w = Utils.ScreenWidth/3;

        initTextArea("KIDE: Parser...",35,w/11,false,
                     ScrollableOutput.mkKeyAdapter(keyBuffer,actionMap));

        // create a new pane next to the lex output
        initScrollPane(new Rectangle(idePanel1.lex.getScrollPane().getX()+idePanel1.lex.getScrollPane().getWidth(),2,w,h));
    }//..


   public void parseProgram(ArrayList<Token> tokens) {
       this.tokens = tokens;
       noParseErrors=true;
       parseErrors = "";
       textArea.setText("");
       tokenIndex = 0;
       getNextToken();
       parseBlock();
   }//..

    protected void parseBlock(){
       if(isExpected(TokenType.LEFTCURL)){
           parseStatementList();
           isExpected(TokenType.RIGHTCURL);
       }
    }//..

   protected void parseStatementList(){
        parseStatement();
   }//..

    private void parseStatement(){
        switch (currentToken.getType()){
            case ID:
                parseAssignmentStatement();
                parseStatement();
                break;
            case TYPE:
                parseVarDecl();
                parseStatement();
                break;
            case LEFTCURL:
                parseBlock();
                parseStatement();
                break;
            case PRINT:
                parsePrintStatement();
                parseStatement();
                break;
            case IF:
                parseIfStatement();
                parseStatement();
                break;
        }
    }//..

    private void parseIfStatement(){
        if(isExpected(TokenType.IF)){
            parseBooleanExpr();
            parseBlock();
        }
    }//..

    private void parsePrintStatement(){
        if(isExpected(TokenType.PRINT)){
            isExpected(TokenType.LEFTPAREN);
            parseExpr();
            isExpected(TokenType.RIGHTPAREN);
        }
    }//..

    private void parseAssignmentStatement(){
        if(isExpected(TokenType.ID)){
            isExpected(TokenType.ASSIGNMENT);
            parseExpr();
        }
    }//..

    private void parseExpr(){
         switch (currentToken.getType()){
             case ID:
                 isExpected(TokenType.ID);
                 break;
             case DIGIT:
                 parseIntExpr();
                 break;
             case STRING:
                 isExpected(TokenType.STRING);
                 break;
             case BOOLVAL:
                 parseBooleanExpr();
                 break;
             case LEFTPAREN:
                 parseBooleanExpr();
                 break;
             default:
                 addParseError("<ID>, <DIGIT>, <STRING>, <BOOLVAL>");
         }
    }//..

    private void parseBooleanExpr(){
        switch (currentToken.getType()){
            case BOOLVAL:
                isExpected(TokenType.BOOLVAL);
                break;
            case LEFTPAREN:
                isExpected(TokenType.LEFTPAREN);
                parseExpr();
                isExpected(TokenType.BOOLOP);
                parseExpr();
                isExpected(TokenType.RIGHTPAREN);
                break;
            default:
                addParseError("<BOOLVAL>, <RIGHTPAREN>");
        }
    }//..

    private void parseIntExpr(){
        if(isExpected(TokenType.DIGIT)){
            Token next =  peekNextToken();
            if(next != null) {
                if(next.getType() == TokenType.INTOP) {
                    isExpected(TokenType.INTOP);
                    parseExpr();
                }
            }
        }
    }//..

    private void parseVarDecl(){
        if(isExpected(TokenType.TYPE)){
            isExpected(TokenType.ID);
        }
    }//..

    protected boolean isExpected(TokenType expected){
        boolean result = true;
        textArea.append("\nExpecting token <"+expected+">");
        textArea.append("\n    Found token <"+currentToken.getType()+">");
        if(currentToken.getType() != expected){
            addParseError(expected);
            result=false;
        }
        getNextToken();
        return result;
    }//..

    protected Token peekNextToken(){
        if(tokenIndex < tokens.size()-1){
            return tokens.get(tokenIndex+1);
        }
        return null;
    }//..

    protected void getNextToken(){
        if(tokenIndex <= tokens.size()-1){
            currentToken = tokens.get(tokenIndex++);
        }
    }//..

    protected void addParseError(TokenType expected){
        String newError = errorPrefix+(currentToken.getLineNum()+1)+": expected <"+expected+"> found <"+currentToken.getType()+">";
        parseErrors+= newError;
        textArea.append(newError);
        idePanel.editor.addErrorLineNumber(currentToken.getLineNum());
        noParseErrors=false;
    }//..

    protected void addParseError(String expected){
        String newError = errorPrefix+(currentToken.getLineNum()+1)+": expected "+expected+" found <"+currentToken.getType()+">";
        parseErrors+= newError;
        textArea.append(newError);
        idePanel.editor.addErrorLineNumber(currentToken.getLineNum());
        noParseErrors=false;
    }//..

    public String getParseErrors() {
        return parseErrors;
    }//..

}// Parser


/*
*
*   private void parseAssignment(){
        switch (currentToken.getType()){
            case ID:
                getNextToken();
                isExpected(TokenType.ASSIGNMENT);
                parseExpr();
                break;
        }
    }//..

    protected void parseExpr(){
        switch (currentToken.getType()){
            case ID:
                isExpected(TokenType.ID);
                break;
            case DIGIT:
                parseIntExpr();
                break;
            default:
                addParseError("<ID>, <DIGIT>",currentToken);
                break;
        }
    }//..

    private void parseIntExpr(){
        switch (currentToken.getType()){
            case DIGIT:
                isExpected(TokenType.DIGIT);
                getNextToken();
                if(currentToken.getType()!=TokenType.INTOP){
                    getNextToken();
                    parseIntExpr();
                }
                break;
            default:
                isExpected(TokenType.DIGIT);
                break;
        }
    }//..**/