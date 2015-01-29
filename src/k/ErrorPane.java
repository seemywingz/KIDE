package k;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by kevin on 1/27/15.
 *
 * Lex and Parse errors show here in a more readable form
 *
 *
 */
public class ErrorPane extends JPanel{


    private IDEPanel idePanel;
    private JTextArea textArea;
    private ActionMap actionMap;
    private JScrollPane scrollPane;
    private Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
    private int w,
            h = 500;


    ErrorPane(IDEPanel idePanel) {
        this.idePanel = idePanel;
        w = Utils.ScreenWidth;
        h = Utils.ScreenHeight;
//        setSize(w,h-(idePanel.editor.getHeight()));
        setBackground(Color.lightGray);

        initTextArea();
        initScrollPane();

    }//..

    private void initTextArea(){
        textArea = new JTextArea("KIDE: Error Pane...");
        textArea.setRows(100);
        textArea.setColumns(w);
        textArea.setFocusable(true);
        textArea.setBorder(border);
        textArea.setEditable(false);
        textArea.addKeyListener(idePanel.lex.mkKeyAdapter());
        add(textArea);
        actionMap = textArea.getActionMap();
    }//..

    protected void initScrollPane(){
        scrollPane = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        scrollPane.setBounds(2, idePanel.editor.getHeight() + 5, w - 10, h = h - (idePanel.editor.getHeight() + 55));
        scrollPane.setBorder( border );
        idePanel.add(scrollPane);
    }//..

    public JTextArea getTextArea() {
        return textArea;
    }//..
}// ErrorPane
