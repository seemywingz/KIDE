package k;

import java.util.ArrayList;

/**
 * Created by Kevin on 4/15/2015.
 */
public class Scope {


    protected Scope parentScope;
    protected ArrayList<Symbol> symbolTable = new ArrayList<Symbol>();

    Scope(Scope parentScope){
        this.parentScope=parentScope;
    }

    public Symbol isDeclared(Node var){
        Symbol declared = null;

        for (Symbol s:symbolTable){
            if(s.varName.equals(var.token.getData())){
                declared=s;
            }
        }

        if(declared==null && parentScope!=null)
           return parentScope.isDeclared(var);

        return declared;
    }//..

    public Symbol isDeclaredLocally(Node var){
        Symbol declared = null;

        for (Symbol s:symbolTable){
            if(s.varName.equals(var.token.getData())){
                declared=s;
            }
        }
        return declared;
    }//..
}//..
