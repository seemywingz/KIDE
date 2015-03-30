package k;

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
        }else{
            root = new Node(token,root);
        }

    }//..

}// Tree
