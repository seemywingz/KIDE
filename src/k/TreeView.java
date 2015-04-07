package k;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marist User on 4/6/2015.
 */
public class TreeView extends JFrame{

    protected IDEPanel idePanel;
    Tree tree;
    JTree displayTree;

    TreeView(IDEPanel idePanel,Tree tree){
        this.idePanel = idePanel;
        this.tree = tree;
                setSize(600,400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);

        if(tree != null){
            genTree();
        }else {
//            genTree();
            add(new JLabel("\t\t\t   Compile Some Code to Generate a CST!"));
        }

        setVisible(true);
    }//..

    protected void genTree(){
        JScrollPane scrollPane;
        JPanel panel = new JPanel();

        displayTree = new JTree(tree.buildTreeView());
        panel.add(displayTree);

        scrollPane = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }//..


}// TreeView
