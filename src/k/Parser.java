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
       this.tokens = tokens;
       boolean noParseErrors = false;
       parseErrors = "";
       textArea.setText("");
       tokenIndex = 0;
       getNextToken();
       parseBlock();

   }//..

    protected void parseBlock(){
        switch (currentToken.getType()){
            case LEFTCURL:
                isExpected(TokenType.LEFTCURL);
                parseStatementList();
                isExpected(TokenType.RIGHTCURL);
            default:
                isExpected(TokenType.LEFTCURL);
        }
    }//..

   protected void parseStatementList(){
       switch (currentToken.getType()){
           case TYPE:
               parseVarDecl();
           case ID:
               parseAssignment();
           default:
               getNextToken();
       }
   }//..

    private void parseAssignment(){
        switch (currentToken.getType()){
            case ID:
                getNextToken();
                isExpected(TokenType.ASSIGNMENT);
                parseExpr();
        }
    }//..

    protected void parseExpr(){
        switch (currentToken.getType()){
            case ID:
                isExpected(TokenType.ID);
                break;
            case DIGIT:
                parseIntExpr();
            default:
                addParseError("<ID>, <DIGIT>",currentToken);
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
            default:
                isExpected(TokenType.DIGIT);
        }
    }//..

    private void parseVarDecl(){
        switch (currentToken.getType()){
            case TYPE:
                getNextToken();
                isExpected(TokenType.ID);
                break;
            default:
                addParseError("<TYPE>",currentToken);
        }
    }//..


    protected void isExpected(TokenType expected){

        Token token = currentToken;
        textArea.append("\nExpecting token <"+expected+">");
        textArea.append("\n    Found token <"+token.getType()+">");

        if(token.getType() != expected){
            addParseError(expected,token);
        }
        getNextToken();
    }//..

    protected Token peekNextToken(){
        return tokens.get(tokenIndex+1);
    }//..

    protected void getNextToken(){

        if(tokenIndex <= tokens.size()-1){
            currentToken = tokens.get(tokenIndex++);
        }
        if(tokens.get(tokens.size()-1).getType() != TokenType.EOF){
            eofWarning();
        }
    }//..

    protected void eofWarning(){
        parseErrors +="\nWARNING: file needs to end with '$', token added";
        tokens.add(new Token(TokenType.EOF,"$",tokens.get(tokens.size()-1).getLineNum()));
        idePanel.editor.getTextArea().append("$");
    }//..

    protected void addParseError(TokenType expected,Token found){
        String newError = errorPrefix+(found.getLineNum()+1)+": expected <"+expected+"> found <"+found.getType()+">";
        parseErrors+= newError;
        textArea.append(newError);
        idePanel.editor.addErrorLineNumber(found.getLineNum());
    }//..

    protected void addParseError(String expected,Token found){
        String newError = errorPrefix+(found.getLineNum()+1)+": expected "+expected+" found <"+found.getType()+">";
        parseErrors+= newError;
        textArea.append(newError);
        idePanel.editor.addErrorLineNumber(found.getLineNum());
    }//..

    public String getParseErrors() {
        return parseErrors;
    }//..

}// Parser
