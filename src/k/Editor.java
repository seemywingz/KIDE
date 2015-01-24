package k;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
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

    private JTextArea textArea;
    private IDEPanel idePanel;
    private ActionMap actionMap;
    private JTextArea lineNumbers;
    private JScrollPane scrollPane;
    private String currentFile = "Untitled";
    private Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
    private boolean keyBuffer[] = new boolean[256],
                    addedLine,
                    fileChanged;
    private int w = 900,
                h = 500,
                rows = 35,
                lines = 1,
                currentLine = 0;

    Editor(IDEPanel idePanel){
        this.idePanel = idePanel;
        w = Utils.graphicsDevice.getDisplayMode().getWidth()/2;
        setBackground(Color.lightGray);
        setSize(w,h);

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

        scrollPane.setBounds(1,2, w, h);
        scrollPane.setBorder( border );
        idePanel.add(scrollPane);
    }//..

    protected void initTextArea(){
        textArea = new JTextArea();

        textArea.setRows(35);
        textArea.setColumns(w / 11);
        textArea.setAlignmentX(50f);
        textArea.setFocusable(true);
        textArea.requestFocus();
        textArea.setBorder(border);
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
        lineNumbers.setBorder(border);
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
                fileChanged = true;
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
                        idePanel.ideMenuBar.saveFile(currentFile);
                    }
                }//

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

    public JTextArea getTextArea() {
        return textArea;
    }//..

    public void setFileChanged(boolean fileChanged) {
        this.fileChanged = fileChanged;
    }//..

    public  boolean getFileChanged(){return fileChanged;}//..

    public String getCurrentFile() {
        return currentFile;
    }//..

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }
}// Editor
