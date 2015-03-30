package k;

/**
 * Created by Marist User on 3/30/2015.
 */
public class Tree {

    protected Node root = null;
    protected Node currentNode = null;

    Tree(){
        root = new Node(new Token(TokenType.ROOT,"ROOT",0));
        currentNode = root;
    }//..

    public void addBranchNode(Token token){
        Node newNode = new Node(token);



    }//..

}// Tree
