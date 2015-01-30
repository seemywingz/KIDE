package k;

import javax.swing.*;

/**
 * Created by Kevin Jayne on 1/23/15.
 *
 * An IDE for Alen's language
 *
 * some source help from
 * http://forum.codecall.net/forum/126-java/?utm_source=codecall&utm_medium=menu&utm_campaign=link
 *
 */

public class IDE extends JFrame{


    static IDEPanel idePanel;

    IDE(){
        setLayout(null);
        setSize(800, 600);
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
