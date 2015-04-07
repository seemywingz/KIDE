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
public class ScrollableOutput{

    protected String title;
    protected IDEPanel idePanel;
    protected JTextArea textArea;
    protected ActionMap actionMap;
    protected JScrollPane scrollPane;
    protected JPanel panel = new JPanel();
    protected JFrame frame = new JFrame();
    protected boolean keyBuffer[] = new boolean[256];
    protected static Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
    protected boolean viable;
    protected int w = 300,
                  h = 500;

    ScrollableOutput(IDEPanel idePanel){
        this.idePanel= idePanel;

        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(getClass().getResource("/k/img/ide.png")).getImage());

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                panel.setBackground(Options.backgroundColor);
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
        panel.add(textArea);
        actionMap = textArea.getActionMap();
    }//..

    protected void initScrollPane(Rectangle bounds){
        scrollPane = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBounds(bounds);
        scrollPane.setBorder( border );
        frame.add(scrollPane);
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

    public void showHide(){
        viable=!viable;
        frame.setVisible(viable);
    }//..

    public JTextArea getTextArea() {
        return textArea;
    }//..

    public JScrollPane getScrollPane() {
        return scrollPane;
    }//..

    public JPanel getPanel() {
        return panel;
    }
}// ScrollableOutput
