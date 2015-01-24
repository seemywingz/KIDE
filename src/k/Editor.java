package k;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by kevin on 1/23/15.
 *
 * The editor for the IDE
 *
 */
public class Editor{

    private IDEPanel idePanel;
    private ActionMap actionMap;
    public JTextArea editor;
    private JScrollPane scrollPane;
    private boolean keyBuffer[] = new boolean[256];
    private int w = 700,
                h = 500;

    Editor(IDEPanel idepanel){

        this.idePanel = idepanel;
        editor = new JTextArea(w,h);
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);
        editor.setFocusable(true);
        editor.addKeyListener(mkKeyAdapter());
        actionMap = editor.getActionMap();

        scrollPane = new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10,10,w,h);
        idepanel.add(scrollPane);
        editor.requestFocus();
    }//..

    protected KeyAdapter mkKeyAdapter(){
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                idePanel.ideMenuBar.changed = true;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                keyBuffer[e.getKeyCode()]=true;

                if(keyBuffer[KeyEvent.VK_CONTROL]){
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('x')]){
                        actionMap.get(DefaultEditorKit.cutAction);
                    }
                }
                if(keyBuffer[KeyEvent.VK_CONTROL]){
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('c')]){
                        actionMap.get(DefaultEditorKit.copyAction);
                    }
                }
                if(keyBuffer[KeyEvent.VK_CONTROL]){
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('v')]){
                        actionMap.get(DefaultEditorKit.pasteAction);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                keyBuffer[e.getKeyCode()]=false;
            }
        };
    }//..

}// Editor
