package k;

import javax.swing.*;

/**
 * Created by kevin on 1/23/15.
 */

public class IDE extends JFrame{



    IDE(){
        setLayout(null);
        setSize(800, 600);
        setTitle("KIDE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new IDEPanel());
        setVisible(true);
    }//..

    public static void main(String[] args) {
        new IDE();
    }//..

}// IDE
