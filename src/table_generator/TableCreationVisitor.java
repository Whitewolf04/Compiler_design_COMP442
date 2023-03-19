package table_generator;


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
        if(node.checkContent("classDecl")){
            SyntaxTreeNode cur = node.getChild();

            // Get ID
            String name = cur.getValue();

            // Get inherit list
            cur = cur.getRightSib();
            SyntaxTreeNode parent = cur.getChild();
            String type = name + "::";
            while(parent != null && !parent.isEpsilon()){
                type += parent.getValue() + ",";
                cur = cur.getRightSib();
            }
            type = type.substring(0, type.length()-1);

            // Loop through member declaration list
            cur = cur.getRightSib().getChild();
            SymbolTable classTable = new SymbolTable(name);

            // Add self pointer
            classTable.addEntry("self", new SymTabEntry("self", "variable", name, classTable));
            // Add all variable and functions declared in this class
            while(cur != null && !cur.isEpsilon()){
                classTable.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(name, "class", name));
            this.table.addEntry(name, node.getTableEntry());
        } else if(node.checkContent("constructorDecl")){
            SyntaxTreeNode fParams = node.getChild().getChild();
            SymbolTable table = new SymbolTable("constructor");
            while(fParams != null && !fParams.isEpsilon()){
                table.addEntry(fParams.getTableEntry().getName(), fParams.getTableEntry());
                fParams = fParams.getRightSib();
            }

            node.setTableEntry(new SymTabEntry("constructor", "function", "void", table));
        } else if(node.checkContent("floatLit")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "floatLit", "float"));
        } else if(node.checkContent("funcBody")){
            // Skip through
            return;
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
                if(cur.getValue() == null){
                    type += "[" + "]";
                } else {
                    type += "[" + cur.getValue() + "]";
                }
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
        } else if(node.checkContent("memberDecl")){
            // Get visibility
            SyntaxTreeNode cur = node.getChild();
            boolean visibility = cur.getTableEntry().getVisibility();

            // get member declare
            cur = cur.getRightSib();
            node.setTableEntry(new SymTabEntry(cur.getTableEntry()));
            node.getTableEntry().setVisibility(visibility ? "public" : "private");
        } else if(node.checkContent("memberFuncDecl")){
            SyntaxTreeNode cur = node.getChild();

            // Get ID
            String name = cur.getValue();

            // Get fParamsList
            cur = cur.getRightSib();
            SyntaxTreeNode fParams = cur.getChild();
            SymbolTable table = new SymbolTable(name);
            while(fParams != null && !fParams.isEpsilon()){
                table.addEntry(fParams.getTableEntry().getName(), fParams.getTableEntry());
                fParams = fParams.getRightSib();
            }

            // Get returnType
            cur = cur.getRightSib();
            String type = cur.getTableEntry().getType();

            node.setTableEntry(new SymTabEntry(name, "function", type, table));

        } else if(node.checkContent("memberVarDecl")){
            // Get ID
            SyntaxTreeNode cur = node.getChild();
            String name = cur.getValue();

            // Get type
            cur = cur.getRightSib();
            String type = cur.getTableEntry().getType();

            // Get array size list
            cur = cur.getRightSib().getChild();
            while(cur != null && !cur.isEpsilon()){
                if(cur.getValue() == null){
                    type += "[" + "]";
                } else {
                    type += "[" + cur.getValue() + "]";
                }
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(name, "variable", type));
        } else if(node.checkContent("termList")){
            SymbolTable table = new SymbolTable("arithExpr");
            SyntaxTreeNode cur = node.getChild();

            while(cur != null && !cur.isEpsilon()){
                table.addEntry(cur.getTableEntry().getName(), cur.getTableEntry());
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(null, "termList", null, table));
        } else if(node.checkContent("type")){
            SyntaxTreeNode cur = node.getChild();
            String type = null;
            if(cur.checkContent("id")){
                type = cur.getValue();
            } else {
                type = cur.toString();
            }

            node.setTableEntry(new SymTabEntry(null, "type", type));
        } else if(node.checkContent("visibility")){
            SyntaxTreeNode cur = node.getChild();
            node.setTableEntry(new SymTabEntry(null, "visibility", null));
            node.getTableEntry().setVisibility(cur.toString());
        }

    }
}
