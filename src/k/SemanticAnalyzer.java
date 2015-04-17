package k;


/**
 * Created by Kevin on 4/15/2015.
 */
public class SemanticAnalyzer {

    protected Tree AST;
    protected IDEPanel idePanel;
    protected final String errorPrefix="\nSemantic Error on line ";
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
            case PRINT_STATEMENT:
                analyze_PRINT_STATEMENT(root);
                break;
            case ASSIGNMENT_STATEMENT:
                analyze_ASSIGNMENT_STATEMENT(root);
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

    private void analyze_ASSIGNMENT_STATEMENT(Node root){
        Node val1 = root.children.get(0), val2 = root.children.get(1);
        Symbol s1=currentScope.isDeclared(val1),s2=null;
        if(s1==null){
            addError("Cannot resolve symbol " + val1.token.getData() + ", variable is undefined", val1.token);
        }

            switch (val2.getType()){
                case ID:
                    s2 = currentScope.isDeclared(val1);
                    if(s2==null){
                        addError("Cannot resolve symbol " + val2.token.getData() + ", variable is undefined", val2.token);
                    }
                    break;
            }
    }//..

    private void analyze_PRINT_STATEMENT(Node root){
        Node val1 = root.children.get(0);
        if(currentScope.isDeclared(val1)==null){
            addError("Cannot resolve symbol " + val1.token.getData() + ", variable is undefined", val1.token);

        }
    }//..

    private void analyze_COMPARE(Node root){
        Node val1 = root.children.get(0).children.get(0),val2 = root.children.get(0).children.get(1);
        Symbol s1=null,s2=null;

        switch (val1.getType()){
            case ID:
                s1  = currentScope.isDeclared(val1);
                if(s1==null){
                    addError("Cannot resolve symbol " + val1.token.getData() + ", variable is undefined", val1.token);
                }
                break;
            case INT_EXPR:
                System.out.println("HAVEDIGIT");
                s1=new Symbol(new Token(TokenType.TYPE,"int",val1.children.get(0).token.getLineNum()),val1.children.get(0).getData().toString());
                break;
            case DIGIT:
                s1=new Symbol(new Token(TokenType.TYPE,"int",val1.token.getLineNum()),val1.getData().toString());
                break;
            case BOOLVAL:
                System.out.println("HAVEBOOL");
                s1 = new Symbol(new Token(TokenType.TYPE,"boolean",val1.token.getLineNum()),val1.getData().toString());
                break;
        }

        switch (val2.getType()){
            case ID:
                s2  = currentScope.isDeclared(val2);
                if(s2==null){
                    addError("Cannot resolve symbol " + val2.token.getData() + ", variable is undefined", val2.token);
                }
                break;
            case INT_EXPR:
                System.out.println("HAVEDIGIT");
                s2=new Symbol(new Token(TokenType.TYPE,"int",val2.children.get(0).token.getLineNum()),val2.children.get(0).getData().toString());
                break;
            case DIGIT:
                s2=new Symbol(new Token(TokenType.TYPE,"int",val2.token.getLineNum()),val2.getData().toString());
                break;
            case BOOLVAL:
                System.out.println("HAVEBOOL");
                s2 = new Symbol(new Token(TokenType.TYPE,"boolean",val2.token.getLineNum()),val2.getData().toString());
                break;
        }

        typeMismatch(s1,s2);
    }//..

    protected boolean typeMismatch(Symbol s1, Symbol s2){
        if(s1==null||s2==null)
            return true;

        boolean mismatch = true;

        if(s1.token.getData().equals(s2.token.getData())) {
            mismatch = false;
        }else {
            addError("Incompatible types: "+s1.token.getData()+" "+s1.varName+", "+s2.getData()+" "+s2.varName,s2.token);
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
