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

    public <ANY> ANY getData(){return token.getData();}//..

    public Token getToken() {
        return token;
    }//..

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

//        System.out.println("building AST: " + getType());

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
                AST.returnToParent(true);
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
        if(children.get(2).children.get(0).children.size()>1){// if assigning int expression to variable
            addASTSegment_INT_EXPR(AST,children.get(2).children.get(0));
        }else {
            switch (children.get(2).children.get(0).getType()){
                case ID:
                    AST.addLeafNode(children.get(2).children.get(0));
                    break;
                case INT_EXPR:
                    AST.addLeafNode(children.get(2).children.get(0).children.get(0).token);

            }
        }
        AST.returnToParent(true);
    }//..

    private void addASTSegment_INT_EXPR(Tree AST,Node root){
        AST.addBranchNode(root.children.get(1).token);
        AST.addLeafNode(root.children.get(0));
        if(root.children.get(2).children.get(0).children.size()>1){
            addASTSegment_INT_EXPR(AST,root.children.get(2).children.get(0));
        }else {
            AST.addLeafNode(root.children.get(2).children.get(0).children.get(0));
        }

        AST.returnToParent(true);
    }//..

    protected void addASTSegment_PRINT_STATEMENT(Tree AST){
        AST.addBranchNode(token);
        AST.addLeafNode(children.get(2).getLeafNode());
        AST.returnToParent(true);
    }//..

    protected void addASTSegment_WHILE_STATEMENT(Tree AST){
        AST.addBranchNode(token);
        AST.addBranchNode(new Token(TokenType.COMPARE,"COMPARE",0));
        AST.addBranchNode(children.get(1).children.get(2).token);// boolop
        AST.addLeafNode(children.get(1).children.get(1).children.get(0));
        if(children.get(1).children.get(3).children.get(0).children.size()>1){
            addASTSegment_INT_EXPR(AST,children.get(1).children.get(3).children.get(0));
        }else {
            AST.addLeafNode(children.get(1).children.get(3).children.get(0).children.get(0));
        }
        AST.returnToParent(true);
        AST.returnToParent(true);
    }//..

    protected void addASTSegment_IF_STATEMENT(Tree AST){
        AST.addBranchNode(token);
        AST.addBranchNode(new Token(TokenType.COMPARE,"COMPARE",0));
        AST.addBranchNode(children.get(1).children.get(2).token);// boolop

        if(children.get(1).children.get(1).children.get(0).children.size()>1){
            addASTSegment_INT_EXPR(AST,children.get(1).children.get(1).children.get(0));
        }else {
            AST.addLeafNode(children.get(1).children.get(1).children.get(0));
        }

        if(children.get(1).children.get(3).children.get(0).children.size()>1){
            addASTSegment_INT_EXPR(AST,children.get(1).children.get(3).children.get(0));
        }else {
            AST.addLeafNode(children.get(1).children.get(3).children.get(0).children.get(0));
        }
        AST.returnToParent(true);
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
