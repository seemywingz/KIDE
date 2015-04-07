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

    TreeView(final IDEPanel idePanel,Tree tree){
        this.idePanel = idePanel;
        this.tree = tree;
                setSize(200,400);
        setTitle("KIDE: Tree View");
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        setIconImage(new ImageIcon(getClass().getResource("/k/img/ide.png")).getImage());

        if(tree != null){
            genTree();
        }else {
            add(new JLabel("   Compile Some Code to Generate a Tree!"));
        }

        setVisible(true);
    }//..

    protected void genTree(){
        JScrollPane scrollPane;
        displayTree = new JTree(tree.buildTreeView());

        scrollPane = new JScrollPane(displayTree);
        add(scrollPane);
    }//..


}// TreeView
