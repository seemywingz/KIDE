package k;

import javax.swing.*;

/**
 * Created by Kevin Jayne on 1/23/15.
 *
 * An IDE for Alen's language
 *
 */

public class IDE extends JFrame{


    static IDEPanel idePanel;

    IDE(){
        setLayout(null);
        setSize(800, 600);
        setTitle("KIDE");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(idePanel = new IDEPanel(this));
        setJMenuBar(new IDEMenuBar(idePanel));
        setVisible(true);
    }//..

    public static void main(String[] args) {
        new IDE();
    }//..

}// IDE
