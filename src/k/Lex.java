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

    private int index;
    Lex(IDEPanel idePanel){
        super(idePanel);
        w = (Utils.ScreenWidth / 3);
        h = 500;
        setSize(w,h);

        initTextArea("KIDE: Lexical Analysis...",35,w/12,false,
                super.mkKeyAdapter(keyBuffer,actionMap));
        initScrollPane(new Rectangle(idePanel.editor.getScrollPane().getWidth(),2, w, h));
    }//..

    public void analyze(String s){
        lexErrors = "";
        textArea.setText(title);
        idePanel.editor.resetErrorLineNumber();
        tokens = new ArrayList<Token>();
        String lineSplit[] = s.split("\\n");
        for(int lineNUmber = 0; lineNUmber < lineSplit.length;lineNUmber++){
            for(index=0;index<lineSplit[lineNUmber].length();index++){
                char c = lineSplit[lineNUmber].charAt(index);
                switch (c){
                    case 'i':
                        if(lexKeyword("int", lineSplit[lineNUmber], lineNUmber))
                        break;
                        if(lexKeyword("if", lineSplit[lineNUmber], lineNUmber))
                        break;
                        addToken(""+c,lineNUmber);
                        break;
                    case 's':
                        if(!lexKeyword("string", lineSplit[lineNUmber], lineNUmber)){
                            addToken(""+c,lineNUmber);
                        }
                        break;
                    case 'b':
                        if(!lexKeyword("boolean", lineSplit[lineNUmber], lineNUmber)){
                            addToken(""+c,lineNUmber);
                        }
                        break;
                    case 'w':
                        if(!lexKeyword("while", lineSplit[lineNUmber], lineNUmber)){
                            addToken(""+c,lineNUmber);
                        }
                        break;
                    case 'p':
                        if(!lexKeyword("print", lineSplit[lineNUmber], lineNUmber)){
                            addToken(""+c,lineNUmber);
                        }
                        break;
                    case 'f':
                        if(!lexKeyword("false", lineSplit[lineNUmber], lineNUmber)){
                            addToken(""+c,lineNUmber);
                        }
                        break;
                    case 't':
                        if(!lexKeyword("true", lineSplit[lineNUmber], lineNUmber)){
                            addToken(""+c,lineNUmber);
                        }
                        break;
                    case '"':
                        lexString(lineSplit[lineNUmber],lineNUmber);
                        break;
                    case '=':
                        if(!lexKeyword("==", lineSplit[lineNUmber], lineNUmber)){
                            addToken(""+c,lineNUmber);
                        }
                        break;
                    default:
                        if(TokenType.getByValue(""+c)!=TokenType.SPACE)
                            if(TokenType.getByValue(""+c)!=TokenType.UNSUPPORTED)
                            addToken(""+c,lineNUmber);
                        else
                            addLexError(""+c,lineNUmber);
                        break;
                }
            }
        }
        if(tokens.size() > 0)
            idePanel.parser.parseProgram(tokens);
    }//..

    protected boolean lexString(String stringVal, int lineNumber){
        String data = "";
        int testIndex = new Integer(index);
        if(stringVal.charAt(testIndex) == '"'){
            addToken("\"",lineNumber);
            testIndex++;
            while (testIndex < stringVal.length()){
                switch (stringVal.charAt(testIndex)){
                    case '"':
                        tokens.add(new Token(TokenType.STRING,data,lineNumber));
                        textArea.append("\nFound: <STRING> " + data);
                        index=testIndex;
                        addToken("\"",lineNumber);
                        break;
                    default:
                        if(TokenType.getByValue(""+stringVal.charAt(testIndex))!= TokenType.UNSUPPORTED) {
                            data += stringVal.charAt(testIndex);
                        }else {
                            String error = "\nLex WARNING on line " + (lineNumber + 1) + ": " + stringVal.charAt(testIndex)
                                    + " is not currently supported and will be removed from the string!";
                            lexErrors += error;
                        }
                }
                testIndex++;
            }

        }else
            return false;
        return true;
    }//..

    protected void addToken(String data, int lineNumber){
        TokenType tokenType = TokenType.getByValue(data);
        tokens.add(new Token(tokenType,data,lineNumber));
        textArea.append("\nFound: <" + tokenType + "> " + data);
    }//..

    protected boolean lexKeyword(String keyword,String testString, int lineNumber){
        try {
            int testIndex;
            for (testIndex = 0; testIndex < keyword.length(); testIndex++) {
                if (keyword.charAt(testIndex) != testString.charAt(index + testIndex)) {
                    return false;
                }
            }
            addToken(keyword, lineNumber);
            index += testIndex - 1;
        }catch (Exception e){
            return false;
        }
        return true;
    }//..

    public void addEOF(){
        tokens.add(new Token(TokenType.EOF,"$",tokens.size()));
        idePanel.editor.getTextArea().append("$");
    }//*/

    protected void addLexError(String data, int lineNumber){
        String error = "\nLex Error on line " + (lineNumber + 1) + ": " + data + " is not currently supported and removed from program!";
        lexErrors += error;
    }//..

    public String getLexErrors() {
        return lexErrors;
    }//..
}// Lex

