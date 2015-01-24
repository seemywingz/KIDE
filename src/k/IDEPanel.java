package k;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kevin on 1/23/15.
 */
public class IDEPanel extends JPanel{

    Editor editor;

    IDEPanel(){

        setLayout(null);
        setBackground(Color.darkGray);
        editor = new Editor(this);


        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                repaint();
            }
        }, 20);
    }//..

}// IDEPanel
