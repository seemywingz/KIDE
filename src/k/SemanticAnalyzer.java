package k;

import java.util.ArrayList;

/**
 * Created by KevAdmin on 4/15/2015.
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
        }

        for (Node c:root.children){
            analyze(c);
        }

    }//..

    private void analyze_VARDECL(Node root){

        String symbolEntry = root.children.get(0).token.getData().toString()+" "+root.children.get(1).token.getData();

        if(scope.get(currentScope).symbolTable.contains(symbolEntry)){
            addError("Variable  "+root.children.get(1).token.getData()+" is already defined in this scope",root);
        }else {
            scope.get(currentScope).symbolTable.add(symbolEntry);
            System.out.println(symbolEntry);
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
