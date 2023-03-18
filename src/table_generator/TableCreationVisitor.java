package table_generator;

import java.util.LinkedList;

import AST_generator.SyntaxTreeNode;

public class TableCreationVisitor extends Visitor{
    LinkedList<SymTabEntry> buffer;
    SymbolTable table;

    public TableCreationVisitor(){
        buffer = new LinkedList<SymTabEntry>();
        table = null;
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("aParams")){
            SyntaxTreeNode expr = node.getChild();

            while(expr != null && !expr.isEpsilon()){
                
            }
        }
        if(node.checkContent("id")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "id", null));
        } else if(node.checkContent("intLit")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "intLit", "integer"));
        } else if(node.checkContent("floatLit")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "floatLit", "float"));
        } else if(node.checkContent("funcDef")){
            SyntaxTreeNode funcHead = node.getChild();
            node.setTableEntry(new SymTabEntry(funcHead.getTableEntry()));

            SymbolTable table = new SymbolTable();
            // Traverse funcBody node
            SyntaxTreeNode cur = funcHead.getRightSib().getChild();
            while(cur != null && !cur.isEpsilon()){
                table.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());
                cur = cur.getRightSib();
            }

            SymbolTable paramList = funcHead.getTableEntry().getLink();
            paramList.pushToTable(table);
            funcHead.getTableEntry().setLink(null);

            node.getTableEntry().setLink(table);
        } else if(node.checkContent("funcHead")){
            String name = null;
            SyntaxTreeNode cur = node.getChild();
            if(node.getChildNum() == 2){
                // Constructor
                name = cur.toString() + "::constructor";
            } else if(node.getChildNum() == 3){
                // Global function
                name = cur.toString();
            } else if(node.getChildNum() == 4){
                // Member function
                name = cur.toString() + "::";
                // Skip through sr symbol
                cur = cur.getRightSib();
                cur = cur.getRightSib();
                // Add the name of the function
                name += cur.toString();
            }

            // Switch to fParamsList
            cur = cur.getRightSib();
            SyntaxTreeNode fParams = cur.getChild();
            SymbolTable table = new SymbolTable();
            while(fParams != null && !fParams.isEpsilon()){
                table.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());
                fParams = fParams.getRightSib();
            }

            // Swith to returnType
            cur = cur.getRightSib();
            
            node.setTableEntry(new SymTabEntry(name, "function", cur.getTableEntry().getType(), table));
        } else if(node.checkContent("fParamsList")){
            // Skip through
            return;
        } else if(node.checkContent("fParams")){
            // get ID
            SyntaxTreeNode cur = node.getChild();
            String name = cur.getTableEntry().getName();

            // get Type
            cur = cur.getRightSib();
            String type = cur.getTableEntry().getType();

            // get ArraySize
            cur = cur.getRightSib().getChild();
            while(cur != null && !cur.isEpsilon()){
                type += "[" + cur.getTableEntry().getName() + "]";
            }

            node.setTableEntry(new SymTabEntry(name, "parameter", type));
        }

    }
}
