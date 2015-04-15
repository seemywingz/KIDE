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

    TreeView(final IDEPanel idePanel, Tree tree){
        this.idePanel = idePanel;
        this.tree = tree;
                setSize(350,400);
        setTitle("KIDE: Tree View");
        setLocation(idePanel.ide.getLocation().x-400,idePanel.ide.getLocation().y);
        getContentPane().setBackground(Color.white);
        setIconImage(new ImageIcon(getClass().getResource("/k/img/ide.png")).getImage());

        if(tree != null){
            genTreeView();
        }else {
            add(new JLabel("   Compile Some Code to Generate a Tree!"));
        }

        setVisible(true);
    }//..

    protected void genTreeView(){
        JScrollPane scrollPane;
        displayTree = new JTree(tree.buildTreeView());

        scrollPane = new JScrollPane(displayTree);
        add(scrollPane);
    }//..


}// TreeView
