package k;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

/**
 * Created by Kevin on 3/30/2015.
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

    public TokenType getType(){return token.getType();}//..


    public DefaultMutableTreeNode buildTreeView(DefaultMutableTreeNode root){

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("< "+token.getData()+" >");

        if(root == null){
            root = newNode;
            for (Node n:children){
                n.buildTreeView(root);
            }
        }else {
            for (Node n:children){
                n.buildTreeView(newNode);
            }
            root.add(newNode);
            return root;
        }
        return root;
    }//..

    public void buildAST(Tree AST){

        System.out.println("building AST: " + getType());

        switch (getType()){
            case BLOCK:
                 addASTSegment_BLOCK(AST);
                break;
            case VARDECL:
                 addASTSegment_VARDECL(AST);
                break;
            case ASSIGNMENT_STATEMENT:
                addASTSegment_ASSIGNMENT_STATEMENT(AST);
                break;
            case PRINT_STATEMENT:
                addASTSegment_PRINT_STATEMENT(AST);
                break;
            case WHILE_STATEMENT:
                addASTSegment_WHILE_STATEMENT(AST);
                break;
            case IF_STATEMENT:
                addASTSegment_IF_STATEMENT(AST);
            case RIGHTCURL:
                AST.returnToParent();
                break;
        }
        for (Node c:children){
            c.buildAST(AST);
        }
    }//..

    protected void addASTSegment_BLOCK(Tree AST){
       AST.addBranchNode(token);
    }//..

    protected void addASTSegment_VARDECL(Tree AST) {
        AST.addLeafNode(this);
    }//..

    protected void addASTSegment_ASSIGNMENT_STATEMENT(Tree AST){
        AST.addBranchNode(token);
        AST.addLeafNode(children.get(0));
        AST.addLeafNode(children.get(2).getLeafNode());
        AST.returnToParent();
    }//..

    protected void addASTSegment_PRINT_STATEMENT(Tree AST){
        AST.addBranchNode(token);
        AST.addLeafNode(children.get(2).getLeafNode());
        AST.returnToParent();
    }//..

    protected void addASTSegment_WHILE_STATEMENT(Tree AST){
        AST.addBranchNode(token);
        AST.addBranchNode(new Token(TokenType.COMPARE,"COMPARE",0));
        AST.addBranchNode(children.get(1).children.get(2).token);
        AST.addLeafNode(children.get(1).children.get(1).children.get(0));
        AST.addLeafNode(children.get(1).children.get(3).children.get(0).children.get(0));
        AST.returnToParent();
        AST.returnToParent();
    }//..

    protected void addASTSegment_IF_STATEMENT(Tree AST){
        AST.addBranchNode(token);
        AST.addBranchNode(new Token(TokenType.COMPARE,"COMPARE",0));
        AST.addBranchNode(children.get(1).children.get(2).token);
        AST.addLeafNode(children.get(1).children.get(1).children.get(0));
        AST.addLeafNode(children.get(1).children.get(3).children.get(0).children.get(0));
        AST.returnToParent();
    }//..

    protected Node getLeafNode(){
        if(getType() == TokenType.STRING_EXPR){
            return children.get(1);
        }
       if(children.size()==0){
           return this;
       }else {
           for (Node c:children){
               return c.getLeafNode();
           }
       }
        return null;
    }//..

}// Node
