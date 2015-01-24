package k;

import javax.swing.*;

/**
 * Created by kevin on 1/23/15.
 */
public class Editor{

    JTextArea editor;
    JScrollPane scrollPane;
    int w = 700,
        h = 500;

    Editor(JPanel jpanel){

        editor = new JTextArea(w,h);
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);

        scrollPane = new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10,10,w,h);
        jpanel.add(scrollPane);

    }//..

}// Editor
