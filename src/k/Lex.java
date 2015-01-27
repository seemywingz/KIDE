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

    private ArrayList<Integer> errorLineNums = new ArrayList<Integer>();
    private ArrayList<TokenType> tokens = new ArrayList<TokenType>();
    private IDEPanel idePanel;
    private JTextArea textArea;
    private ActionMap actionMap;
    private JScrollPane scrollPane;
    private boolean keyBuffer[] = new boolean[256];
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
        textArea.setText("");
        errorLineNums = new ArrayList<Integer>();
        tokens = new ArrayList<TokenType>();
        String lineSplit[] = s.split("[\\n]");
        for(int i = 0; i < lineSplit.length;i++){
            TokenType token;
            // (?=[:;+=])|(?<=[:;+=]) equals to select an empty character before ; or after ;.
            // + means the characters can be combined one or more times. to create one single delimiter
            String[] tokenSplit = lineSplit[i].split("\\s+|(?=[:;+=(\\)])|\\s+|(?<=[:;+=(\\)])|\\s+");
            for (String tokenString:tokenSplit){
                token = TokenType.getByValue(tokenString);
                if(token != null){
                    if(token == TokenType.ASSIGNMENT){

                    }
                    if(token!=TokenType.SPACE) {
                        tokens.add(token);
                        textArea.append("\nFound token: " + token + " : " + tokenString);
                    }
                }else{
                    errorLineNums.add(i);
                    textArea.append("\nError on line "+(i+1)+": "+tokenString+" is not a token");
                }
            }
        }
        idePanel.editor.drawLines();
    }//..

    public ArrayList<Integer> getErrorLineNums() {
        return errorLineNums;
    }//..

    private void initTextArea(){
        textArea = new JTextArea("Lex:");
        textArea.setRows(35);
        textArea.setColumns(w / 12);
        textArea.setAlignmentX(50f);
        textArea.setFocusable(true);
        textArea.setBorder(border);
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

}// Lex
