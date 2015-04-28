package k;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by KevAdmin on 4/28/2015.
 */
public class CodeGenerator extends ScrollableOutput{


    Tree AST;
    String [] codeStream = new String[256];
    ArrayList<TempVar> tempVars = new ArrayList<TempVar>();
    int byteCnt = 0;



    CodeGenerator (IDEPanel idePanel){
        super(idePanel);

        frame.setLocation(400, 400);
        frame.setSize(600,400);w=800;


        initTextArea("Generated 6502 OP Codes:\n", 35, w / 11, false,
                ScrollableOutput.mkKeyAdapter(keyBuffer, actionMap));

        // create a new pane next to the editor
        initScrollPane(new Rectangle(idePanel.editor.getScrollPane().getX()+idePanel.editor.getScrollPane().getWidth(),2,w,idePanel.editor.h));

        AST = idePanel.parser.getAST();
        if(AST==null){
            textArea.append("\n\n      Compile Error Free Code for OP codes");
        }

        genCode(AST.root);
        for (String s:codeStream){
            textArea.append(s+" ");
        }
        showHide();
    }//..

    protected void genCode(Node root){

        switch (root.getType()){
            case VARDECL:
                genVARDECL(root);
                break;
        }

        for (Node c: root.children){
            genCode(c);
        }

    }//..


    protected void genVARDECL(Node root){
        Node type = root.children.get(0),var = root.children.get(1);

        System.out.println("CodeGen: "+root.getData()+" "+type.getData()+" "+var.getData());

        if(type.getData().equals("int")){
            codeStream[byteCnt++] = "A9";
            codeStream[byteCnt++] = "00";
            codeStream[byteCnt++] = "8D";
            TempVar tempVar = new TempVar(tempVars.size(),var.getData().toString());
            tempVars.add(tempVar);
            codeStream[byteCnt++] = tempVar.temp;
            codeStream[byteCnt++] = tempVar.addr;
        }

    }//..


}// CodeGenerator




