package k;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kevin on 1/24/15.
 *
 * This will be the Lexical analyser and output
 *
 */
public class Lex extends ScrollableOutput {

    private String lexErrors = "";
    private ArrayList<Token> tokens = new ArrayList<Token>();
    private String stringVal;
    private String delimiters = "(?<=[\\s+])|(?=[\\s+])"+
                                "|(?<=[+;=\"])|(?=[+;=\"])"+
                                "|(?<=[(\\)])|(?=[(\\)])"+
                                "|(?<=[{}])|(?=[{}])";

    Lex(IDEPanel idePanel){
        super(idePanel);
        w = (Utils.ScreenWidth / 6);
        h = 500;
        setSize(w,h);

        initTextArea("KIDE: Lexical Analysis...",35,w/12,false,
                super.mkKeyAdapter(keyBuffer,actionMap));
        initScrollPane(new Rectangle(Utils.graphicsDevice.getDisplayMode().getWidth() / 2 ,2, w, h));
    }//..

    public void analyze(String s){
        lexErrors = "";
        textArea.setText(title);
        idePanel.editor.resetErrorLineNumber();
        tokens = new ArrayList<Token>();
        String lineSplit[] = s.split("\\n");
        for(int i = 0; i < lineSplit.length;i++){
            TokenType tokenType;
            String[] tokenSplit = lineSplit[i].split(delimiters);
            for (int j =0;j<tokenSplit.length;j++){
                tokenType = TokenType.getByValue(tokenSplit[j]);
                if(tokenType != null && tokenType != TokenType.UNSUPPORTED){
                    if(j < tokenSplit.length-1) {// make sure not last tokenType
                        if (tokenType == TokenType.ASSIGNMENT) {// boolop
                            if (TokenType.getByValue(tokenSplit[j + 1]) == TokenType.ASSIGNMENT) {
                                tokenType = TokenType.BOOLOP;
                                j++;
                                tokenSplit[j] = "==";
                            }
                        }
                        if (tokenType == TokenType.EXCLAMATION) {
                            if (TokenType.getByValue(tokenSplit[j + 1]) == TokenType.ASSIGNMENT) {
                                tokenType = TokenType.BOOLOP;
                                j++;
                                tokenSplit[j] = "!=";
                            }
                        }

                        if(tokenType == TokenType.QUOTE ){
                                stringVal="";
                                int q = j+1;
                                try {
                                    while (TokenType.getByValue(tokenSplit[q]) != TokenType.QUOTE) {
                                        stringVal += tokenSplit[q++];
                                    }
                                    j = q;
                                    tokenType = TokenType.STRING;
                                    tokenSplit[j] = stringVal;
                                }catch (ArrayIndexOutOfBoundsException ae){
                                }
                        }

                    }// endif not last tokenType

                    if(tokenType != TokenType.SPACE) {
                        tokens.add(new Token(tokenType,tokenSplit[j],i));
                        textArea.append("\nFound: <" + tokenType + "> " + tokenSplit[j]);
                    }
                }else{// tokenType == null
                    idePanel.editor.addErrorLineNumber(i);
                    String error = "\nLex Error on line " + (i + 1) + ": " + tokenSplit[j] + " is not currently supported";
                    lexErrors += error;
                    textArea.append(error);
                }
            }
        }
        if(tokens.size()>0){
            idePanel.parser.parse(tokens);
        }

    }//..

    public String getLexErrors() {
        return lexErrors;
    }//..
}// Lex
