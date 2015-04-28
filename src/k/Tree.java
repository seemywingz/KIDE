package k;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Kevin on 3/30/2015.
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

    public void addLeafNode(Node node){
        if(root != null) {
            currentNode.addChild(node);
        }else{
            root = node;
            currentNode = root;
        }
    }//..

    public void returnToParent(boolean inAST){
        if(currentNode.getParent()!=null) {
            if(inAST)
         if(currentNode.getType()==TokenType.BLOCK &&
                 (currentNode.parent.getType()==TokenType.WHILE_STATEMENT
                  || currentNode.parent.getType()==TokenType.IF_STATEMENT)
         )
            currentNode = currentNode.getParent();
         currentNode = currentNode.getParent();
        }
    }//..

    public DefaultMutableTreeNode buildTreeView(){
        return root.buildTreeView(null);
    }//..


}// Tree
