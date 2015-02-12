package k;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kevin on 1/31/15.
 */
public class Parser extends ScrollableOutput {

    protected String parseErrors, errorPrefix="\nParse Error on line ";
    private ArrayList<Token> tokensInGlobalScope = new ArrayList<Token>();
    private ArrayList<Token> tokensInLocalScope = new ArrayList<Token>();
    private ArrayList<String> definedVariables = new ArrayList<String>();
    private boolean inNewScope;

    Parser(IDEPanel idePanel1) {
        super(idePanel1);
        w = Utils.ScreenWidth/3;

        initTextArea("KIDE: Parser...",35,w/11,false,
                     ScrollableOutput.mkKeyAdapter(keyBuffer,actionMap));

        initScrollPane(new Rectangle(idePanel1.lex.getScrollPane().getX()+idePanel1.lex.getScrollPane().getWidth(),2,w,h));
    }//..

    public void parse(ArrayList<Token> tokens){
        parseErrors = "";
        textArea.setText(title);
        tokensInGlobalScope = new ArrayList<Token>();
        tokensInLocalScope = new ArrayList<Token>();
        definedVariables = new ArrayList<String>();

//        if(tokens.get(0).getType() != TokenType.BEGIN){
//            idePanel.editor.addErrorLineNumber(tokens.get(0).getLineNum());
//            parseErrors+="\nParse Error on line "+(tokens.get(0).getLineNum()+1)+": program must start with 'begin:'";
//        }

        for(int i=0;i<tokens.size();i++){
                // Recursive Descent Parser
                Token token = tokens.get(i);
                int tokensChecked = 0;
                switch (token.getType()) {
                    case TYPE:
                        i += typeCheck(tokens, i);
                        break;

                    default:
                }
        }//

//        if(tokens.get(tokens.size()-1).getType()!= TokenType.END){
//            idePanel.editor.addErrorLineNumber(tokens.get(tokens.size()-1).getLineNum());
//            parseErrors+="\nParse Error on line "+(tokens.get(tokens.size()-1).getLineNum()+1)+": program must end with 'end.'";
//        }
        idePanel.editor.drawLines();
        textArea.append(parseErrors);
    }//..

    private int typeCheck(ArrayList<Token> tokens,int i){
        int tokensParsed = 0;
        Token token;
        try {
            if ((token = tokens.get(i + 1)).getType() == TokenType.ID) {
                String data = token.getData();
                if(inNewScope){

                }else{
                    boolean err=false;
                    for (Token t:tokensInGlobalScope){
                        if(t.getData().equals(data)) {
                            parseErrors += errorPrefix +( token.getLineNum()+1) + ": variable '" + data + "' already defined in this scope";
                            err=true;
                            idePanel.editor.addErrorLineNumber(token.getLineNum());
                        }
                    }
                    if(!err){
                        tokensInGlobalScope.add(token);
                    }
                }

            } else {
                addParseError(tokens.get(i + 1).getLineNum(), TokenType.ID, tokens.get(i + 1).getType());
                return 1;
            }
            return tokensParsed;
        }catch (Exception e){
            return tokensParsed;
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
