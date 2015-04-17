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
        Symbol s1,s2;
        if(val1.getType()==TokenType.ID){
            s1  = currentScope.isDeclared(val1);
            if(s1==null){
                addError("Cannot resolve symbol " + val1.token.getData() + ", variable is undefined", val1.token);
            }else {
            }
        }else if(val1.getType()==TokenType.INT_EXPR) {
            System.out.println("HAVEDIGIT");
            s1=new Symbol(new Token(TokenType.TYPE,"int",val1.children.get(0).token.getLineNum()),val1.children.get(0).getData().toString());
        }

//        typeMismatch(s1,s);
    }//..

    protected boolean typeMismatch(Symbol s1, Symbol s2){
        if(s1==null||s2==null)
            return true;

        boolean mismatch = true;

        if(s1.token.getData().equals(s2.token.getData())) {
            mismatch = false;
        }else {
            addError("Incompatible types expected "+s1.token.getData()+" found "+s2.getData(),s2.token);
        }
        return mismatch;
    }//..



    private void analyze_VARDECL(Node root){

        if(currentScope.isDeclaredLocally(root.children.get(1)) != null) {
            addError("Variable  " + root.children.get(1).token.getData() + " is already defined in this scope", root.token);
        }else{
            Symbol symbolEntry = new Symbol(root.children.get(0).token,root.children.get(1).token.getData().toString());
            currentScope.symbolTable.add(symbolEntry);
        }

    }//..

    protected void addError(String error,Token token){
        String newError = errorPrefix+(token.getLineNum()+1)+": "+error;
        idePanel.editor.addErrorLineNumber(token.getLineNum());
        idePanel.editor.drawLines();
        idePanel.errorPane.getTextArea().append(newError);
        idePanel.parser.parseErrors+=newError;
        System.out.println(newError);
    }//..

}//.. SemanticAnalyzer
