package k;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kevin on 1/24/15.
 *
 * This will be the Lexical analyser and output
 *
 */
public class Lex extends JPanel {

    private IDEPanel idePanel;

    Lex(IDEPanel idePanel){
        this.idePanel = idePanel;
        setBounds(Utils.graphicsDevice.getDisplayMode().getWidth() / 2 + 5, 2, Utils.graphicsDevice.getDisplayMode().getWidth() / 4, 500);
        setBackground(Color.RED);

        idePanel.add(this);
    }//..

}// Lex
