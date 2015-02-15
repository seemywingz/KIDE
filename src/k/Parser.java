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
       if(isExpected(TokenType.LEFTCURL,currentToken)) {
           noParseErrors = parseBlock();
       }else {
           addParseError(TokenType.LEFTCURL,currentToken);
       }
       if(noParseErrors){
           // do semantical analysis
       }

   }//..

    protected boolean parseBlock(){
        switch (currentToken.getType()){
            case LEFTCURL:
                getNextToken();
                return parseStatement();
            case RIGHTCURL:
                return true;
            default:
                addParseError("Block",currentToken);
                return false;
        }
    }//..

    protected boolean parseStatement(){
        switch (currentToken.getType()){
            case TYPE:
                return parseVarDecl();
            case ID:
                return parseAssignmentStatement();
            case RIGHTCURL:
                return parseBlock();
            default:
                addParseError("Statement",currentToken);
                return false;
        }
    }//..

    protected boolean parseAssignmentStatement(){
        switch (currentToken.getType()){
            case ID:
                getNextToken();// if next not assignment
                if(!isExpected(TokenType.ASSIGNMENT,currentToken)){
                    // incorrect assignment statement
                    addParseError(TokenType.ASSIGNMENT,currentToken);
                    return false;
                }// we have assignment
                getNextToken();// should have an expr next
                return parseExper();
            default:
                addParseError(TokenType.ID,currentToken);
                return isExpected(TokenType.ID,currentToken);
        }
    }//..

    protected boolean parseExper(){
        switch (currentToken.getType()){

            case ID:
                return true;
            case STRING:
                return true;
            case DIGIT:
                return parseIntExpr();
            default:
                addParseError("Expression",currentToken);
                return false;
        }
    }//..

    protected boolean parseIntExpr(){
        switch (currentToken.getType()){
            case DIGIT:
                getNextToken();// if next not intop
                if(!isExpected(TokenType.INTOP,currentToken)){
                    // we have digit
                    return true;
                }else {// we have intop
                    getNextToken();// should have expression next
                    return parseExper();
                }
            default:
                addParseError(TokenType.DIGIT,currentToken);
                return isExpected(TokenType.DIGIT,currentToken);
        }
    }//..

    protected boolean parseVarDecl(){
        switch (currentToken.getType()){
            case TYPE:
                getNextToken();// if next not ID
                if(!isExpected(TokenType.ID,currentToken)){
                    addParseError(TokenType.ID,currentToken);
                    return false;
                }// we have ID
                return true;
            default:
                addParseError(TokenType.TYPE, currentToken);
                return isExpected(TokenType.TYPE,currentToken);
        }
    }//..

    protected boolean isExpected(TokenType expected,Token token){

        textArea.append("\nExpecting token <"+expected+">");
        textArea.append("\n    Found token <"+token.getType()+">");

        if(token.getType() == expected){
            return true;
        }
        return false;
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
