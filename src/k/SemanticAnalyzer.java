package k;


/**
 * Created by Kevin on 4/15/2015.
 */
public class SemanticAnalyzer {

    protected Tree AST;
    protected IDEPanel idePanel;
    protected final String errorPrefix="\nSemantic Error on line ";

//    protected ArrayList<Scope> scope = new ArrayList<Scope>();
    protected Scope currentScope = null;


    SemanticAnalyzer(IDEPanel idePanel,Tree AST){
        this.AST = AST;
        this.idePanel = idePanel;

        analyze(AST.root);
    }//..

    private void analyze(Node root){

        switch (root.getType()){
            case BLOCK:
                currentScope=new Scope(currentScope);
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
            if(currentScope.parentScope!=null)
                currentScope=currentScope.parentScope;
        }


    }//..

    private void analyze_COMPARE(Node root){
        boolean undeclaired = true,typeMismatch=false;
        Node val1 = root.children.get(0).children.get(0),val2 = root.children.get(0).children.get(1);
        if(val1.getType()==TokenType.ID){
            if(isDeclared(val1)!=null){

            }
        }

    }//..

    protected Symbol isDeclared(Node var){
        Symbol declared = null;

        for (Symbol s:currentScope.symbolTable){
            if(s.varName.equals(var.token.getData())){
                declared=s;
            }
        }

        if(declared==null){
            addError("Cannot resolve symbol " + var.token.getData() + ", variable is undefined", var);
        }
        return declared;
    }//..

    private void analyze_VARDECL(Node root){

        Symbol symbolEntry = new Symbol(root.children.get(1).token.getData().toString(),root.children.get(0).token);

        boolean noError = true;

        for (Symbol s:currentScope.symbolTable){
            if(s.varName.equals(symbolEntry.varName)) {
                noError=false;
                addError("Variable  " + root.children.get(1).token.getData() + " is already defined in this scope", root);
            }
        }

        if(noError){
            System.out.println(symbolEntry);
            currentScope.symbolTable.add(symbolEntry);
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
