package k;

import java.util.ArrayList;

/**
 * Created by Marist User on 3/30/2015.
 */
public class Node {

    Token token = null;
    Node parent = null;
    ArrayList<Node> children = new ArrayList<Node>();


    Node(Token token, Node parent){
        this.token = token;
        this.parent = parent;


    }//..

    public void addChild(Node child){
        children.add(child);
    }//..


}// Node
