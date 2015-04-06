package k;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Marist User on 3/30/2015.
 */
public class Tree {

    protected Node root = null;
    protected Node currentNode = null;

    Tree(){
        currentNode = root;
    }//..

    public void addBranchNode(Token token){

        if(root != null) {
            Node newNode = new Node(token, currentNode);
            currentNode.addChild(newNode);
            currentNode = newNode;
        }else{
            root = new Node(token,root);
            currentNode = root;
        }

    }//..

    public void addLeafNode(Token token){
        if(root != null) {
            Node newNode = new Node(token, currentNode);
            currentNode.addChild(newNode);
        }else{
            root = new Node(token,root);
            currentNode = root;
        }
    }//..

    public void returnToParent(){
        currentNode = currentNode.getParent();
    }//..

    public void buildTreeView(DefaultMutableTreeNode node){

    }//..


}// Tree
