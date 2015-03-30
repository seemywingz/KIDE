package k;

import java.util.ArrayList;

/**
 * Created by Marist User on 3/30/2015.
 */
public class Node {

    Token token = null;
    Node parent = null;
    ArrayList<Node> childres = new ArrayList<Node>();


    Node(Token token){
        this.token = token;


    }//..



}// Node
