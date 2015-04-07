package k;


import javax.swing.tree.DefaultMutableTreeNode;
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
    protected DefaultMutableTreeNode displayTree;

    Parser(IDEPanel idePanel1) {
        super(idePanel1);
        w = Utils.ScreenWidth/3;
        frame.setTitle("KIDE: Parser");

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
       noParseErrors = isExpected(TokenType.EOF,2);
       CST.returnToParent();
       if(tokenIndex<tokens.size()){
           warnUnreachableCode();
       }
//       idePanel.loadingDialog.dispose();
   }//..

    protected void parseBlock(){
        CST.addBranchNode(new Token(TokenType.BLOCK,"BLOCK",currentToken.getLineNum()));
       if(isExpected(TokenType.LEFTCURL,2)){
           parseStatementList();
           isExpected(TokenType.RIGHTCURL,2);
       }
        CST.returnToParent();
    }//..

   protected void parseStatementList(){
       CST.addBranchNode(new Token(TokenType.STATEMENTLIST,"STATEMENTLIST",currentToken.getLineNum()));
       switch (currentToken.getType()){
           case ID:
           case TYPE:
           case LEFTCURL:
           case PRINT:
           case IF:
           case WHILE:
               parseStatement();
               parseStatementList();
       }
       CST.returnToParent();
   }//..

    private void parseStatement(){
        CST.addBranchNode(new Token(TokenType.STATEMENT,"STATEMENT",currentToken.getLineNum()));
        switch (currentToken.getType()){
            case ID:
                parseAssignmentStatement();
                break;
            case TYPE:
                parseVarDecl();
                break;
            case LEFTCURL:
                parseBlock();
                break;
            case PRINT:
                parsePrintStatement();
                break;
            case IF:
                parseIfStatement();
                break;
            case WHILE:
                parseWhileStatement();
                break;
        }
        CST.returnToParent();
    }//..

    private void parseWhileStatement(){
        CST.addBranchNode(new Token(TokenType.WHILE_STATEMENT,"WHILE_STATEMENT",currentToken.getLineNum()));
        if(isExpected(TokenType.WHILE,2)){
            parseBooleanExpr();
            parseBlock();
        }
        CST.returnToParent();
    }//..

    private void parseIfStatement(){
        CST.addBranchNode(new Token(TokenType.IF_STATEMENT,"IF_STATEMENT",currentToken.getLineNum()));
        if(isExpected(TokenType.IF,2)){
            parseBooleanExpr();
            parseBlock();
        }
        CST.returnToParent();
    }//..

    private void parsePrintStatement(){
        CST.addBranchNode(new Token(TokenType.PRINT_STATEMENT,"PRINT_STATEMENT",currentToken.getLineNum()));
        if(isExpected(TokenType.PRINT,2)){
            isExpected(TokenType.LEFTPAREN,2);
            parseExpr();
            isExpected(TokenType.RIGHTPAREN,2);
        }
        CST.returnToParent();
    }//..

    private void parseAssignmentStatement(){
        CST.addBranchNode(new Token(TokenType.ASSIGNMENT_STATEMENT,"ASSIGNMENT_STATEMENT",currentToken.getLineNum()));
        if(isExpected(TokenType.ID,2)){
            isExpected(TokenType.ASSIGNMENT,2);
            parseExpr();
        }
        CST.returnToParent();
    }//..

    private void parseExpr(){
        CST.addBranchNode(new Token(TokenType.EXPR,"EXPR",currentToken.getLineNum()));
        Token next;
         switch (currentToken.getType()){
             case ID:
                 if((next = peekNextToken())!=null)
                 if(next.getType()==TokenType.ASSIGNMENT){
                     parseAssignmentStatement();
                     break;
                 }
                 isExpected(TokenType.ID,2);
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
        CST.returnToParent();
    }//..

    private void parseIntExpr(){
        CST.addBranchNode(new Token(TokenType.INT_EXPR,"INT_EXPR",currentToken.getLineNum()));
        if(isExpected(TokenType.DIGIT,2)){
                if(currentToken.getType()==TokenType.INTOP) {
                    isExpected(TokenType.INTOP,2);
                    parseExpr();
                }
        }
        CST.returnToParent();
    }//..

    private void parseStringExpr(){
        CST.addBranchNode(new Token(TokenType.STRING_EXPR,"STRING_EXPR",currentToken.getLineNum()));
        if(isExpected(TokenType.QUOTE,2)) {
           if (isExpected(TokenType.STRING,2)){
               isExpected(TokenType.QUOTE,2);
           }
        }
        CST.returnToParent();
    }//..

    private void parseBooleanExpr(){
        CST.addBranchNode(new Token(TokenType.BOOLEAN_EXPR,"BOOLEAN_EXPR",currentToken.getLineNum()));
        switch (currentToken.getType()){
            case BOOLVAL:
                isExpected(TokenType.BOOLVAL,1);
                break;
            case LEFTPAREN:
                isExpected(TokenType.LEFTPAREN,2);
                parseExpr();
                isExpected(TokenType.BOOLOP,2);
                parseExpr();
                isExpected(TokenType.RIGHTPAREN,2);
                break;
            default:
                addParseError("<BOOLVAL>, <RIGHTPAREN>");
        }
        CST.returnToParent();
    }//..

    private void parseVarDecl(){
        CST.addBranchNode(new Token(TokenType.VARDECL,"VARDECL",currentToken.getLineNum()));
        if(isExpected(TokenType.TYPE,2)){
            isExpected(TokenType.ID,2);
        }
        CST.returnToParent();
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
                case 2:
                    CST.addLeafNode(currentToken);
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

    public Tree getCST() {
        return CST;
    };
}// Parser
