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

}//..
