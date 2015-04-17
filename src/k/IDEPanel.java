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
        editor = new Editor(this);
        System.out.println("***");
        lex = new Lex(this);
        System.out.println("***");
        parser = new Parser(this);
        System.out.println("***");
        errorPane = new ErrorPane(this);
        System.out.println("***");
        ideMenuBar = new IDEMenuBar(this);
        System.out.println("***");
    }//..

}// IDEPanel
