package k;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Marist User on 4/6/2015.
 */
public class TreeView extends JFrame{

    private IDEPanel idePanel;
    Tree tree;
    JTree displayTree;

    TreeView(IDEPanel idePanel,Tree tree){
        this.idePanel = idePanel;
        this.tree = tree;
                setSize(600,400);
        setLocationRelativeTo(null);

        if(tree != null){
            genTree();
        }else {
            genTree();
            //add(new JLabel("\t\t\t   Compile Some Code to Generate a CST!"));
        }

        setVisible(true);
    }//..

    protected void genTree(){
        //DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.root.getType());
        DefaultMutableTreeNode root = null;

        tree.buildTreeView(root);

        displayTree = new JTree(root);
        add(displayTree);
    }//..


}// TreeView
