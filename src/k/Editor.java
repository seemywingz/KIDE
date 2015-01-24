package k;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by kevin on 1/23/15.
 *
 * The textArea for the IDE
 *
 */
public class Editor extends JPanel{

    public JTextArea textArea;

    private IDEPanel idePanel;
    private ActionMap actionMap;
    private JTextArea lineNumbers;
    private JScrollPane scrollPane;
    private boolean keyBuffer[] = new boolean[256],
                    addedLine;
    private int w = 600,
                h = 500,
                rows = 35,
                lines = 1,
                currentLine = 0;

    Editor(IDEPanel idePanel){
        this.idePanel = idePanel;
        setSize(w,h);
        setBackground(Color.darkGray);

        initLineNumbers();
        initTextArea();
        initScrollPane();

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {

                setLineNumbers();
            }
        }, 20);
    }//..

    protected void setLineNumbers(){

        try {
            int caretpos = textArea.getCaretPosition();
            currentLine = textArea.getLineOfOffset(caretpos) + 1;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        if(lines < currentLine ) {
            lines = currentLine;
        }
        if(addedLine){
            drawLines();
            addedLine=false;
        }

    }//..

    protected void drawLines(){
        String lineNuberString = "   1";
        for (int i = 2; i <= lines; i++) {
            if(i<10)
                lineNuberString += "\n   " + String.valueOf(i)+" ";
            else
                lineNuberString += "\n " + String.valueOf(i)+" ";
        }
        lineNumbers.setText(lineNuberString);
    }//..

    public void open(){
        lines=0;
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        addedLine=true;
        drawLines();
    }//..

    protected void initScrollPane(){
        scrollPane = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 10, w, h);
        Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
        scrollPane.setBorder( border );
        idePanel.add(scrollPane);
    }//..

    protected void initTextArea(){
        textArea = new JTextArea();

        textArea.setRows(35);
        textArea.setColumns(49);
        textArea.setAlignmentX(50f);
        textArea.setFocusable(true);
        textArea.requestFocus();
        textArea.setCaretPosition(textArea.getSelectionStart());
        textArea.addKeyListener(mkKeyAdapter());
        textArea.addCaretListener(mkCaretListener());
        add(textArea);

        actionMap = textArea.getActionMap();
    }//..

    protected void initLineNumbers(){
        lineNumbers = new JTextArea();

        lineNumbers.setRows(rows);
        lineNumbers.setColumns(1);
        lineNumbers.setText("   1 ");
        lineNumbers.setEditable(false);
        lineNumbers.setBackground(Color.lightGray);
        add(lineNumbers);
    }//..

    protected CaretListener mkCaretListener(){
        return new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {

            }
        };
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
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('s')]){
                        idePanel.ideMenuBar.save.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                            //Nothing need go here, the actionPerformed method (with the
                            //above arguments) will trigger the respective listener
                        });
                    }
                }

                if(keyBuffer[KeyEvent.VK_ENTER]) {
                    lines++;
                    addedLine = true;
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
