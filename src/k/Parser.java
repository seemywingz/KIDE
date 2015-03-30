package k;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kevin on 1/31/15.
 */
public class Parser extends ScrollableOutput {

    protected String parseErrors;
    protected final String errorPrefix="\nParse Error on line ";
    protected ArrayList<Token> tokens = null;
    protected Token currentToken;
    protected int tokenIndex = 0;
    protected boolean noParseErrors = true;
    protected Tree CST;

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

       CST = new Tree();
       CST.addBranchNode(new Token(TokenType.PROGRAM,"PROGRAM",0));

       tokenIndex = 0;
       getNextToken();
       parseBlock();
       noParseErrors = isExpected(TokenType.EOF);
       if(tokenIndex<tokens.size()){
           warnUnreachableCode();
       }
//       idePanel.loadingDialog.dispose();
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
        CST.addBranchNode(new Token(TokenType.STATEMENT,"STATEMENT",currentToken.getLineNum()));
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
            case WHILE:
                parseWhileStatement();
                parseStatement();
                break;
        }
    }//..

    private void parseWhileStatement(){
        if(isExpected(TokenType.WHILE)){
            parseBooleanExpr();
            parseBlock();
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
        CST.addBranchNode(new Token(TokenType.ASSIGNMENT_STATEMENT,"ASSIGNMENT_STATEMENT",currentToken.getLineNum()));
        if(isExpected(TokenType.ID)){
            CST.addBranchNode(currentToken);
            isExpected(TokenType.ASSIGNMENT);
            CST.addBranchNode(currentToken);
            parseExpr();
        }
    }//..

    private void parseExpr(){
        CST.addBranchNode(new Token(TokenType.EXPR,"EXPR",currentToken.getLineNum()));
        Token next = null;
         switch (currentToken.getType()){
             case ID:
                 if((next = peekNextToken())!=null)
                 if(next.getType()==TokenType.ASSIGNMENT){
                     parseAssignmentStatement();
                     break;
                 }
                 isExpected(TokenType.ID);
                 break;
             case DIGIT:
                 parseIntExpr();
                 break;
             case QUOTE:
                 parseStringExpr();
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

    private void parseIntExpr(){
        if(isExpected(TokenType.DIGIT)){
                if(currentToken.getType()==TokenType.INTOP) {
                    isExpected(TokenType.INTOP);
                    parseExpr();
                }
        }
    }//..

    private void parseStringExpr(){
        if(isExpected(TokenType.QUOTE)) {
           if (isExpected(TokenType.STRING)){
               isExpected(TokenType.QUOTE);
           }
        }
    }//..

    private void parseBooleanExpr(){
        switch (currentToken.getType()){
            case BOOLVAL:
                isExpected(TokenType.BOOLVAL,true);
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

    private void parseVarDecl(){
        CST.addBranchNode(new Token(TokenType.VARDECL,"VARDECL",currentToken.getLineNum()));
        if(isExpected(TokenType.TYPE)){
            CST.addBranchNode(currentToken);
            isExpected(TokenType.ID);
        }
    }//..

    protected boolean isExpected(TokenType expected,int addBranchNode){
        boolean result = true;
        textArea.append("\nExpecting token <"+expected+">");
        textArea.append("\n    Found token <"+currentToken.getType()+">");
        if(currentToken.getType() != expected){
            addParseError(expected);
            result=false;
        }
        if(addBranchNode !=0)
            switch (addBranchNode) {
                case 1:
                    CST.addBranchNode(currentToken);
                    break;
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
        }else{
            if(tokens.get(tokenIndex-1).getType() != TokenType.EOF){
                idePanel.lex.addEOF();
            }
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
        String newError = errorPrefix+(currentToken.getLineNum())+": expected "+expected+" found <"+currentToken.getType()+">";
        parseErrors+= newError;
        textArea.append(newError);
        idePanel.editor.addErrorLineNumber(currentToken.getLineNum());
        idePanel.editor.drawLines();
        noParseErrors=false;
    }//..

    protected void warnUnreachableCode(){
        String warning = "\nWARNING! There is unreachable code after EOF symbol '$', code will not be compiled or executed";
        parseErrors+= warning;
        textArea.append(warning);
        idePanel.editor.addErrorLineNumber(currentToken.getLineNum());
        idePanel.editor.drawLines();
        noParseErrors=false;
    }//..

    public String getParseErrors() {
        return parseErrors;
    }//..

}// Parser
