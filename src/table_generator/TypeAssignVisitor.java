package table_generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;

/*
 * List of functionalities:
 * - Type assigning for variables in function
 * - Type resolution for idnest (and partly assignOrFuncCall)
 *   Any resulting type from idnest will be assigned to the last node of this chain
 * - Type check for idnest (and partly assignOrFuncCall)
 * - Type check for indice
 * - Param check for idnest calls
 * - Aggregate type to factor
 */

public class TypeAssignVisitor extends Visitor{
    SymbolTable globalTable;
    SymbolTable localTable;

    public TypeAssignVisitor(SymbolTable globalTable){
        this.globalTable = globalTable;
        this.localTable = globalTable;
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("assignOrFuncCall")){
            // Idnest resolution
            SyntaxTreeNode cur = node.getChild();
            if(!cur.checkContent("assign")){
                SyntaxTreeNode origin = node.getLeftmostSib();
                String typeBuffer = null;
                SyntaxTreeNode idBuffer = origin;

                while(cur != null && !cur.checkContent("assign")){
                    if(cur.checkContent("dot")){
                        idBuffer = null;
                    } else if(cur.checkContent("indiceList") || cur.checkContent("exprList")){
                        typeBuffer = idnestResolution(idBuffer, cur, typeBuffer);
                    } else if(cur.checkContent("id")){
                        // Check for mem-var call
                        if(cur.getRightSib().checkContent("dot") || cur.getRightSib().checkContent("assign")){
                            SymTabEntry idEntry = findVariable(cur.getValue());

                            if(idEntry == null){
                                OutputWriter.semanticErrWriting("ERROR: Use of undefined variable " + cur.getValue() + " on line " + cur.getLineCount());
                                typeBuffer = "ERR@!";
                            } else if(idEntry.getKind().equals("function") || idEntry.getKind().equals("class")){
                                OutputWriter.semanticErrWriting("ERROR: Illegal use of variable " + cur.getValue() + " on line " + cur.getLineCount());
                                typeBuffer = "ERR@!";
                            } else {
                                typeBuffer = idEntry.getType();
                            }
                            cur.setType(typeBuffer);
                            
                            // Reset idBuffer
                            idBuffer = null;
                        } else {
                            idBuffer = cur;
                        }
                    }

                    // Set type on the last node
                    if(cur.getRightSib() != null && cur.getRightSib().checkContent("assign")){
                        cur.setType(typeBuffer);
                    }

                    // Move on to the next node
                    cur = cur.getRightSib();
                }
            } else {
                // Skip if assignOrFuncCall starts with assign operator
                return;
            }
        } else if(node.checkContent("factor")){
            int numChild = node.getChildNum();
            if(numChild == 1){
                node.setType(node.getChild().getType());
            } else if(numChild == 2){
                node.setType(node.getChild().getRightSib().getType());
            } else {
                // factor containing id
                SyntaxTreeNode id = node.getChild();
                SyntaxTreeNode indiceOrExpr = id.getRightSib();
                SyntaxTreeNode idnestList = indiceOrExpr.getRightSib();

                if(!idnestList.getChild().isEpsilon()){
                    node.setType(idnestList.getType());
                } else {
                    String typeBuffer = idnestResolution(id, indiceOrExpr, null);
                    node.setType(typeBuffer);
                }
            }
        } else if(node.checkContent("funcHead")){
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }
            if(node.getChildNum() == 3){
                // Global function scope
                localTable = this.globalTable.containsName(node.getChild().getValue()).getLink();
            } else {
                // Member function scope
                String owner = node.getChild().getValue();
                SymbolTable classTable = this.globalTable.containsName(owner).getLink();
                SymTabEntry funcEntry = classTable.containsFunction(funcName, node.getTableEntry().getType());

                if(funcEntry == null){
                    OutputWriter.semanticErrWriting("ERROR: Function " + funcName + " with type " + node.getTableEntry().getType() + " undeclared in class " + classTable.name + ", line " + node.getLineCount());
                }
                localTable = funcEntry.getLink();
            }
        } else if(node.checkContent("id")){
            // Avoid assigning type for id in declaration phase or in idnest function call
            SyntaxTreeNode parent = node.getParent();
            if(parent.checkContent("funcHead") || parent.checkContent("memFuncDecl") || parent.checkContent("fParams") || parent.checkContent("memberVarDecl") || parent.checkContent("idnest") || parent.checkContent("assignOrFuncCall")){
                return;
            }
            
            String varName = node.getValue();
            SymTabEntry variable = findVariable(varName);

            if(variable == null){
                OutputWriter.semanticErrWriting("ERROR: Use of undefined variable! Variable name: " + varName + " on line " + node.getLineCount());
                node.setType("ERR@!");
                return;
            } else {
                if(variable.getKind().equals("function")){
                    node.setType(variable.getReturnType());
                } else if(variable.getKind().equals("variable") || variable.getKind().equals("parameter")){
                    node.setType(variable.getType());
                }
            }
        } else if(node.checkContent("idnestList")){
            // Idnest resolution
            if(node.getChild().isEpsilon()){
                return;
            }

            // Get the origin id
            SyntaxTreeNode origin = node.getLeftmostSib();
            if(origin.getType().equals("ERR@!")){
                node.setType("ERR@!");
                return;
            }

            // Check if origin type is compatible for idnest calls
            String typeBuffer = null;
            if(origin.getRightSib().checkContent("indiceList") || origin.getRightSib().checkContent("exprList")){
                typeBuffer = idnestResolution(origin, origin.getRightSib(), typeBuffer);
            } else {
                typeBuffer = origin.getType();
            }

            SyntaxTreeNode idnest = node.getChild();
            while(idnest != null && !idnest.isEpsilon()){
                SyntaxTreeNode id = idnest.getChild().getRightSib();
                SyntaxTreeNode indiceOrExpr = id.getRightSib();

                typeBuffer = idnestResolution(id, indiceOrExpr, typeBuffer);
                
                // Move to the next idnest
                idnest = idnest.getRightSib();
            }
            node.setType(typeBuffer);
        } else if(node.checkContent("indice")){
            SyntaxTreeNode leafNode = node.getChild();

            // Traverse down to factor
            while(leafNode != null && !leafNode.checkContent("factor")){
                leafNode = leafNode.getChild();
            }

            // Check if this indice is of the right type
            if(!leafNode.getType().equals("integer")){
                OutputWriter.semanticErrWriting("ERROR: Illegal type for indexing array on line " + node.getLineCount());
            }
        } else if (node.checkContent("localVarDecl")){
            SyntaxTreeNode id = node.getChild();
            SyntaxTreeNode type = id.getRightSib();
            SyntaxTreeNode indiceOrExpr = type.getRightSib().getChild();

            if(type.getChild().checkContent("id") && indiceOrExpr.checkContent("arraySizeList") && indiceOrExpr.getChild().isEpsilon()){
                // Raise error for object declaration with no constructor call
                OutputWriter.semanticErrWriting("ERROR: Object " + id.getValue() + " was declared without constructor call from class " + type.getChild().getValue() + ", line " + id.getLineCount());
            } else if(indiceOrExpr.checkContent("exprList")){
                String typeBuffer = idnestResolution(type.getChild(), indiceOrExpr, null);

                if(typeBuffer.equals("ERR@!")){
                    id.setType("ERR@!");
                }
            }
        } else if(node.checkContent("variable")){
            SyntaxTreeNode id = node.getChild();
            SyntaxTreeNode indiceOrExpr = id.getRightSib();
            SyntaxTreeNode varIdnest = indiceOrExpr.getRightSib();
            String typeBuffer = idnestResolution(id, indiceOrExpr, null);

            // Loop through idnest element
            SyntaxTreeNode idnestNode = varIdnest.getChild();
            SyntaxTreeNode idBuffer = null;
            while(idnestNode != null && !idnestNode.isEpsilon()){
                if(idnestNode.checkContent("dot")){
                    // Reset idBuffer when move to the next idnest element
                    idBuffer = null;
                } else if (idnestNode.checkContent("exprList") || idnestNode.checkContent("indiceList")){
                    typeBuffer = idnestResolution(idBuffer, idnestNode, typeBuffer);
                } else {
                    // Check for mem-var call
                    if(idnestNode.getRightSib().checkContent("dot") || idnestNode.getRightSib().checkContent("EPSILON")){
                        SymTabEntry idEntry = findVariable(idnestNode.getValue());

                        if(idEntry == null){
                            OutputWriter.semanticErrWriting("ERROR: Use of undefined variable " + idnestNode.getValue() + " on line " + idnestNode.getLineCount());
                            typeBuffer = "ERR@!";
                        } else if(idEntry.getKind().equals("function") || idEntry.getKind().equals("class")){
                            OutputWriter.semanticErrWriting("ERROR: Illegal use of variable " + idnestNode.getValue() + " on line " + idnestNode.getLineCount());
                            typeBuffer = "ERR@!";
                        } else {
                            typeBuffer = idEntry.getType();
                        }
                        idnestNode.setType(typeBuffer);
                        
                        // Reset idBuffer
                        idBuffer = null;
                    } else {
                        idBuffer = idnestNode;
                    }
                }

                idnestNode = idnestNode.getRightSib();
            }
        }
    }

    private String indiceHandling(SyntaxTreeNode origin, SyntaxTreeNode indiceList){
        int indicingDimension = indiceList.getChildNum()-1;
        int originDimension = 0;
        String originType = origin.getType();
        while(originType.indexOf(']') != -1 && originDimension < indicingDimension){
            // Only remove dimension until out of dimension or the dimension matches the indicing
            ++originDimension;
            originType = originType.substring(0, originType.indexOf('[')) + originType.substring(originType.indexOf(']')+1, originType.length());
        }

        if(originDimension < indicingDimension){
            OutputWriter.semanticErrWriting("Indicing dimension out of bounds for variable " + origin.getValue() + " on line " + origin.getLineCount());
            return "ERR@!";
        } else {
            return originType;
        }

    }

    private boolean arrayIdentifier(String type){
        Pattern pattern = Pattern.compile("\\A.*(\\[.*\\])+\\Z");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()){
            return true;
        } else {
            return false;
        }
    }

    private SymTabEntry findVariable(String name){
        SymTabEntry variable = localTable.containsName(name);

        if(variable != null){
            // Variable is in local table
            return variable;
        } else {
            // Try to search in outer tables
            SymbolTable outerTable = localTable.outerTable;
            while(outerTable != null){
                variable = outerTable.containsName(name);
                if(variable != null){
                    return variable;
                }
                outerTable = outerTable.outerTable;
            }

        }
        // Return null if variable not found
        return variable;
    }

    private SymTabEntry findVariableIn(String varName, String paramTypes, SymbolTable table){
        if(paramTypes == null){
            // variable is ID, param or object
            SymTabEntry varEntry = table.containsName(varName);

            if(varEntry == null || varEntry.getKind().equals("function")){
                return null;
            }
            return varEntry;
        } else {
            // variable is function
            SymTabEntry funcEntry = table.containsParams(varName, paramTypes);

            if(funcEntry == null){
                return null;
            }
            return funcEntry;
        }
    }

    private SymbolTable findClass(String name){
        SymTabEntry classTableEntry = globalTable.containsName(name);

        if(classTableEntry == null){
            return null;
        } else if(!classTableEntry.getKind().equals("class")){
            return null;
        } else {
            return classTableEntry.getLink();
        }
    }

    /*
     * Desc: Utility function that can check array type and function call
     * Input: Name of function/array variable (id), exprList or indiceList, previous type buffer if applied
     * Return: Type after resolved. Return ERR@! if resolution fail
     */
    private String idnestResolution(SyntaxTreeNode id, SyntaxTreeNode indiceOrExpr, String typeBuffer){
        if(typeBuffer == null){
            SymTabEntry idEntry = findVariable(id.getValue());
            if(indiceOrExpr.checkContent("indiceList")){
                // array or id
                String idType = null;

                // Get the type for id
                if(idEntry == null){
                    OutputWriter.semanticErrWriting("ERROR: Use of undeclared variable " + id.getValue() + " on line " + id.getLineCount());
                    return "ERR@!";
                } else if(idEntry.getKind().equals("function")){
                    OutputWriter.semanticErrWriting("ERROR: Illegal use of function " + id.getValue() + " on line " + id.getLineCount());
                    id.setType("ERR@!");
                    return "ERR@!";
                } else {
                    idType = idEntry.getType();
                    id.setType(idType);
                }

                if(indiceOrExpr.getChild().isEpsilon()){
                    // ID
                    return idType;
                } else {
                    // array
                    return indiceHandling(id, indiceOrExpr);
                }
            } else {
                // function call
                String paramTypes = paramTypesUnpack(indiceOrExpr);
                String funcType = "";

                // Find this function
                SymbolTable curTable = localTable;
                SymTabEntry funcEntry = findVariableIn(id.getValue(), paramTypes, curTable);
                while(funcEntry == null && curTable.outerTable != null){
                    curTable = curTable.outerTable;
                    funcEntry = findVariableIn(id.getValue(), paramTypes, curTable);
                }

                // Check if this function is a constructor
                if(funcEntry == null){
                    SymTabEntry classEntry = findVariableIn(id.getValue(), null, globalTable);
                    if(classEntry != null && classEntry.getKind().equals("class")){
                        SymbolTable classTable = classEntry.getLink();

                        funcEntry = findVariableIn("constructor", paramTypes, classTable);
                    }
                }

                if(funcEntry == null){
                    OutputWriter.semanticErrWriting("ERROR: Use of undeclared function " + id.getValue() + " with param types (" + paramTypes + ") on line " + id.getLineCount());
                    id.setType("ERR@!");
                    return "ERR@!";
                } else if(!funcEntry.isPublic()){
                    OutputWriter.semanticErrWriting("ERROR: Use of private function " + id.getValue() + " with param types (" + paramTypes + ") on line " + id.getLineCount());
                    id.setType("ERR@!");
                    return "ERR@!";
                } else {
                    funcType = funcEntry.getReturnType();
                    id.setType(funcType);
                }

                return funcType;

            }
        } else if(typeBuffer.equals("ERR@!")){
            return typeBuffer;
        } else if(arrayIdentifier(typeBuffer)){
            OutputWriter.semanticErrWriting("ERROR: Idnest call for array type on line " + id.getLineCount());
            return "ERR@!";
        } else if(typeBuffer.equals("integer") || typeBuffer.equals("float")){
            OutputWriter.semanticErrWriting("ERROR: Idnest call for primitive type on line " + id.getLineCount());
            return "ERR@!";
        }

        // Get the class table of the current object
        SymbolTable classTable = findClass(typeBuffer);
        
        if(indiceOrExpr.checkContent("indiceList")){
            // array or id
            SymTabEntry idEntry = findVariableIn(id.getValue(), null, classTable);
            String idType = null;

            // Get the type for id
            if(idEntry == null){
                OutputWriter.semanticErrWriting("ERROR: Variable " + id.getValue() + " not found in class " + classTable.name + ", line " + id.getLineCount());
                id.setType("ERR@!");
                return "ERR@!";
            } else if(!idEntry.isPublic() && !localTable.inClass(classTable)){
                OutputWriter.semanticErrWriting("ERROR: Variable " + id.getValue() + " is private in class " + classTable.name + ", line " + id.getLineCount());
                id.setType("ERR@!");
                return "ERR@!";
            } else {
                idType = idEntry.getType();
                id.setType(idType);
            }

            if(indiceOrExpr.getChild().isEpsilon()){
                // ID
                return idType;
            } else {
                // array
                return indiceHandling(id, indiceOrExpr);
            }
        } else {
            // function call
            String paramTypes = paramTypesUnpack(indiceOrExpr);
            SymTabEntry funcEntry = findVariableIn(id.getValue(), paramTypes, classTable);
            String idType = null;

            if(funcEntry == null){
                OutputWriter.semanticErrWriting("ERROR: Function " + id.getValue() + " with param types (" + paramTypes + ") not found in class " + classTable.name + ", line " + id.getLineCount());
                id.setType("ERR@!");
                return "ERR@!";
            } else if(!funcEntry.isPublic() && localTable.inClass(classTable)){
                OutputWriter.semanticErrWriting("ERROR: Function " + id.getValue() + " with param types (" + paramTypes + ") is private in class " + classTable.name + ", line " + id.getLineCount());
                id.setType("ERR@!");
                return "ERR@!";
            } else {
                idType = funcEntry.getReturnType();
                id.setType(idType);
            }

            return idType;
        }
    }

    private String paramTypesUnpack(SyntaxTreeNode exprList){
        SyntaxTreeNode param = exprList.getChild();
        String paramTypes = "";
        while(param != null && !param.isEpsilon()){
            SyntaxTreeNode leafNode = param.getChild();
            while(leafNode.getChild() != null && !leafNode.checkContent("factor")){
                leafNode = leafNode.getChild();
            }
            paramTypes += leafNode.getType() + ",";

            param = param.getRightSib();
        }
        if(!paramTypes.isEmpty()){
            paramTypes = paramTypes.substring(0, paramTypes.length()-1);
        }

        return paramTypes;
    }
}
