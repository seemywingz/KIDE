package k;


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by kevin on 1/23/15.
 *
 * The textArea for the IDE
 *
 */
public class Editor extends ScrollableOutput{

    private JTextArea lineNumbers;
    private String currentFile = "Untitled";
    private boolean addedLine,
                    fileChanged,
                    analyzed = true;
    private int lines = 1,
                currentLine = 0;

    private ArrayList<Integer> errorLineNums = new ArrayList<Integer>();


    Editor(final IDEPanel idePanel){
        super(idePanel);
        w = Utils.ScreenWidth/2;

        initLineNumbers();
        initTextArea(" ",35,w/11,true,mkKeyAdapter());
        initScrollPane(new Rectangle(1,2, w, h));

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                setLineNumbers();
                if(!analyzed){
                    System.out.println("analyzing");
                    idePanel.lex.analyze(textArea.getText());
                    analyzed = true;
                }
                drawLines();
            }
        }, 20);
    }//..

    protected void setLineNumbers(){

        if(addedLine) {
            try {
                int caretpos = textArea.getCaretPosition();
                currentLine = textArea.getLineOfOffset(caretpos) + 1;
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            if (lines < currentLine) {
                lines = currentLine;
            }
                drawLines();
                addedLine = false;
        }

    }//..

    protected void drawLines(){
        String lineNuberString = "";
        String error;
        for (int i = 0; i <= lines-1; i++) {
            if(errorLineNums.contains((i)))
                error = "*";
            else
                error = "";
            if(i == 0)
                lineNuberString += "   "+error + String.valueOf((i+1))+error +" ";
            else
            if(i<9)
                lineNuberString += "\n   "+error + String.valueOf((i+1))+error +" ";
            else
                lineNuberString += "\n  "+error + String.valueOf((i+1))+error +" ";
        }

        lineNumbers.setText(lineNuberString);
    }//..

    public void open(){
        lines=0;
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        addedLine=true;
        setLineNumbers();
    }//..

    protected void initLineNumbers(){
        lineNumbers = new JTextArea();

        lineNumbers.setRows(35);
        lineNumbers.setColumns(1);
        lineNumbers.setText("   1 ");
        lineNumbers.setEditable(false);
        lineNumbers.setBorder(border);
        lineNumbers.setBackground(Options.backgroundColor);
        add(lineNumbers);
    }//..

    protected KeyAdapter mkKeyAdapter(){
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                fileChanged = true;
                analyzed = false;
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
                        int len = textArea.getDocument().getLength();
                        textArea.setCaretPosition(len);
                        addedLine=true;
                        setLineNumbers();
                        idePanel.lex.analyze(textArea.getText());
                        drawLines();
                    }
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('s')]){
                        idePanel.ideMenuBar.saveFile(currentFile);
                    }
                }//

                if(keyBuffer[KeyEvent.VK_ENTER]) {
                    lines++;
                    addedLine = true;
                    analyzed = false;
                }

            }//

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                keyBuffer[e.getKeyCode()]=false;
            }
        };
    }//..

    public void addErrorLineNumber(int i){
        if(!errorLineNums.contains(i)){
            errorLineNums.add(i);
        }
    }//..

    public void setDirty(){analyzed=false;}

    public void resetErrorLineNumber(){
        errorLineNums = new ArrayList<Integer>();
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
