package k;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kevin on 1/31/15.
 */
public class Parser extends ScrollableOutput {

    protected String parseErrors, errorPrefix="\nParse Error on line ";

    Parser(IDEPanel idePanel1) {
        super(idePanel1);
        w = Utils.ScreenWidth/3;

        initTextArea("KIDE: Parser...",35,w/11,false,
                     ScrollableOutput.mkKeyAdapter(keyBuffer,actionMap));

        initScrollPane(new Rectangle(idePanel1.lex.getScrollPane().getX()+idePanel1.lex.getScrollPane().getWidth(),2,w,h));
    }//..


   private void parseProgram(){


   }//..

    private void parseBlock(){

    }//..

    protected void addParseError(int line,TokenType expected,TokenType found){
        parseErrors+=errorPrefix+(line+1)+": expected <"+expected+"> found <"+found+">";
        idePanel.editor.addErrorLineNumber(line);
    }//..

    public String getParseErrors() {
        return parseErrors;
    }//..

}// Parser
