package k;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by kevin on 1/24/15.
 *
 * This will be the Lexical analyser and output
 *
 */
public class Lex extends JPanel {

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
//        setBounds(Utils.graphicsDevice.getDisplayMode().getWidth() / 2 + 5, 2,
//                  w= Utils.graphicsDevice.getDisplayMode().getWidth() / 4, 500);
        w= Utils.graphicsDevice.getDisplayMode().getWidth() / 4;
        setSize(w,h);
        setBackground(Color.lightGray);

        initTextArea();
        initScrollPane();
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
