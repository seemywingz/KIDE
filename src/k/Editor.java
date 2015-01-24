package k;


import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by kevin on 1/23/15.
 *
 * The textArea for the IDE
 *
 */
public class Editor extends JPanel{

    private IDEPanel idePanel;
    private ActionMap actionMap;
    public JTextArea textArea,lineNumbers;
    private JScrollPane scrollPane;
    private boolean keyBuffer[] = new boolean[256];
    private int w = 700,
                h = 500,
                rows = 35;
    private int lines =1;

    Editor(IDEPanel idepanel){
        this.idePanel = idepanel;
        setSize(w,h);
//        setLayout(null);
        setBackground(Color.darkGray);

        initLineNUmbers();
        initTextArea();
//        add(new TextLineNumber(textArea));
        add(textArea);

        actionMap = textArea.getActionMap();

        scrollPane = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 10, w, h);
        idepanel.add(scrollPane);
        textArea.requestFocus();

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {

                setLineNUmbers();
                repaint();
            }
        }, 100);
    }//..

    protected void setLineNUmbers(){
        String lineNuberString = "";
        lineNumbers.setText(lineNuberString);

        for (int i=1;i<=lines;i++){
            lineNuberString += "    "+String.valueOf(i)+"\n";
        }
        lineNumbers.setText(lineNuberString);

    }//..

    protected void initTextArea(){
        textArea = new JTextArea(rows,50);

//        textArea.setBounds(1,1,1,1);
//        textArea.setLocation(2,2);
        textArea.setAlignmentX(50f);
//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);
        textArea.setFocusable(true);
        textArea.addKeyListener(mkKeyAdapter());

    }//..

    protected void initLineNUmbers(){
        lineNumbers = new JTextArea(rows,1);
        lineNumbers.setEditable(false);
        lineNumbers.setBackground(Color.lightGray);
        add(lineNumbers);
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
                if(keyBuffer[KeyEvent.VK_ENTER]){
                    lines++;

                }
            }//

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                keyBuffer[e.getKeyCode()]=false;
            }
        };
    }//..

}// Editor
