package k;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kevin on 1/31/15.
 */
public class Parser extends ScrollableOutput {

    protected String parseErrors;
    private ArrayList<Token> tokensInGlobalScope = new ArrayList<Token>();
    private ArrayList<Token> tokensInLocalScope = new ArrayList<Token>();
    private ArrayList<Token> definedVariables = new ArrayList<Token>();

    Parser(IDEPanel idePanel1) {
        super(idePanel1);
        w = Utils.ScreenWidth/3;

        initTextArea("KIDE: Parser...",35,w/11,false,
                     ScrollableOutput.mkKeyAdapter(keyBuffer,actionMap));

        initScrollPane(new Rectangle(idePanel1.lex.getScrollPane().getX()+idePanel1.lex.getScrollPane().getWidth(),2,w,h));
    }//..

    public void parse(ArrayList<Token> tokens){
        parseErrors = "";
        tokensInGlobalScope = new ArrayList<Token>();
        tokensInLocalScope = new ArrayList<Token>();
        definedVariables = new ArrayList<Token>();

        if(tokens.get(0).getType() != TokenType.BEGIN){
            idePanel.editor.addErrorLineNumber(tokens.get(0).getLineNum());
            parseErrors+="\nParse Error on line "+(tokens.get(0).getLineNum()+1)+": program must start with 'begin:'";
        }

        for(Token token:tokens){
            // Recursive Descent Parser
        }//

        if(tokens.get(tokens.size()-1).getType()!= TokenType.END){
            idePanel.editor.addErrorLineNumber(tokens.get(tokens.size()-1).getLineNum());
            parseErrors+="\nParse Error on line "+(tokens.get(tokens.size()-1).getLineNum()+1)+": program must end with 'end.'";
        }
        idePanel.editor.drawLines();
    }//..

    public String getParseErrors() {
        return parseErrors;
    }//..

}// Parser
