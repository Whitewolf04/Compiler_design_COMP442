package code_generator;


import AST_generator.SyntaxTreeNode;
import table_generator.Visitor;

public class TempVarVisitor extends Visitor{
    CodeGenTable globalTable;
    CodeGenTable localTable = null;
    private int tempVarID = 0;

    public TempVarVisitor(CodeGenTable table){
        this.globalTable = table;
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("factor")){
            if(node.getType().equals("ERR@!")){
                return;
            }
            int numChild = node.getChildNum();
            if(numChild == 1){
                if(node.getChild().getType().equals("integer")){
                    localTable.scopeSize += 4;
                    CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "litval", "integer", 4, localTable.scopeSize);
                    localTable.addEntry(newEntry);
                    localTable.litval.add(newEntry);
                } else if(node.getChild().getType().equals("float")) {
                    localTable.scopeSize += 8;
                    CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "litval", "float", 8, localTable.scopeSize);
                    localTable.addEntry(newEntry);
                    localTable.litval.add(newEntry);
                } else {
                    System.out.println("Unknown type for factor! Calling from " + localTable.name);
                    return;
                }
            } else if(numChild == 2) {
                // numChild == 2
                SyntaxTreeNode factor = node.getChild().getRightSib();
                String type = null;
                int size = 0;
                if(factor.getType().equals("integer")){
                    size = 4;
                    type = "integer";
                } else if(factor.getType().equals("float")){
                    size = 8;
                    type = "float";
                } else if(factor.getType().equals("ERR@!")) {
                    return;
                } else {
                    size = findObjectSize(factor.getType(), localTable);
                    type = factor.getType();
                }
                localTable.scopeSize += size;
                CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "tempvar", type, size, localTable.scopeSize);
                localTable.addEntry(newEntry);
                localTable.tempvar.add(newEntry);
            }
        } else if(node.checkContent("funcHead")){
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }
            if(node.getChildNum() == 3){
                // Global function scope
                localTable = this.globalTable.containsName(node.getChild().getValue()).link;
            } else {
                // Member function scope
                String owner = node.getChild().getValue();
                CodeGenTable classTable = this.globalTable.containsName(owner).link;
                localTable = classTable.containsFunction(funcName, node.getTableEntry().getType()).link;
            }
        } else if(node.checkContent("term")){
            // Check if there's mult op
            if(node.getChildNum() > 2){
                SyntaxTreeNode cur = node.getChild();
                int size = 0;
                String type = null;
                if(cur.getType().equals("integer")){
                    size = 4;
                    type = "integer";
                } else if(cur.getType().equals("float")){
                    size = 8;
                    type = "float";
                } else {
                    System.out.println("Unknown type for factor! Calling from " + localTable.name);
                    return;
                }
                while(cur != null && !cur.isEpsilon()){
                    if(cur.checkContent("factor") && cur.getType().equals("ERR@!")){
                        return;
                    } else if(cur.checkContent("multOp")){
                        localTable.scopeSize += size;
                        CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "tempvar", type, size, localTable.scopeSize);
                        localTable.addEntry(newEntry);
                        localTable.tempvar.add(newEntry);
                    }
                    cur = cur.getRightSib();
                }
            }
        } else if(node.checkContent("termList")){
            if(node.getChildNum() > 2){
                SyntaxTreeNode cur = node.getChild();
                int size = 0;
                String type = null;
                if(cur.getType().equals("integer")){
                    size = 4;
                    type = "integer";
                } else if(cur.getType().equals("float")){
                    size = 8;
                    type = "float";
                } else {
                    System.out.println("Unknown type for term! Calling from " + localTable.name);
                    return;
                }
                while(cur != null && !cur.isEpsilon()){
                    if(cur.checkContent("term") && cur.getType().equals("ERR@!")){
                        return;
                    } else if(cur.checkContent("plus") || cur.checkContent("minus") || cur.checkContent("or")){
                        localTable.scopeSize += size;
                        CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "tempvar", type, size, localTable.scopeSize);
                        localTable.addEntry(newEntry);
                        localTable.tempvar.add(newEntry);
                    }
                    cur = cur.getRightSib();
                }
            }
        }
    }

    private int findObjectSize(String type, CodeGenTable localTable){
        CodeTabEntry temp = localTable.containsName(type);
        CodeGenTable curTable = localTable;
        while(temp == null){
            curTable = curTable.outerTable;
            temp = curTable.containsName(type);
        }

        return temp.size;
    }
}
