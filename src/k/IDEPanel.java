package k;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kevin Jayne on 1/23/15.
 *
 * the IDE Panel with a text textArea
 *
 */
public class IDEPanel extends JPanel{

    IDE ide;
    Lex lex;
    Editor editor;
    Parser parser;
    ErrorPane errorPane;
    IDEMenuBar ideMenuBar;

    IDEPanel(IDE ide){

        this.ide = ide;

        setLayout(null);
        setBackground(Color.black);
        lex = new Lex(this);
        editor = new Editor(this);
        errorPane = new ErrorPane(this);
        parser = new Parser(this);
        ideMenuBar = new IDEMenuBar(this);
    }//..

}// IDEPanel
