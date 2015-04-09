package k;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Created by KevAdmin on 4/9/2015.
 */
public class ASTView extends JFrame{


    protected IDEPanel idePanel;
    protected Tree CST;

    ASTView(IDEPanel idePanel){

        this.idePanel = idePanel;
        this.CST = idePanel.parser.getCST();
        setSize(350,400);
        setTitle("KIDE: Tree View");
        setLocation(idePanel.ide.getLocation().x-400,idePanel.ide.getLocation().y);
        getContentPane().setBackground(Color.white);
        setIconImage(new ImageIcon(getClass().getResource("/k/img/ide.png")).getImage());

        if(CST != null){
            genAST();
        }else {
            add(new JLabel("   Compile Some Code to Generate a Tree!"));
        }

        setVisible(true);

    }//..

    protected void genAST(){

        DefaultMutableTreeNode displayTreeRoot = CST.buildAstTreeView();



    }//..

}//..
