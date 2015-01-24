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
    IDEMenuBar ideMenuBar;

    IDEPanel(IDE ide){

        this.ide = ide;

        setLayout(null);
        setBackground(Color.darkGray);
        editor = new Editor(this);
        ideMenuBar = new IDEMenuBar(this);
        lex = new Lex(this);

    }//..

}// IDEPanel
