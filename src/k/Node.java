package k;

import java.util.ArrayList;

/**
 * Created by Marist User on 3/30/2015.
 */
public class Node {

    protected Token token = null;
    protected Node parent = null;
    protected ArrayList<Node> children = null;


    Node(Token token, Node parent){
        this.token = token;
        this.parent = parent;
        children =  new ArrayList<Node>();
    }//..

    public void addChild(Node child){
        children.add(child);
    }//..

    public Node getParent() {
        return parent;
    }//..

}// Node
