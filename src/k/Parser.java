package k;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kevin on 1/31/15.
 */
public class Parser extends ScrollableOutput {

    protected String parseErrors, errorPrefix="\nParse Error on line ";
    protected ArrayList<Token> tokens = null;
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
       parseBlock(tokens.get(0),0);
   }//..

    private void parseBlock(Token token,int index){
        switch (token.getType()){
            case LEFTCURL:
                parseStatement(tokens.get(index+1),index+1);
                break;
            case EOF:
                break;
            default:
        }
    }//..

    protected void parseStatement(Token token,int index){

        switch (token.getType()){
            case LEFTCURL:
                parseBlock(tokens.get(index+1),index+1);
                break;
            default:
                break;
        }


    }//..

    protected void addParseError(int line,TokenType expected,TokenType found){
        parseErrors+=errorPrefix+(line+1)+": expected <"+expected+"> found <"+found+">";
        idePanel.editor.addErrorLineNumber(line);
    }//..

    public String getParseErrors() {
        return parseErrors;
    }//..

}// Parser
