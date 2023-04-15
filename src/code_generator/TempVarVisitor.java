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
                    CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "litval", "integer", 4, localTable.scopeSize);
                    localTable.scopeSize += 4;
                    localTable.addEntry(newEntry);
                    localTable.litval.add(newEntry);
                } else if(node.getChild().getType().equals("float")) {
                    CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "litval", "float", 8, localTable.scopeSize);
                    localTable.scopeSize += 8;
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
                CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "tempvar", type, size, localTable.scopeSize);
                localTable.scopeSize += size;
                localTable.addEntry(newEntry);
                localTable.tempvar.add(newEntry);
            } else {
                // Factor might contain idnest
                SyntaxTreeNode id = node.getChild();
                SyntaxTreeNode indiceOrExpr = id.getRightSib();
                SyntaxTreeNode idnestList = indiceOrExpr.getRightSib();

                if(indiceOrExpr.checkContent("indiceList") && idnestList.getChild().isEpsilon()){
                    if(indiceOrExpr.getChild().isEpsilon()){
                        return;
                    } else {
                        CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "tempArrVar", "integer", 4, localTable.scopeSize);
                        localTable.scopeSize += 4;
                        localTable.addEntry(newEntry);
                        localTable.tempArrVar.add(newEntry);
                    }
                }
            }
        } else if(node.checkContent("funcDef")){
            localTable.addEntry(new CodeTabEntry("link", "link", "integer", 4, localTable.scopeSize));
            localTable.scopeSize += 4;
        } else if(node.checkContent("funcHead")){
            // Get function name
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }

            // Set self variable for member functions
            if(node.getChildNum() == 3){
                // Global function scope
                localTable = this.globalTable.containsName(node.getChild().getValue()).link;
            } else {
                // Member function scope
                String owner = node.getChild().getValue();
                CodeGenTable classTable = this.globalTable.containsName(owner).link;
                localTable = classTable.containsFunction(funcName, node.getTableEntry().getType()).link;
                int objectSize = findObjectSize(owner, globalTable);
                localTable.addEntry(new CodeTabEntry("self", "classPointer", owner, objectSize, localTable.scopeSize));
                localTable.scopeSize += objectSize;
            }

            // Create moon function name
            localTable.moonName = createMoonFuncName(node, funcName);
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
                        CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "tempvar", type, size, localTable.scopeSize);
                        localTable.scopeSize += size;
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
                    } else if(cur.checkContent("addOp")){
                        CodeTabEntry newEntry = new CodeTabEntry("t"+tempVarID++, "tempvar", type, size, localTable.scopeSize);
                        localTable.scopeSize += size;
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

    private String createMoonFuncName(SyntaxTreeNode funcHeadNode, String funcName){
        String finalName = "";
        SyntaxTreeNode fParamsList = null;
        if(funcHeadNode.getChildNum() == 5){
            // Member function (not constructor)
            String className = funcHeadNode.getChild().getValue();
            fParamsList = funcHeadNode.getChild().getRightSib().getRightSib().getRightSib();
            finalName += className + "_" + funcName;
        } else if(funcHeadNode.getChildNum() == 4){
            // Member constructor
            String className = funcHeadNode.getChild().getValue();
            fParamsList = funcHeadNode.getChild().getRightSib().getRightSib().getRightSib();
            finalName += className + "_constructor";
        } else if(funcHeadNode.getChildNum() == 3){
            // Global function
            fParamsList = funcHeadNode.getChild().getRightSib();
            finalName += funcName;
        } else {
            return null;
        }

        SyntaxTreeNode fParams = fParamsList.getChild();
        while(fParams != null && !fParams.isEpsilon()){
            finalName += "_" + fParams.getChild().getRightSib().getChild().getValue();
            fParams = fParams.getRightSib();
        }

        return finalName;
    }
}
