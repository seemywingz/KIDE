package k;

import javax.swing.*;

/**
 * Created by Kevin Jayne on 1/23/15.
 *
 * An IDE for Alen's language
 *
 *
 */

public class IDE extends JFrame{


    static IDEPanel idePanel;

    IDE(){
        setLayout(null);
        setSize(1200, 800);
        setTitle("KIDE");
        setIconImage(new ImageIcon(getClass().getResource("/k/img/ide.png")).getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(idePanel = new IDEPanel(this));
        setJMenuBar(new IDEMenuBar(idePanel));
        setVisible(true);
    }//..

    public static void main(String[] args) {
        new IDE();
    }//..

}// IDE
