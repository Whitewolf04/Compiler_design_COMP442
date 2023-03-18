package table_generator;

import java.util.LinkedList;

import AST_generator.SyntaxTreeNode;

public class TableCreationVisitor extends Visitor{
    // LinkedList<SymTabEntry> buffer;
    SymbolTable table;

    public TableCreationVisitor(){
        // buffer = new LinkedList<SymTabEntry>();
        // Master table for the program
        table = new SymbolTable();
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("arrayOrObject")){
            //TODO: Implement here
            return;
        } else if(node.checkContent("expr")){
            if(node.getChildNum() == 1){
                node.setTableEntry(new SymTabEntry(node.getChild().getTableEntry()));
            } else if(node.getChildNum() == 3){
                SymbolTable table = new SymbolTable("expr");
                
                // First arithExpr
                SyntaxTreeNode cur = node.getChild();
                table.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());

                // Second arithExpr, skip through relOp
                cur = cur.getRightSib();
                cur = cur.getRightSib();
                table.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());

                node.setTableEntry(new SymTabEntry(null, "expr", "boolean"));
            }
        } else if(node.checkContent("classDecl")){
            SyntaxTreeNode cur = node.getChild();

            // Get ID
            String name = cur.getValue();

            // Get inherit list
            cur = cur.getRightSib();
            SyntaxTreeNode parent = cur.getChild();
            String type = name + ":";
            while(parent != null && !parent.isEpsilon()){
                type += parent.getValue() + ",";
                cur = cur.getRightSib();
            }
            type = type.substring(0, type.length()-1);

            // Loop through member declaration list
            cur = cur.getRightSib().getChild();
            SymbolTable classTable = new SymbolTable(name);
            while(cur != null && !cur.isEpsilon()){
                classTable.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(name, "class", name));
            this.table.addEntry(name, node.getTableEntry());
        } else if(node.checkContent("exprList")){
            SymbolTable exprTable = new SymbolTable();
            SyntaxTreeNode cur = node.getChild();

            while(cur != null && !cur.isEpsilon()){
                exprTable.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry("paramsList", "exprList", null, exprTable));
        } else if(node.checkContent("factor")){
            if(node.getChildNum() == 1){
                node.setTableEntry(new SymTabEntry(node.getChild().getTableEntry()));
            } else if(node.getChildNum() == 2){
                // Skip through the not, plus, or minus signs
                node.setTableEntry(new SymTabEntry(node.getChild().getRightSib().getTableEntry()));
            } else {
                SyntaxTreeNode cur = node.getChild();
                
                // get ID
                String name = cur.getValue();

                // get exprList/indiceList
                cur = cur.getRightSib();
                if(cur.checkContent("indiceList")){
                    name += "[" + cur.getTableEntry().getName() + "]";
                } else if(cur.checkContent("exprList")){
                    name += "(" + cur.getTableEntry().getName() + ")";
                } else {
                    System.out.println("ExprList/IndiceList error at Factor node!");
                }

                // get idnestList
                cur = cur.getRightSib();
                name += cur.getTableEntry().getName();

                node.setTableEntry(new SymTabEntry(name, "factor", null));
            }
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
            this.table.addEntry(node.getTableEntry().getName(), node.getTableEntry());
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
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(name, "parameter", type));
        } else if(node.checkContent("id")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "id", null));
        } else if(node.checkContent("inheritList")){
            // Skip through
            return;
        } else if(node.checkContent("intLit")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "intLit", "integer"));
        } else if(node.checkContent("termList")){
            SymbolTable table = new SymbolTable("arithExpr");
            SyntaxTreeNode cur = node.getChild();

            while(cur != null && !cur.isEpsilon()){
                table.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(null, "termList", null, table));
        }

    }
}
