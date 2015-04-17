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
                currentScope = new Scope(currentScope);
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
        Node val1 = root.children.get(0).children.get(0),val2 = root.children.get(0).children.get(1);
        if(val1.getType()==TokenType.ID){

            Symbol s = currentScope.isDeclared(val1);
            if(s==null){
                addError("Cannot resolve symbol " + val1.token.getData() + ", variable is undefined", val1);
            }else {

            }
        }

    }//..

    protected boolean typeMismatch(Symbol s, Node n){
        if(s==null||n==null)
            return true;

        boolean mismatch = true;

        if(s.getData() == n.token.getType()) {
            mismatch = false;
        }else {
            addError("Incompatible types expected "+s.getType()+" found "+n.getType(),n);
        }
        return mismatch;
    }//..



    private void analyze_VARDECL(Node root){

        if(currentScope.isDeclaredLocally(root.children.get(1)) != null) {
            addError("Variable  " + root.children.get(1).token.getData() + " is already defined in this scope", root);
        }else{
            Symbol symbolEntry = new Symbol(root.children.get(0).token,root.children.get(1).token.getData().toString());
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
