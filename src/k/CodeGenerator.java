package k;

import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Kevin on 4/28/2015.
 */
public class CodeGenerator extends ScrollableOutput{


    Tree AST;
    String [] codeStream = new String[256];
    ArrayList<TempVar> tempVars = new ArrayList<TempVar>();
    int byteCnt = 0,tempNum=0, heap = 256;
    Scope symbolTable,curentScope;

    CodeGenerator (final IDEPanel idePanel){
        super(idePanel);

        frame.setLocation(idePanel.editor.getScrollPane().getX()+idePanel.editor.getPanel().getWidth(), 400);
        frame.setSize(300,600);w=800;
        initTextArea("", 35, w / 11, false, mkKeyAdapter());

        // create a new pane next to the editor
        initScrollPane(new Rectangle(idePanel.editor.getScrollPane().getX()+idePanel.editor.getScrollPane().getWidth(),2,w,idePanel.editor.h));

        AST = idePanel.parser.getAST();
        if(AST==null){
            textArea.append("\n\n      Compile Error Free Code for OP codes");
        }

        symbolTable = idePanel.parser.getSemanticAnalyzer().getCurrentScope();
        curentScope=symbolTable;
        padWithZeros();
        genCode(AST.root);

        backPatch();
        printCode();
        showHide();

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                if(!idePanel.editor.analyzed()){
                    while (!idePanel.editor.analyzed())
                        Utils.wait(10);
                    codeStream = new String[256];
                    AST = idePanel.parser.getAST();
                    symbolTable = idePanel.parser.getSemanticAnalyzer().getCurrentScope();
                    curentScope=symbolTable;
                    tempVars = new ArrayList<TempVar>();
                    byteCnt = 0; tempNum=0;heap=256;
                    padWithZeros();
                    genCode(AST.root);
                    backPatch();
                    textArea.setText("");
                    printCode();
                }
            }
        },20);
    }//..

    protected void genCode(Node root){

        switch (root.getType()){
            case VARDECL:
                genVARDECL(root);
                break;
            case ASSIGNMENT_STATEMENT:
                genASSIGNMENT(root);
                break;
            case PRINT_STATEMENT:
                genPRINT_STATEMENT(root);
                break;
        }
        for (Node c: root.children){
            genCode(c);
        }

    }//..

    protected void genPRINT_STATEMENT(Node root){
        Node val = root.children.get(0);
        System.out.println("CodeGen PRINT_STATEMENT: "+val.getData());
        TempVar  tempVar;

        switch (val.getType()){
            case ID:
                Symbol symbol = curentScope.isDeclared(val);
                if("int".equals(symbol.getData())){
                    tempVar= haveTempFor(val);
                    codeStream[byteCnt++] = "AC";// load y from mem
                    codeStream[byteCnt++] = tempVar.temp1;
                    codeStream[byteCnt++] = tempVar.temp2;
                    codeStream[byteCnt++] = "A2";// load x with const
                    codeStream[byteCnt++] = "01";
                    codeStream[byteCnt++] = "FF";
                }else if("string".equals(symbol.getData())){
                    tempVar= haveTempFor(val);
                    codeStream[byteCnt++] = "AC";// load y from mem
                    codeStream[byteCnt++] = tempVar.temp1;
                    codeStream[byteCnt++] = tempVar.temp2;
                    codeStream[byteCnt++] = "A2";// load x with const
                    codeStream[byteCnt++] = "02";
                    codeStream[byteCnt++] = "FF";
                }else if("boolean".equals(symbol.getData())){

                }
                System.out.println(symbol.getData());
                break;
            case STRING:
                System.out.println("Printing a "+val.getType()+": "+val.getData() );
                addStringToHeap(val.getData().toString());
                codeStream[byteCnt++] = "A2";// load x with const
                codeStream[byteCnt++] = "02";
                codeStream[byteCnt++] = "A0";// load y with const
                codeStream[byteCnt++] = Integer.toHexString(heap);
                codeStream[byteCnt++] = "FF";// System call
                break;
        }
    }//..

    public void addStringToHeap(String string){
        int stringLen = string.length()+1;
        heap -= stringLen;
        System.out.println(heap+" "+stringLen);
        for(int i=0;i<stringLen-1;i++){
            codeStream[heap+i]=charToHex(string.charAt(i));
        }
    }//..

    public String charToHex(char c){
        return Integer.toHexString((int)c);
    }//..

    protected void   genASSIGNMENT(Node root){
        Node val = root.children.get(1),var = root.children.get(0);
        System.out.println("CodeGen ASSIGNMENT: "+var.getData()+" = "+val.getData());

        if(Utils.isInt(val.getData().toString())) {
            System.out.println("We have an int");
            codeStream[byteCnt++] = "A9";
            codeStream[byteCnt++] = "0" + val.getData();
            codeStream[byteCnt++] = "8D";
            TempVar tempVar = haveTempFor(var);
            codeStream[byteCnt++] = tempVar.temp1;
            codeStream[byteCnt++] = tempVar.temp2;

        }else if(var.getType() == TokenType.ID){
            System.out.println("We have an id");
            switch (val.getType()){
                case STRING:
                    System.out.println("assigning "+val.getData()+" to "+var.getData());
                    addStringToHeap(val.getData().toString());
                    codeStream[byteCnt++] = "A9";
                    codeStream[byteCnt++] = Integer.toHexString(heap);
                    codeStream[byteCnt++] = "8D";
                    TempVar tempVar = haveTempFor(var);
                    codeStream[byteCnt++] = tempVar.temp1;
                    codeStream[byteCnt++] = tempVar.temp2;
                    break;
                case ID:
                    tempVar = haveTempFor(var);
                    TempVar newVal = haveTempFor(val);
                    codeStream[byteCnt++] = "AD";
                    codeStream[byteCnt++] = newVal.temp1;
                    codeStream[byteCnt++] = newVal.temp2;
                    codeStream[byteCnt++] = "8D";
                    codeStream[byteCnt++] = tempVar.temp1;
                    codeStream[byteCnt++] = tempVar.temp2;
                    break;
            }
        }
    }//..

    protected void genVARDECL(Node root){

        TempVar tempVar;
        Node type = root.children.get(0),var = root.children.get(1);

        System.out.println("CodeGen: "+root.getData()+" "+type.getData()+" "+var.getData());

        if(type.getData().equals("int")){
            codeStream[byteCnt++] = "A9";
            codeStream[byteCnt++] =           codeStream[byteCnt++] = "8D";
            tempVar = haveTempFor(var);
            codeStream[byteCnt++] = tempVar.temp1;
            codeStream[byteCnt++] = tempVar.temp2;
        }else if(type.getData().equals("string")){
            tempVar=haveTempFor(var);
        }else if(type.getData().equals("boolean")){
            System.out.println("  boolean");
        }

    }//..

    protected TempVar haveTempFor(Node var){
        for (TempVar t:tempVars){
            if(t.variable.equals(var.getData()))
                return t;
        }

        System.out.println("   Creating Temp for var "+var.getData().toString()+": T"+tempNum);
        TempVar tempVar = new TempVar(tempNum++, var.getData().toString());
        tempVars.add(tempVar);
        return tempVar;
    }//..

    protected void padWithZeros(){
        for (int i=byteCnt;i<codeStream.length;i++){
            codeStream[i]="00";
        }
    }//..

    protected void printCode(){
        for (int i=0;i<codeStream.length;i++){
            if(i%8==0)
                textArea.append("\n");
            textArea.append(codeStream[i] + " ");
        }
    }//..

    protected void backPatch(){
        byteCnt++;

        for (TempVar t:tempVars){
            t.addr = Integer.toHexString(byteCnt++);
            for (int i=0;i<codeStream.length;i++){
                if(codeStream[i].equals(t.temp1)){
                    codeStream[i]=t.addr;
                    codeStream[i+1]="00";
                }
            }
        }
    }//..

    protected KeyAdapter mkKeyAdapter(){
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                keyBuffer[e.getKeyCode()]=true;

                if(keyBuffer[KeyEvent.VK_CONTROL]){// CTRL+
                    if(keyBuffer[KeyEvent.getExtendedKeyCodeForChar('c')]){
                        actionMap.get(DefaultEditorKit.copyAction);
                    }

                }//

            }//

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                keyBuffer[e.getKeyCode()]=false;
            }
        };
    }//..

}// CodeGenerator




