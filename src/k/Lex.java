package k;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by kevin on 1/24/15.
 *
 * This will be the Lexical analyser and output
 *
 */
public class Lex extends JPanel {

    private String errorMsg = "";
    private ArrayList<Integer> errorLineNums = new ArrayList<Integer>();
    private ArrayList<Token> tokens = new ArrayList<Token>();
    private IDEPanel idePanel;
    private JTextArea textArea;
    private ActionMap actionMap;
    private JScrollPane scrollPane;
    private String stringVal;
    private boolean keyBuffer[] = new boolean[256], foundString;
    private Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
    private int w,
                h = 500;

    Lex(IDEPanel idePanel){
        this.idePanel = idePanel;
        w = Utils.graphicsDevice.getDisplayMode().getWidth() / 4;
        setSize(w,h);
        setBackground(Color.lightGray);

        initTextArea();
        initScrollPane();
    }//..

    public void analyze(String s){
        errorMsg = "";
        textArea.setText("KIDE: Lexical Analysis...");
        errorLineNums = new ArrayList<Integer>();
        tokens = new ArrayList<Token>();
        String lineSplit[] = s.split("\\n+");
        for(int i = 0; i < lineSplit.length;i++){
            TokenType token;
            String[] tokenSplit = lineSplit[i].split("(?<=[\\s+])|(?=[\\s+])"+
                                                     "|(?<=[+;=\"])|(?=[+;=\"])"+        // + symbol
                                                     "|(?<=[(\\)])|(?=[(\\)])"+
                                                     "|(?<=[{}])|(?=[{}])");
            for (int j =0;j<tokenSplit.length;j++){
                token = TokenType.getByValue(tokenSplit[j]);
                if(token != null && token != TokenType.NOTSUPPORTED){
                    if(j < tokenSplit.length-1) {// make sure not last token
                        if (token == TokenType.ASSIGNMENT) {// boolop
                            if (TokenType.getByValue(tokenSplit[j + 1]) == TokenType.ASSIGNMENT) {
                                token = TokenType.BOOLOP;
                                j++;
                                tokenSplit[j] = "==";
                            }
                        }
                        if (token == TokenType.EXCLAMATION) {
                            if (TokenType.getByValue(tokenSplit[j + 1]) == TokenType.ASSIGNMENT) {
                                token = TokenType.BOOLOP;
                                j++;
                                tokenSplit[j] = "!=";
                            }
                        }

                        if(token == TokenType.QUOTE ){
                                stringVal="";
                                int q = j+1;
                                while(TokenType.getByValue(tokenSplit[q]) != TokenType.QUOTE){
                                    stringVal+=tokenSplit[q++];
                                }
                                j=q;
                                token=TokenType.STRING;
                                tokenSplit[j]=stringVal;
                        }

                    }// endif not last token

                    if(token != TokenType.SPACE) {
                        tokens.add(new Token(token,tokenSplit[j]));
                        textArea.append("\nFound token: <" + token + "> " + tokenSplit[j]);
                    }
                }else{// token == null
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

    private void initTextArea(){
        textArea = new JTextArea("KIDE: Lexical Analysis...");
        textArea.setRows(35);
        textArea.setColumns(w / 12);
        textArea.setAlignmentX(50f);
        textArea.setFocusable(true);
        textArea.setBorder(border);
        textArea.setEditable(false);
        textArea.setCaretPosition(textArea.getSelectionStart());
        textArea.addKeyListener(mkKeyAdapter());
        add(textArea);

        actionMap = textArea.getActionMap();
    }//..

    protected void initScrollPane(){
        scrollPane = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBounds(Utils.graphicsDevice.getDisplayMode().getWidth() / 2 + 5,2, w, h);
        scrollPane.setBorder( border );
        idePanel.add(scrollPane);
    }//..

    protected KeyAdapter mkKeyAdapter(){
        return new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                keyBuffer[e.getKeyCode()]=true;

                if(keyBuffer[KeyEvent.VK_CONTROL]){// CTRL+
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('x')]){
                        actionMap.get(DefaultEditorKit.cutAction);
                    }
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('c')]){
                        actionMap.get(DefaultEditorKit.copyAction);
                    }
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('v')]){
                        actionMap.get(DefaultEditorKit.pasteAction);
                    }
                }//

            }//

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                keyBuffer[e.getKeyCode()]=false;
            }
        };
    }//..

    public String getErrorMsg() {
        return errorMsg;
    }//..
}// Lex
