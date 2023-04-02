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
                if(origin.getType().equals("ERR@!")){
                    node.setType("ERR@!");
                    return;
                } else if(origin.getType().equals("integer") || origin.getType().equals("float")){
                    // Type check for primitive type

                    // Check for dot notation, list indicing, or function call on primitive type
                    SyntaxTreeNode temp = cur;
                    while(temp.getRightSib() != null && !temp.checkContent("assign")){
                        if(temp.checkContent("dot") || temp.checkContent("exprList") || temp.checkContent("indiceList")){
                            OutputWriter.semanticErrWriting("Illegal use on primitive type for variable " + origin.getValue() + " in " + localTable.name);
                            node.setType("ERR@!");
                            return;
                        } else {
                            temp = temp.getRightSib();
                        }
                    }
                } else if(primitiveArrayIdentifier(origin.getType())){
                    // Type check for primitive type arrays
                    
                    // Check for dot notation or function call on primitive type array
                    SyntaxTreeNode temp = cur;
                    while(temp.getRightSib() != null && !temp.checkContent("assign")){
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
                            // System.out.println("Variable " + origin.getValue() + " in " + localTable.name + " has been set to type " + cur.getType());
                        }
                    }
                } else if(objectArrayIdentifier(origin.getType())) {
                    // Type check for object array
                    String typeBuffer = null;
                    
                    // Error handling the illegal func call or variable call
                    if(cur.checkContent("exprList") || cur.checkContent("dot")){
                        OutputWriter.semanticErrWriting("Illegal function/variable call on object array for variable " + origin.getValue() + " in " + localTable.name);
                        node.setType("ERR@!");
                        return;
                    } else if(cur.checkContent("indiceList")){
                        // Indice resolution

                        // Error handling if indice type-check fails
                        if(cur.getType().equals("ERR@!")){
                            node.setType("ERR@!");
                            return;
                        }

                        // Set type accordingly from the indicing list
                        cur.setType(indiceHandling(origin, cur));
                        typeBuffer = cur.getType();
                        if(typeBuffer.equals("ERR@!")){
                            node.setType("ERR@!");
                            return;
                        } else {
                            // System.out.println("Variable " + origin.getValue() + " in " + localTable.name + " has been set to type " + cur.getType());
                        }
                    }

                    // Go through idnest list
                    cur = cur.getRightSib();
                    SymbolTable classTable = this.globalTable.containsName(origin.getType().substring(0, origin.getType().indexOf('['))).getLink();
                    SymTabEntry idBuffer = null;
                    while(cur.getRightSib() != null && !cur.checkContent("assign")){
                        if(cur.checkContent("dot")){
                            // Skip through dot symbol
                            if(typeBuffer != null){
                                // Return type can only be primitive type or an object type
                                if(typeBuffer.equals("integer") || typeBuffer.equals("float")){
                                    OutputWriter.semanticErrWriting("Illegal use on primitive type for variable returned by " + idBuffer.getName() + " in " + localTable.name);
                                    node.setType("ERR@!");
                                    return;
                                } else if(objectArrayIdentifier(typeBuffer)){
                                    OutputWriter.semanticErrWriting("Illegal function/variable call on object array for variable " + origin.getValue() + " in " + localTable.name);
                                    node.setType("ERR@!");
                                    return;
                                } else {
                                    classTable = this.globalTable.containsName(typeBuffer).getLink();
                                }
                            }
                            idBuffer = null;
                            cur = cur.getRightSib();
                            continue;
                        } else if(cur.checkContent("id")){
                            SymTabEntry variable = classTable.containsName(cur.getValue());
                            if(variable == null){
                                OutputWriter.semanticErrWriting("Illegal function/variable call on object array for variable " + cur.getValue() + " in " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }
                            idBuffer = variable;
                            typeBuffer = variable.getReturnType();
                        } else if(cur.checkContent("exprList")){
                            // Check if we are function calling on a variable
                            if(idBuffer.getKind().equals("variable") || idBuffer.getKind().equals("parameter")){
                                OutputWriter.semanticErrWriting("ERROR: Unable to function call on a variable name for variable " + idBuffer.getName() + " in " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }

                            // Get all the types of parameter
                            String paramTypes = "";
                            SyntaxTreeNode param = cur.getChild();
                            while(param != null && !param.isEpsilon()){
                                SyntaxTreeNode leafNode = param.getChild();
                                while(leafNode.getChild() != null){
                                    leafNode = leafNode.getChild();
                                }
                                paramTypes += leafNode.getType() + ",";

                                param = param.getRightSib();
                            }
                            paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                            String functionType = typeBuffer + ":" + paramTypes;

                            // Check for param type
                            SymTabEntry function = classTable.containsFunction(idBuffer.getName(), functionType);
                            if(function == null){
                                OutputWriter.semanticErrWriting("ERROR: Function " + idBuffer.getName() + " with type " + functionType + " doesn't exist in " + classTable.name + ", calling from " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }
                        } else if(cur.checkContent("indiceList")){
                            // Has to be a variable with array-type
                            if(idBuffer.getKind().equals("function") || idBuffer.getKind().equals("parameter") || !primitiveArrayIdentifier(idBuffer.getType())){
                                OutputWriter.semanticErrWriting("ERROR: Unable to indice a non-array variable: " + idBuffer.getName() + " in " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }

                            // Indice resolution
                            // Error handling if indice type-check fails
                            if(cur.getType().equals("ERR@!")){
                                node.setType("ERR@!");
                                return;
                            }

                            // Set type accordingly from the indicing list
                            typeBuffer = indiceHandling(origin, cur);
                            if(typeBuffer.equals("ERR@!")){
                                node.setType("ERR@!");
                                return;
                            }
                        } else {
                            System.out.println("Unexpected token " + cur.toString() + " in assignOrFuncCall subtree");
                        }
                        cur = cur.getRightSib();
                    }
                } else {
                    // Type check for object
                    SymbolTable classTable = null;
                    SymTabEntry funcOrObj = this.globalTable.containsName(origin.getType());
                    if(funcOrObj != null){
                        // This is an object
                        classTable = funcOrObj.getLink();
                    } else {
                        funcOrObj = this.globalTable.containsName(origin.getValue());
                        if(funcOrObj != null){
                            // This is a global function
                            classTable = this.globalTable;
                        } else {
                            OutputWriter.semanticErrWriting("Use of undefined variable: " + origin.getValue() + " in " + localTable.name);
                            node.setType("ERR@!");
                            return;
                        }
                    }
                    SymTabEntry idBuffer = findVariable(origin.getValue());
                    String typeBuffer = origin.getType();
                    while(cur.getRightSib() != null && !cur.checkContent("assign")){
                        if(cur.checkContent("dot")){
                            // Skip through dot symbol
                            if(typeBuffer != null){
                                // Return type can only be primitive type or an object type
                                if(typeBuffer.equals("integer") || typeBuffer.equals("float")){
                                    OutputWriter.semanticErrWriting("Illegal use on primitive type for variable returned by " + idBuffer.getName() + " in " + localTable.name);
                                    node.setType("ERR@!");
                                    return;
                                } else if(objectArrayIdentifier(typeBuffer)){
                                    OutputWriter.semanticErrWriting("Illegal function/variable call on object: " + origin.getValue() + " in " + localTable.name);
                                    node.setType("ERR@!");
                                    return;
                                } else {
                                    classTable = this.globalTable.containsName(typeBuffer).getLink();
                                }
                            }
                            idBuffer = null;
                            cur = cur.getRightSib();
                            continue;
                        } else if(cur.checkContent("id")){
                            SymTabEntry variable = classTable.containsName(cur.getValue());
                            if(variable == null){
                                OutputWriter.semanticErrWriting("Illegal function/variable call on object: " + cur.getValue() + " in " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }
                            idBuffer = variable;
                            typeBuffer = variable.getReturnType();
                        } else if(cur.checkContent("exprList")){
                            // Check if we are function calling on a variable
                            if(idBuffer.getKind().equals("variable") || idBuffer.getKind().equals("parameter")){
                                OutputWriter.semanticErrWriting("ERROR: Unable to function call on a variable name for variable " + idBuffer.getName() + " in " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }

                            // Get all the types of parameter
                            String paramTypes = "";
                            SyntaxTreeNode param = cur.getChild();
                            while(param != null && !param.isEpsilon()){
                                SyntaxTreeNode leafNode = param.getChild();
                                while(leafNode.getChild() != null){
                                    leafNode = leafNode.getChild();
                                }
                                paramTypes += leafNode.getType() + ",";

                                param = param.getRightSib();
                            }
                            if(!paramTypes.isEmpty()){
                                paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                            }
                            String functionType = typeBuffer + ":" + paramTypes;

                            // Check for param type
                            SymTabEntry function = classTable.containsFunction(idBuffer.getName(), functionType);
                            if(function == null){
                                OutputWriter.semanticErrWriting("ERROR: Function " + idBuffer.getName() + " with type " + functionType + " doesn't exist in " + classTable.name + ", calling from " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }
                        } else if(cur.checkContent("indiceList")){
                            // Has to be a variable with array-type
                            if(idBuffer.getKind().equals("function") || idBuffer.getKind().equals("parameter") || !primitiveArrayIdentifier(idBuffer.getType())){
                                OutputWriter.semanticErrWriting("ERROR: Unable to indice a non-array variable: " + idBuffer.getName() + " in " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            }

                            // Indice resolution
                            // Error handling if indice type-check fails
                            if(cur.getType().equals("ERR@!")){
                                node.setType("ERR@!");
                                return;
                            }

                            // Set type accordingly from the indicing list
                            typeBuffer = indiceHandling(origin, cur);
                            if(typeBuffer.equals("ERR@!")){
                                node.setType("ERR@!");
                                return;
                            }
                        } else {
                            System.out.println("Unexpected token " + cur.toString() + " in assignOrFuncCall subtree");
                        }
                        cur = cur.getRightSib();
                    }
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
                } else if(!indiceOrExpr.getChild().isEpsilon()){
                    if(indiceOrExpr.checkContent("indiceList")){
                        node.setType(indiceHandling(id, indiceOrExpr));
                    } else {
                        SymTabEntry functionOrClass = findVariable(id.getValue());
                        if(functionOrClass == null){
                            OutputWriter.semanticErrWriting("ERROR: Use of undeclared variable: " + id.getValue() + " in " + localTable.name);
                            node.setType("ERR@!");
                            return;
                        } else if(functionOrClass.getKind().equals("variable") || functionOrClass.getKind().equals("parameter")){
                            OutputWriter.semanticErrWriting("ERROR: Unable to function call on variable: " + id.getValue() + " in " + localTable.name);
                            node.setType("ERR@!");
                            return;
                        } else if(functionOrClass.getKind().equals("class")){
                            node.setType(id.getType());
                        } else {
                            // Origin is a function call, has to be global function
                            // Get all param types
                            String paramTypes = "";
                            SyntaxTreeNode param = id.getRightSib().getChild();
                            while(param != null && !param.isEpsilon()){
                                SyntaxTreeNode leafNode = param.getChild();
                                while(leafNode.getChild() != null){
                                    leafNode = leafNode.getChild();
                                }
                                paramTypes += leafNode.getType() + ",";

                                param = param.getRightSib();
                            }
                            if(!paramTypes.isEmpty()){
                                paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                            }

                            // Find the right function
                            functionOrClass = this.globalTable.containsFunction(id.getValue(), paramTypes);
                            if(functionOrClass == null){
                                OutputWriter.semanticErrWriting("ERROR: There is no global function: " + id.getValue() + " that has these parameters: " + paramTypes + ", calling from " + localTable.name);
                                node.setType("ERR@!");
                                return;
                            } else {
                                node.setType(functionOrClass.getReturnType());
                            }
                        }
                    }
                } else {
                    node.setType(id.getType());
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
                localTable = classTable.containsFunction(funcName, node.getTableEntry().getType()).getLink();
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
                OutputWriter.semanticErrWriting("Use of undefined variable! Variable name: " + varName + " in " + localTable.name);
                node.setType("ERR@!");
                return;
            } else {
                if(variable.getKind().equals("function")){
                    node.setType(variable.getReturnType());
                } else if(variable.getKind().equals("variable") || variable.getKind().equals("parameter")){
                    node.setType(variable.getType());
                }
                // System.out.println("Variable " + varName + " in " + localTable.name + " has been set to type " + node.getType());
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
            if(origin.getRightSib().checkContent("indiceList")){
                if(origin.getRightSib().isEpsilon()){
                    typeBuffer = origin.getType();
                } else {
                    typeBuffer = indiceHandling(origin, origin.getRightSib());
                }
            } else {
                // exprList
                SymTabEntry functionOrClass = findVariable(origin.getValue());
                if(functionOrClass == null){
                    OutputWriter.semanticErrWriting("ERROR: Use of undeclared variable: " + origin.getValue() + " in " + localTable.name);
                    node.setType("ERR@!");
                    return;
                } else if(functionOrClass.getKind().equals("variable") || functionOrClass.getKind().equals("parameter")){
                    OutputWriter.semanticErrWriting("ERROR: Unable to function call on variable: " + origin.getValue() + " in " + localTable.name);
                    node.setType("ERR@!");
                    return;
                } else if(functionOrClass.getKind().equals("class")){
                    typeBuffer = origin.getType();
                } else {
                    // Origin is a function call, has to be global function
                    // Get all param types
                    String paramTypes = "";
                    SyntaxTreeNode param = origin.getRightSib().getChild();
                    while(param != null && !param.isEpsilon()){
                        SyntaxTreeNode leafNode = param.getChild();
                        while(leafNode.getChild() != null){
                            leafNode = leafNode.getChild();
                        }
                        paramTypes += leafNode.getType() + ",";

                        param = param.getRightSib();
                    }
                    if(!paramTypes.isEmpty()){
                        paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                    }

                    // Find the right function
                    functionOrClass = this.globalTable.containsFunction(origin.getValue(), paramTypes);
                    if(functionOrClass == null){
                        OutputWriter.semanticErrWriting("ERROR: There is no global function: " + origin.getValue() + " that has these parameters: " + paramTypes + ", calling from " + localTable.name);
                        node.setType("ERR@!");
                        return;
                    } else {
                        typeBuffer = functionOrClass.getReturnType();
                    }
                }
                
            }
            if(objectArrayIdentifier(typeBuffer) || typeBuffer.equals("integer") || typeBuffer.equals("float")){
                OutputWriter.semanticErrWriting("ERROR: Unable to function call on a variable of array/primitive type: " + origin.getValue() + " in " + localTable.name);
                node.setType("ERR@!");
                return;
            }

            // Idnest calls
            SyntaxTreeNode cur = node.getChild();
            SymbolTable classTable = null;
            SymTabEntry funcOrObj = this.globalTable.containsName(typeBuffer);
            if(funcOrObj != null){
                // This is an object
                classTable = funcOrObj.getLink();
            } else {
                funcOrObj = this.globalTable.containsName(origin.getValue());
                if(funcOrObj != null){
                    // This is a function
                    classTable = funcOrObj.getLink();
                } else {
                    OutputWriter.semanticErrWriting("Use of undefined variable: " + origin.getValue() + " in " + localTable.name);
                    node.setType("ERR@!");
                    return;
                }
            }
            while(cur != null && !cur.isEpsilon()){
                SyntaxTreeNode id = cur.getChild().getRightSib();
                SyntaxTreeNode indiceOrExpr = id.getRightSib();

                // Check if previous type is compatible for idnest calls
                if(objectArrayIdentifier(typeBuffer) || typeBuffer.equals("void") || typeBuffer.equals("integer") || typeBuffer.equals("float")){
                    OutputWriter.semanticErrWriting("ERROR: Unable to function call on variable: " + origin.getValue() + " of type " + typeBuffer + " in " + localTable.name);
                    node.setType("ERR@!");
                    return;
                } else if(typeBuffer.equals("ERR@!")){
                    // Don't throw double error
                    node.setType("ERR@!");
                    return;
                }

                // Check to see if id is in the class table
                funcOrObj = classTable.containsName(id.getValue());
                if(funcOrObj == null){
                    OutputWriter.semanticErrWriting("Use of undefined variable: " + id.getValue() + " in " + localTable.name);
                    node.setType("ERR@!");
                    return;
                }

                // Check on the kind of variable
                if(funcOrObj.getKind().equals("variable")){
                    if(indiceOrExpr.checkContent("exprList")){
                        OutputWriter.semanticErrWriting("ERROR: Passing parameter to a non-function variable: " + id.getValue() + " in " + localTable.name);
                        node.setType("ERR@!");
                        return;
                    } else {
                        // indiceOrExpr is indiceList which can contain EPSILON
                        if(!indiceOrExpr.isEpsilon() && !indiceOrExpr.getChild().isEpsilon() && !objectArrayIdentifier(funcOrObj.getType())){
                            OutputWriter.semanticErrWriting("ERROR: Indicing a non-array variable: " + id.getValue() + " in " + localTable.name);
                            node.setType("ERR@!");
                            return;
                        } else if(!indiceOrExpr.isEpsilon() && objectArrayIdentifier(funcOrObj.getType())){
                            typeBuffer = indiceHandling(id, indiceOrExpr);
                        } else {
                            SymTabEntry memberVariableEntry = classTable.containsName(id.getValue());

                            if(memberVariableEntry == null){
                                OutputWriter.semanticErrWriting("ERROR: Use of undefined member variable " + id.getValue() + " from class " + typeBuffer + ", calling from " + localTable.name);
                                node.setType("ERR@!");
                                id.setType("ERR@!");
                                return;
                            } else {
                                typeBuffer = memberVariableEntry.getType();
                                id.setType(typeBuffer);
                            }
                        }
                    }
                } else {
                    // This is a function
                    if(indiceOrExpr.checkContent("indiceList")){
                        OutputWriter.semanticErrWriting("ERROR: Indicing a function: " + id.getValue() + " in " + localTable.name);
                        node.setType("ERR@!");
                        return;
                    } else {
                        // indiceOrExpr is exprList
                        // Get all the types of parameter
                        String paramTypes = "";
                        SyntaxTreeNode param = indiceOrExpr.getChild();
                        while(param != null && !param.isEpsilon()){
                            SyntaxTreeNode leafNode = param.getChild();
                            while(leafNode.getChild() != null){
                                leafNode = leafNode.getChild();
                            }
                            paramTypes += leafNode.getType() + ",";

                            param = param.getRightSib();
                        }
                        if(!paramTypes.isEmpty()){
                            paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                        }

                        // Get the correct function from class table (in case of overloading)
                        funcOrObj = classTable.containsParams(id.getValue(), paramTypes);
                        if(funcOrObj == null){
                            OutputWriter.semanticErrWriting("ERROR: There's no function " + id.getValue() + " in " + classTable.name + " with the corresponding params, calling from " + localTable.name);
                            node.setType("ERR@!");
                            return;
                        }

                        // Load return type to type buffer
                        typeBuffer = funcOrObj.getReturnType();
                    }
                }

                // Change the class table
                funcOrObj = this.globalTable.containsName(typeBuffer);
                if(funcOrObj != null){
                    // This is an object
                    classTable = funcOrObj.getLink();
                } else if(!cur.getRightSib().isEpsilon()) {
                    OutputWriter.semanticErrWriting("ERROR: Use of undefined object of type " + typeBuffer + " in " + localTable.name);
                    node.setType("ERR@!");
                    return;
                }

                // Go to the next idnest
                cur = cur.getRightSib();
            }

            node.setType(typeBuffer);
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
            OutputWriter.semanticErrWriting("Indicing dimension out of bounds for variable " + origin.getValue() + " in " + localTable.name);
            return "ERR@!";
        } else {
            return originType;
        }

    }

    private boolean primitiveArrayIdentifier(String type){
        Pattern intArrPattern = Pattern.compile("\\Ainteger(\\[.*\\])+\\Z");
        Pattern floatArrPattern = Pattern.compile("\\Afloat(\\[.*\\])+\\Z");
        Matcher matcher1 = intArrPattern.matcher(type);
        Matcher matcher2 = floatArrPattern.matcher(type);
        if(matcher1.find() || matcher2.find()){
            return true;
        } else {
            return false;
        }
    }

    private boolean objectArrayIdentifier(String type){
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
        return variable;
    }
}
