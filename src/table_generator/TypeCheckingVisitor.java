package table_generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;

public class TypeCheckingVisitor extends Visitor {
    private SymbolTable globalTable;
    private SymbolTable localTable = null;
    private SymbolTable classLevelTable = null;
    private String typeBuffer = null;

    public TypeCheckingVisitor(SymbolTable table){
        // Get the global table for type checking on functions
        this.globalTable = table;
    }
    
    public void visit(SyntaxTreeNode node){
        if(node.checkContent("assignOrFuncCall")){
            SyntaxTreeNode cur = node.getChild();
            String lhsType = null;

            if(cur.isEpsilon()){
                node.setType("void");
                return;
            }

            while(cur != null && !cur.isEpsilon()){
                if(cur.checkContent("id")){
                    String variable = cur.getValue();
                    if(cur.getRightSib().checkContent("exprList")){
                        // Function call
                        cur = cur.getRightSib();
                        SyntaxTreeNode expr = cur.getChild();

                        // Get param types to match with func declaration
                        String paramTypes = "";
                        while(expr != null && !expr.isEpsilon()){
                            paramTypes += expr.getType() + ",";
                        }
                        if(!paramTypes.isEmpty()){
                            paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                        }
                        if(typeBuffer == null){
                            // global function call or constructor
                            SymTabEntry varDecl = this.globalTable.accessFromGlobal(variable);
                            if(varDecl == null){
                                // Declaration not found, error handling
                                System.out.println("ERROR: Unable to find this variable " + variable + " declaration, assignOrFuncCall error!");
                                node.setType("ERR@!");
                            } else if(varDecl.getKind().equals("class")){
                                // Found declaration as a constructor
                                SymbolTable classTable = varDecl.getLink();

                                if(paramTypes.isEmpty()){
                                    SymTabEntry constructor = classTable.contains("constructor", "void");
                                    if(constructor == null){
                                        System.out.println("ERROR: Unable to find the default constructor for class " + variable + ", assignOrFuncCall error!");
                                        typeBuffer = "ERR@!";
                                    } else {
                                        typeBuffer = variable;
                                    }
                                } else {
                                    SymTabEntry constructor = classTable.containsParams("constructor", paramTypes);
                                    if(constructor == null){
                                        System.out.println("ERROR: Unable to find the constructor for class " + variable + " with params(" + paramTypes + "), assignOrFuncCall error!");
                                        typeBuffer = "ERR@!";
                                    } else {
                                        typeBuffer = variable;
                                    }
                                }
                            } else {
                                // Found declaration as a global function
                                SymTabEntry funcDecl = this.globalTable.containsParams(variable, paramTypes);
                                if(funcDecl == null){
                                    System.out.println("ERROR: Unable to find the global function " + variable + " with params(" + paramTypes + "), assignOrFuncCall error!");
                                    typeBuffer = "ERR@!";
                                } else {
                                    if(funcDecl.getType().indexOf(':') != -1){
                                        typeBuffer = funcDecl.getType().split(":")[0];
                                    } else {
                                        typeBuffer = funcDecl.getType();
                                    }
                                }
                            }
                            cur = cur.getRightSib();
                            continue;
                        } else {
                            // member func call or error
                            if(typeBuffer.equals("ERR@!")){
                                cur = cur.getRightSib();
                                continue;
                            }

                            SymTabEntry classDecl = this.globalTable.accessFromGlobal(typeBuffer);
                            SymTabEntry funcDecl = classDecl.getLink().containsParams(variable, paramTypes);
                            if(funcDecl == null){
                                System.out.println("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                typeBuffer = "ERR@!";
                            } else{
                                if(paramTypes.isEmpty()){
                                    typeBuffer = funcDecl.getType();
                                } else {
                                    String[] funcTypes = funcDecl.getType().split(":");
                                    typeBuffer = funcTypes[0];
                                }
                            }
                            cur = cur.getRightSib();
                            continue;
                        }
                    } else if(cur.getRightSib().checkContent("indiceList")){
                        // Indicing array
                        cur = cur.getRightSib();
                        typeBuffer = this.localTable.accessFromGlobal(variable).getType();
                        if(!cur.getChild().isEpsilon()){
                            // meaning this is an array
                            int dimension = typeBuffer.length() - typeBuffer.replace("[", "").length();
                            int indicingDim = cur.getChildNum()-1;
                            if(dimension < indicingDim){
                                System.out.println("ERROR: Array dimension out of bound when indicing variable " + variable);
                            } else if (dimension > indicingDim){
                                int residual = dimension - indicingDim;
                                while(residual > 0){
                                    typeBuffer = typeBuffer.substring(0, typeBuffer.lastIndexOf('['));
                                    residual--;
                                }
                            } else {
                                typeBuffer = typeBuffer.substring(0, typeBuffer.indexOf('['));
                            }
                        }
                        cur = cur.getRightSib();
                        continue;
                    } else {
                        // Dot
                        cur = cur.getRightSib();
                        continue;
                    }
                } else if(cur.checkContent("expr")){
                    typeBuffer = cur.getType();
                    cur = cur.getRightSib();
                } else if(cur.checkContent("dot")){
                    cur = cur.getRightSib();
                    continue;
                } else if(cur.checkContent("assign")){
                    lhsType = typeBuffer;
                    // Clear type buffer for the rhs
                    typeBuffer = null;
                    cur = cur.getRightSib();
                    continue;
                }
            }

            // Compare type
            if(!lhsType.equals(typeBuffer) || lhsType.equals("ERR@!")){
                System.out.println("ERROR: Mismatch type in assignment statement " + node.toTree() + ", assignOrFuncCall error!");
            }
        } else if(node.checkContent("classDecl")){
            // Check for circular inheritance
            SyntaxTreeNode cur = node.getChild();
            String className = cur.getValue();

            // get inherit list
            cur = cur.getRightSib().getChild();
            String parent = null;
            while(cur != null && !cur.isEpsilon()){
                if(cur.checkContent("isa")){
                    cur = cur.getRightSib();
                    continue;
                }
                parent = cur.getValue();
                if(inheritListChecker(className, parent)){
                    System.out.println("ERROR: Circular inheritance found in class " + className + " with parent class " + parent + "!");
                }
                cur = cur.getRightSib();
            }
        } else if(node.checkContent("expr")){
            SyntaxTreeNode arithExpr = node.getChild();
            SyntaxTreeNode expr2 = arithExpr.getRightSib();

            if(expr2.isEpsilon()){
                // meaning EPSILON
                node.setType(arithExpr.getType());
            } else {
                node.setType("boolean");
            }
        } else if(node.checkContent("factor")){
            if(node.getChildNum() == 1){
                // Can be termList, floatLit or intLit
                node.setType(node.getChild().getType());
            } else if(node.getChildNum() == 2){
                // Can be signed factor
                node.setType(node.getChild().getRightSib().getType());
            } else {
                // factor idnest
                SyntaxTreeNode cur = node.getChild();
                if(cur.checkContent("id")){
                    String variable = cur.getValue();
                    SymTabEntry ID = cur.getTableEntry();

                    // Checking the dimension/aParams
                    cur = cur.getRightSib();
                    if(cur.checkContent("indiceList")){
                        // Either a variable or an array
                        SymTabEntry id = this.localTable.accessFromGlobal(variable);
                        if(id == null){
                            id = this.classLevelTable.accessFromGlobal(variable);
                            if(id == null){
                                System.out.println("ERROR: Cannot find variable " + id + " in class " + classLevelTable.name + ", factor error!");
                                node.setType("ERR@!");
                                return;
                            } else{
                                ID.setType(id.getType());
                            }
                        } else {
                            ID.setType(id.getType());
                        }

                        if(!cur.getChild().isEpsilon()){
                            // meaning this is an array
                            int dimension = typeBuffer.length() - typeBuffer.replace("[", "").length();
                            int indicingDim = cur.getChildNum()-1;
                            if(dimension < indicingDim){
                                System.out.println("ERROR: Array dimension out of bound when indicing variable " + variable);
                            } else if (dimension > indicingDim){
                                int residual = dimension - indicingDim;
                                while(residual > 0){
                                    typeBuffer = typeBuffer.substring(0, typeBuffer.lastIndexOf('['));
                                    residual--;
                                }
                            } else {
                                typeBuffer = typeBuffer.substring(0, typeBuffer.indexOf('['));
                            }
                        } else {
                            typeBuffer = id.getType();
                        }
                    } else {
                        // exprList
                        if(!cur.getChild().isEpsilon()){
                            SyntaxTreeNode expr = cur.getChild();

                            // Get param types to match with func declaration
                            String paramTypes = "";
                            while(expr != null && !expr.isEpsilon()){
                                paramTypes += expr.getType() + ",";
                                expr = expr.getRightSib();
                            }
                            if(!paramTypes.isEmpty()){
                                paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                            }
                            if(typeBuffer == null){
                                // Can only be global function or constructor
                                SymTabEntry variableDecl = this.globalTable.accessFromGlobal(variable);
                                if(variableDecl == null){
                                    System.out.println("ERROR: Variable " + variable + " is neither a function nor a class, factor error!");
                                    node.setType("ERR@!");
                                    return;
                                }

                                if(variableDecl.getKind().equals("class")){
                                    SymTabEntry constructor = variableDecl.getLink().contains("constructor", "void:"+paramTypes);
                                    if(constructor == null){
                                        System.out.println("ERROR: Constructor " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                        node.setType("ERR@!");
                                        return;
                                    } else {
                                        typeBuffer = variable;
                                    }
                                } else {
                                    if(typeBuffer.equals("void") || typeBuffer.equals("ERR@!") || typeBuffer.indexOf(':') != -1){
                                        typeBuffer = "ERR@!";
                                        System.out.println("ERROR: Non-object variable found calling member functions, factor error!");
                                        node.setType(typeBuffer);
                                        return;
                                    } else if(paramTypes.isEmpty()){
                                        if(variableDecl.getType().indexOf(':')==-1){
                                            typeBuffer = variableDecl.getType();
                                        } else {
                                            System.out.println("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                            node.setType("ERR@!");
                                            return;
                                        }
                                    } else {
                                        String[] funcTypes = variableDecl.getType().split(":");
                                        if(funcTypes[1].equals(paramTypes)){
                                            typeBuffer = funcTypes[0];
                                        } else {
                                            System.out.println("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                            node.setType("ERR@!");
                                            return;
                                        }
                                    }
                                    
                                }
                            } else {
                                // Can only be member function call
                                SymTabEntry classDecl = this.globalTable.accessFromGlobal(typeBuffer);
                                SymTabEntry funcDecl = classDecl.getLink().containsParams(variable, paramTypes);
                                if(funcDecl == null){
                                    System.out.println("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                    node.setType("ERR@!");
                                    return;
                                } else{
                                    if(paramTypes.isEmpty()){
                                        typeBuffer = funcDecl.getType();
                                    } else {
                                        String[] funcTypes = funcDecl.getType().split(":");
                                        typeBuffer = funcTypes[0];
                                    }
                                }
                            }
                        }
                    }
                    
                    // Checking idnest list
                    cur = cur.getRightSib();
                    if(cur.getChild().isEpsilon()){
                        node.setType(typeBuffer);
                    } else {
                        node.setType(cur.getType());
                    }
                }
            }
        } else if(node.checkContent("funcHead")){
            // Switch local table for each function definition
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }
            if(node.getChildNum() == 3){
                localTable = this.globalTable.accessFromGlobal(node.getChild().getValue()).getLink();
            } else if(node.getChildNum() == 4){
                String owner = node.getChild().getValue();
                classLevelTable = this.globalTable.accessFromGlobal(owner).getLink();
                this.localTable = classLevelTable.contains("constructor", node.getTableEntry().getType()).getLink();
            } else {
                String owner = node.getChild().getValue();
                classLevelTable = this.globalTable.accessFromGlobal(owner).getLink();
                this.localTable = classLevelTable.contains(funcName, node.getTableEntry().getType()).getLink();
            }
        } else if(node.checkContent("id")){
            // Check if the next node is assignOrFuncCall
            if(node.getRightSib() != null && node.getRightSib().checkContent("assignOrFuncCall")){
                SymTabEntry cur = this.localTable.accessFromGlobal(node.getValue());
                if(cur == null){
                    cur = this.classLevelTable.accessFromGlobal(node.getValue());
                    if(cur == null){
                        System.out.println("ERROR: Use of undeclared variable!");
                    } else {
                        typeBuffer = cur.getType();
                    }
                } else {
                    typeBuffer = cur.getType();
                }
            }
        } else if(node.checkContent("idnest")){
            SyntaxTreeNode cur = node.getChild().getRightSib().getRightSib(); // Skip through dot and id nodes
            node.setType(cur.getType());
        } else if(node.checkContent("idnestList")){
            SyntaxTreeNode cur = node.getChild();

            while(cur.getRightSib() != null && !cur.getRightSib().isEpsilon()){
                cur = cur.getRightSib();
            }

            // Set type of the last child to get the most updated type
            node.setType(cur.getType());
        } else if(node.checkContent("localVarDecl")){
            // TODO: Check for constructor or global function call params
            SyntaxTreeNode cur = node.getChild().getRightSib();
            if(cur.getChild().checkContent("id") && cur.getRightSib().getChild().checkContent("expr")){
                SyntaxTreeNode expr = cur.getRightSib().getChild();
                String variable = cur.getChild().getValue();

                // Get param types to match with func declaration
                String paramTypes = "";
                while(expr != null && !expr.isEpsilon()){
                    paramTypes += expr.getType() + ",";
                    expr = expr.getRightSib();
                }
                if(!paramTypes.isEmpty()){
                    paramTypes = paramTypes.substring(0, paramTypes.length()-1);
                }

                SymTabEntry varDecl = this.globalTable.accessFromGlobal(variable);
                if(varDecl == null){
                    System.out.println("ERROR: Undeclared class/function");
                }
            }
        } else if(node.checkContent("relExpr")){
            SyntaxTreeNode lhs = node.getChild();
            SyntaxTreeNode rhs = lhs.getRightSib().getRightSib(); // Skip through the relOp node

            if(!lhs.getType().equals(rhs.getType())){
                System.out.println("ERROR: Type mismatch when comparing between " + lhs + " and " + rhs + ", relExpr error!");
                node.setType("ERR@!");
            } else {
                node.setType("boolean");
            }
        } else if(node.checkContent("statement")){
            // Reset the buffers
            typeBuffer = null;
        } else if(node.checkContent("term")){
            SyntaxTreeNode cur = node.getChild();
            String type = cur.getType();

            while(cur != null && !cur.isEpsilon()){
                if(cur.checkContent("multOp")){
                    cur = cur.getRightSib();
                    continue;
                }
                if(!type.equals("integer") && !type.equals("float")){
                    System.out.println("ERROR: Incompatible type " + type + " for addition!");
                    node.setType("ERR@!");
                    return;
                } else if(!cur.getType().equals(type)){
                    System.out.println("ERROR: Mismatch type " + type + " and " + cur.getType() + " for addition!");
                    node.setType("ERR@!");
                    return;
                } else {
                    cur = cur.getRightSib();
                }
            }
            node.setType(type);
        } else if(node.checkContent("termList")){
            SyntaxTreeNode cur = node.getChild();
            String type = cur.getType();

            while(cur != null && !cur.isEpsilon()){
                if(cur.checkContent("plus") || cur.checkContent("minus") || cur.checkContent("or")){
                    cur = cur.getRightSib();
                    continue;
                }
                if(!type.equalsIgnoreCase("integer") && !type.equalsIgnoreCase("float")){
                    System.out.println("ERROR: Incompatible type " + type + " for addition!");
                    node.setType("ERR@!");
                    return;
                } else if(!cur.getType().equals(type)){
                    System.out.println("ERROR: Mismatch type " + type + " and " + cur.getType() + " for addition!");
                    node.setType("ERR@!");
                    return;
                } else {
                    cur = cur.getRightSib();
                }
            }
            node.setType(type);
        }
    }

    private boolean inheritListChecker(String className, String parent){
        SymTabEntry parentEntry = this.globalTable.accessFromGlobal(parent);
        String type = parentEntry.getType();
        Pattern pattern = Pattern.compile("\\A(.*?):(.*)\\Z");
        Matcher matcher = pattern.matcher(type);

        if(matcher.find()){
            // meaning there is an inherit list for this class
            if(matcher.group(1).compareTo(className)==0){
                return true;
            }
            String inheritList = matcher.group(2);
            String[] parents = inheritList.split(",");
            for(String p : parents){
                if(inheritListChecker(className, p)){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
