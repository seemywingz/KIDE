package k;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marist User on 4/6/2015.
 */
public class CSTView extends JFrame{

    protected IDEPanel idePanel;
    Tree CST;
    JTree displayTree;

    CSTView(final IDEPanel idePanel){
        this.idePanel = idePanel;
        this.CST = idePanel.parser.getCST();
                setSize(350,400);
        setTitle("KIDE: Tree View");
        setLocation(idePanel.ide.getLocation().x-400,idePanel.ide.getLocation().y);
        getContentPane().setBackground(Color.white);
        setIconImage(new ImageIcon(getClass().getResource("/k/img/ide.png")).getImage());

        if(CST != null){
            genCSTView();
        }else {
            add(new JLabel("   Compile Some Code to Generate a Tree!"));
        }

        setVisible(true);
    }//..

    protected void genCSTView(){
        JScrollPane scrollPane;
        displayTree = new JTree(CST.buildCSTTreeView());

        scrollPane = new JScrollPane(displayTree);
        add(scrollPane);
    }//..


}// TreeView
