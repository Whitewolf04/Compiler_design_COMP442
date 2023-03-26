package table_generator;

import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;

/*
 * List of functionalities:
 * - Type assigning for variables in function
 * - Type resolution for idnest (and partly assignOrFuncCall)
 *   Any resulting type from idnest will be assigned to the last node of this chain
 * - Type check for idnest (and partly assignOrFuncCall)
 * - Type check for indice
 */

public class TypeAssignVisitor extends Visitor{
    SymbolTable globalTable;
    SymbolTable localTable;

    public TypeAssignVisitor(SymbolTable globalTable){
        this.globalTable = globalTable;
        this.localTable = globalTable;
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("assignOrFuncCall;")){
            // Idnest resolution
            SyntaxTreeNode cur = node.getChild();
            if(!cur.checkContent("assign")){
                SyntaxTreeNode origin = node.getLeftmostSib();
                if(origin.getType().equals("integer") || origin.getType().equals("float")){
                    // Type check for primitive type

                    // Check for dot notation or function call on primitive type
                    SyntaxTreeNode temp = cur;
                    while(temp.getRightSib() != null){
                        if(temp.checkContent("dot") || temp.checkContent("exprList")){
                            OutputWriter.semanticErrWriting("Illegal use on primitive type for variable " + origin.getValue() + " in " + localTable.name);
                            node.setType("ERR@!");
                            return;
                        } else {
                            temp = temp.getRightSib();
                        }
                    }

                    // Resolve array indicing
                    if(cur.checkContent("indiceList")){
                        // Error handling if indice type-check fails
                        if(cur.getType().equals("ERR@!")){
                            node.setType("ERR@!");
                            return;
                        }

                        // Set type accordingly from the indicing list
                        cur.setType(indiceHandling(origin, cur));
                        if(cur.getType().equals("ERR@!")){
                            node.setType("ERR@!");
                            return;
                        } else {
                            System.out.println("Variable " + origin.getValue() + " in " + localTable.name + " has been set to type " + cur.getType());
                        }
                    }
                } else {
                    // Type check for objects


                }
            } else {
                // Skip if assignOrFuncCall starts with assign operator
                return;
            }
        }else if(node.checkContent("funcHead")){
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }
            if(node.getChildNum() == 3){
                // Global function scope
                localTable = this.globalTable.accessFromGlobal(node.getChild().getValue()).getLink();
            } else {
                // Member function scope
                String owner = node.getChild().getValue();
                SymbolTable classTable = this.globalTable.accessFromGlobal(owner).getLink();
                localTable = classTable.contains(funcName, node.getTableEntry().getType()).getLink();
            }
        } else if(node.checkContent("id")){
            // Avoid assigning type for id in declaration phase or in idnest function call
            SyntaxTreeNode parent = node.getParent();
            if(parent.checkContent("funcHead") || parent.checkContent("memFuncDecl") || parent.checkContent("fParams") || parent.checkContent("memberVarDecl") || parent.checkContent("idnest") || parent.checkContent("assignOrFuncCall")){
                return;
            }
            
            String varName = node.getValue();
            SymTabEntry variable = localTable.accessFromGlobal(varName);

            if(variable != null){
                // Variable is in local table
                if(variable.getKind().equals("variable") || variable.getKind().equals("parameter")){
                    // Variable is a localvar or param
                    node.setType(variable.getType());
                } else {
                    // Variable is a function
                    node.setType(variable.getReturnType());
                }
            } else {
                // Try to search in outer tables
                SymbolTable outerTable = localTable.outerTable;
                while(outerTable != null){
                    variable = outerTable.accessFromGlobal(varName);

                    if(variable != null){
                        if(variable.getKind().equals("variable") || variable.getKind().equals("parameter")){
                            // Variable is a localvar or param
                            node.setType(variable.getType());
                        } else {
                            // Variable is a function
                            node.setType(variable.getReturnType());
                        }
                        break;
                    } else{
                        outerTable = outerTable.outerTable;
                    }
                }

            }

            if(variable == null){
                OutputWriter.semanticErrWriting("Use of undefined variable! Variable name: " + varName + " in " + localTable.name);
                node.setType("ERR@!");
            }
            System.out.println("Variable " + varName + " in " + localTable.name + " has been set to type " + node.getType());
        } else if(node.checkContent("idnest")){
            // Idnest resolution

        }
    }

    private String indiceHandling(SyntaxTreeNode origin, SyntaxTreeNode indiceList){
        int indicingDimension = indiceList.getChildNum();
        int originDimension = 0;
        String originType = origin.getType();
        while(originType.indexOf(']') != -1 || originDimension <= indicingDimension){
            // Only remove dimension until out of dimension or the dimension matches the indicing
            originDimension++;
            originType = originType.substring(0, originType.lastIndexOf('['));
        }

        if(originDimension < indicingDimension){
            OutputWriter.semanticErrWriting("Indicing dimension out of bounds for variable " + origin.getValue() + " in " + localTable.name);
            return "ERR@!";
        } else {
            return originType;
        }

    }
}
