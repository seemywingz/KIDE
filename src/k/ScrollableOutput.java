package k;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by kevin.jayne1 on 1/29/2015.
 */
public class ScrollableOutput extends JPanel{

    protected String title;
    protected IDEPanel idePanel;
    protected JTextArea textArea;
    protected ActionMap actionMap;
    protected JScrollPane scrollPane;
    protected boolean keyBuffer[] = new boolean[256];
    protected Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
    protected int w = 100,
                  h = 500;

    ScrollableOutput(IDEPanel idePanel){
        this.idePanel= idePanel;
        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                setBackground(Options.backgroundColor);
                if(textArea!=null)
                textArea.setBackground(Options.textAreaColor);
            }
        }, 1);
    }//..

    protected void initTextArea(String title,int rows,int cols,boolean editable,KeyListener keyListener){
        textArea = new JTextArea(this.title = title);
        textArea.setRows(rows);
        textArea.setColumns(cols);
        textArea.setAlignmentX(50f);
        textArea.setFocusable(true);
        textArea.requestFocus();
        textArea.setBorder(border);
        textArea.setEditable(editable);
        textArea.setCaretPosition(textArea.getSelectionStart());
        textArea.addKeyListener(keyListener);
        add(textArea);
        actionMap = textArea.getActionMap();
    }//..

    protected void initScrollPane(Rectangle bounds){
        scrollPane = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBounds(bounds);
        scrollPane.setBorder( border );
        idePanel.add(scrollPane);
    }//..

    public static KeyAdapter mkKeyAdapter(final boolean keyBuffer[],final ActionMap actionMap){
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

    public JTextArea getTextArea() {
        return textArea;
    }//..

    public JScrollPane getScrollPane() {
        return scrollPane;
    }//..
}// ScrollableOutput
