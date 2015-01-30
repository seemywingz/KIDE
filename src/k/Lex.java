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

    private String errorMsg = "";
    private ArrayList<Integer> errorLineNums = new ArrayList<Integer>();
    private ArrayList<Token> tokens = new ArrayList<Token>();
    private String stringVal;

    Lex(IDEPanel idePanel){
        super(idePanel);
        this.idePanel = idePanel;
        w = Utils.graphicsDevice.getDisplayMode().getWidth() / 4;
        h = 500;
        setSize(w,h);

        initTextArea("KIDE: Lexical Analysis...",35,w/12,false,
                super.mkKeyAdapter(keyBuffer,actionMap));
        initScrollPane(new Rectangle(Utils.graphicsDevice.getDisplayMode().getWidth() / 2 + 5,2, w, h));
    }//..

    public void analyze(String s){
        errorMsg = "";
        textArea.setText(title);
        errorLineNums = new ArrayList<Integer>();
        tokens = new ArrayList<Token>();
        String lineSplit[] = s.split("\\n+");
        for(int i = 0; i < lineSplit.length;i++){
            TokenType tokenType;
            String[] tokenSplit = lineSplit[i].split("(?<=[\\s+])|(?=[\\s+])"+
                                                     "|(?<=[+;=\"])|(?=[+;=\"])"+
                                                     "|(?<=[(\\)])|(?=[(\\)])"+
                                                     "|(?<=[{}])|(?=[{}])");
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
                        tokens.add(new Token(tokenType,tokenSplit[j]));
                        textArea.append("\nFound: <" + tokenType + "> " + tokenSplit[j]);
                    }
                }else{// tokenType == null
                    errorLineNums.add(i);
                    String error = "\nError on line " + (i + 1) + " " + tokenSplit[j] + " is not currently supported";
                    errorMsg += error;
                    textArea.append(error);
                }
            }
        }
        idePanel.editor.drawLines();
    }//..

    public ArrayList<Integer> getErrorLineNums() {
        return errorLineNums;
    }//..

    public String getErrorMsg() {
        return errorMsg;
    }//..
}// Lex
