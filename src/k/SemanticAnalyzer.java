package k;

import java.util.ArrayList;

/**
 * Created by KevAdmin on 4/15/2015.
 */
public class SemanticAnalyzer {

    protected Tree AST;

    protected ArrayList<Scope> scope = new ArrayList<Scope>();


    SemanticAnalyzer(Tree AST){
        this.AST = AST;

        analyze(AST.root);

    }//..

    private void analyze(Node root){

        switch (root.getType()){
            case BLOCK:
                scope.add(new Scope());
                break;
            case VARDECL:
                break;
        }
    }//..

    private void analyze_VARDECL(Node root){



    }//..

}//.. SemanticAnalyzer
