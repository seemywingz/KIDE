package k;

import javax.swing.*;

/**
 * Created by Kevin Jayne on 1/23/15.
 *
 * An IDE for Aanguage
 *
 *
 */

public class IDE extends JFrame{


    LoadingDialog loadingDialog;
    static IDEPanel idePanel;

    IDE(){
        loadingDialog = new LoadingDialog();
        setLayout(null);
        setSize(600, 400);
        setTitle("KIDE");
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/k/img/ide.png")).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
        idePanel = new IDEPanel(this);
        setContentPane(idePanel.editor.getScrollPane());
        setJMenuBar(new IDEMenuBar(idePanel));
        loadingDialog.dispose();
        setVisible(true);
    }//..



    public static void main(String[] args) {
        new IDE();
    }//..

}// IDE
