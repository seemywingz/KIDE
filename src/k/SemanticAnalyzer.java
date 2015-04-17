package k;

import java.util.ArrayList;

/**
 * Created by Kevin on 4/15/2015.
 */
public class SemanticAnalyzer {

    protected Tree AST;
    protected IDEPanel idePanel;
    protected final String errorPrefix="\nSemantic Error on line ";

    protected ArrayList<Scope> scope = new ArrayList<Scope>();
    protected int currentScope = 0;


    SemanticAnalyzer(IDEPanel idePanel,Tree AST){
        this.AST = AST;
        this.idePanel = idePanel;

        analyze(AST.root);

    }//..

    private void analyze(Node root){

        switch (root.getType()){
            case BLOCK:
                if(scope.size()!=0)
                    currentScope++;
                scope.add(new Scope());
                break;
            case VARDECL:
                analyze_VARDECL(root);
                break;
            case COMPARE:
                analyze_COMPARE(root);
                break;
        }

        for (Node c:root.children){
            analyze(c);
        }
        if(root.getType()==TokenType.BLOCK){
            currentScope--;
            currentScope = currentScope < 0?0:currentScope;
        }


    }//..

    private void analyze_COMPARE(Node root){
        boolean undeclaired;
        if(!scope.get(currentScope).symbolTable.contains(root.children.get(0).children.get(0))){

        }
    }//..

    private void analyze_VARDECL(Node root){

        Symbol symbolEntry = new Symbol(root.children.get(1).token.getData().toString(),root.children.get(0).token);

        boolean noError = true;

        for (Symbol s:scope.get(currentScope).symbolTable){
            if(s.varName.equals(symbolEntry.varName)) {
                noError=false;
                addError("Variable  " + root.children.get(1).token.getData() + " is already defined in this scope", root);
            }
        }

        if(noError){
            System.out.println(symbolEntry);
            scope.get(currentScope).symbolTable.add(symbolEntry);
        }


    }//..

    protected void addError(String error,Node root){
        String newError = errorPrefix+(root.token.getLineNum())+": "+error;
        idePanel.editor.addErrorLineNumber(root.token.getLineNum());
        idePanel.editor.drawLines();
        idePanel.errorPane.getTextArea().append(newError);
        idePanel.parser.parseErrors+=newError;
        System.out.println(newError);
    }//..

}//.. SemanticAnalyzer
