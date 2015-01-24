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
    Editor editor;
    IDEMenuBar ideMenuBar;

    IDEPanel(IDE ide){

        this.ide = ide;

        setLayout(null);
        setBackground(Color.darkGray);
        editor = new Editor(this);
        add(ideMenuBar = new IDEMenuBar(this));



        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                repaint();
            }
        }, 20);
    }//..

}// IDEPanel
