package table_generator;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;

public class TypeCheckingVisitor extends Visitor {
    private SymbolTable globalTable;
    private LinkedList<SymTabEntry> localScopeVar = null;
    private LinkedList<SymTabEntry> classScopeVar = null;
    private SymbolTable localTable = null;
    private SymbolTable classLevelTable = null;
    private String typeBuffer = null;
    private String idnestBuffer = null;

    public TypeCheckingVisitor(SymbolTable table){
        // Get the global table for type checking on functions
        this.globalTable = table;
        localScopeVar = new LinkedList<SymTabEntry>();
    }
    
    public void visit(SyntaxTreeNode node){
        if(node.checkContent("assignOrFuncCall")){
            SyntaxTreeNode cur = node.getChild();
            String lhsType = typeBuffer;

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
                            SymTabEntry varDecl = this.globalTable.containsName(variable);
                            if(varDecl == null){
                                // Declaration not found, error handling
                                OutputWriter.semanticErrWriting("ERROR: Unable to find this variable " + variable + " declaration, assignOrFuncCall error!");
                                node.setType("ERR@!");
                            } else if(varDecl.getKind().equals("class")){
                                // Found declaration as a constructor
                                SymbolTable classTable = varDecl.getLink();

                                if(paramTypes.isEmpty()){
                                    SymTabEntry constructor = classTable.containsFunction("constructor", "void");
                                    if(constructor == null){
                                        OutputWriter.semanticErrWriting("ERROR: Unable to find the default constructor for class " + variable + ", assignOrFuncCall error!");
                                        typeBuffer = "ERR@!";
                                    } else {
                                        typeBuffer = variable;
                                    }
                                } else {
                                    SymTabEntry constructor = classTable.containsParams("constructor", paramTypes);
                                    if(constructor == null){
                                        OutputWriter.semanticErrWriting("ERROR: Unable to find the constructor for class " + variable + " with params(" + paramTypes + "), assignOrFuncCall error!");
                                        typeBuffer = "ERR@!";
                                    } else {
                                        typeBuffer = variable;
                                    }
                                }
                            } else {
                                // Found declaration as a global function
                                SymTabEntry funcDecl = this.globalTable.containsParams(variable, paramTypes);
                                if(funcDecl == null){
                                    OutputWriter.semanticErrWriting("ERROR: Unable to find the global function " + variable + " with params(" + paramTypes + "), assignOrFuncCall error!");
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

                            SymTabEntry classDecl = this.globalTable.containsName(typeBuffer);
                            SymTabEntry funcDecl = classDecl.getLink().containsParams(variable, paramTypes);
                            if(funcDecl == null){
                                OutputWriter.semanticErrWriting("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
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
                        typeBuffer = this.localTable.containsName(variable).getType();
                        if(!cur.getChild().isEpsilon()){
                            // meaning this is an array
                            int dimension = typeBuffer.length() - typeBuffer.replace("[", "").length();
                            int indicingDim = cur.getChildNum()-1;
                            if(dimension < indicingDim){
                                OutputWriter.semanticErrWriting("ERROR: Array dimension out of bound when indicing variable " + variable);
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
                } else if (cur.checkContent("indiceList")){
                    int indicingDim = cur.getChildNum()-1;
                    int dimension = typeBuffer.length() - typeBuffer.replace("[", "").length();
                    
                    if(dimension < indicingDim){
                        OutputWriter.semanticErrWriting("ERROR: Array dimension out of bound when indicing variable " + typeBuffer);
                    } else if (dimension > indicingDim){
                        int residual = dimension - indicingDim;
                        while(residual > 0){
                            typeBuffer = typeBuffer.substring(0, typeBuffer.lastIndexOf('['));
                            residual--;
                        }
                    } else {
                        typeBuffer = typeBuffer.substring(0, typeBuffer.indexOf('['));
                    }
                    cur = cur.getRightSib();
                    continue;
                } else if(cur.checkContent("exprList")){
                    // Check params
                    cur = cur.getChild();
                    String paramsType = "";
                    while(cur != null && !cur.isEpsilon()){
                        paramsType += cur.getType() + ",";
                        cur = cur.getRightSib();
                    }
                    if(!paramsType.isEmpty()){
                        paramsType = paramsType.substring(0, paramsType.length()-1);
                    }
                    SymTabEntry funcDecl = globalTable.containsParams(idnestBuffer, paramsType);
                    if(funcDecl == null){
                        OutputWriter.semanticErrWriting("ERROR: Use of undeclared variable!");
                    }
                }
            }

            // Compare type
            if(!lhsType.equals(typeBuffer) || lhsType.equals("ERR@!")){
                OutputWriter.semanticErrWriting("ERROR: Mismatch type in assignment statement " + node.toTree() + ", assignOrFuncCall error!");
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
                    OutputWriter.semanticErrWriting("ERROR: Circular inheritance found in class " + className + " with parent class " + parent + "!");
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
                String variable = cur.getValue();

                // Checking the dimension/aParams
                cur = cur.getRightSib();
                if(cur.checkContent("indiceList")){
                    // Either a variable or an array
                    SymTabEntry id = null;

                    for(SymTabEntry entry : localScopeVar){
                        if(entry.getName().equals(variable)){
                            id = entry;
                            break;
                        }
                    }
                    if(id == null && classScopeVar != null){
                        for(SymTabEntry entry : classScopeVar){
                            if(entry.getName().equals(variable)){
                                id = entry;
                                break;
                            }
                        }
                    } else if(id == null){
                        OutputWriter.semanticErrWriting("ERROR: Use of undeclared variable " + variable);
                        return;
                    }

                    if(!cur.getChild().isEpsilon()){
                        // meaning this is an array
                        int dimension = typeBuffer.length() - typeBuffer.replace("[", "").length();
                        int indicingDim = cur.getChildNum()-1;
                        if(dimension < indicingDim){
                            OutputWriter.semanticErrWriting("ERROR: Array dimension out of bound when indicing variable " + variable);
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
                            SymTabEntry variableDecl = this.globalTable.containsName(variable);
                            if(variableDecl == null){
                                OutputWriter.semanticErrWriting("ERROR: Variable " + variable + " is neither a function nor a class, factor error!");
                                node.setType("ERR@!");
                                return;
                            }

                            if(variableDecl.getKind().equals("class")){
                                SymTabEntry constructor = variableDecl.getLink().containsFunction("constructor", "void:"+paramTypes);
                                if(constructor == null){
                                    OutputWriter.semanticErrWriting("ERROR: Constructor " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                    node.setType("ERR@!");
                                    return;
                                } else {
                                    typeBuffer = variable;
                                }
                            } else {
                                if(typeBuffer.equals("void") || typeBuffer.equals("ERR@!") || typeBuffer.indexOf(':') != -1){
                                    typeBuffer = "ERR@!";
                                    OutputWriter.semanticErrWriting("ERROR: Non-object variable found calling member functions, factor error!");
                                    node.setType(typeBuffer);
                                    return;
                                } else if(paramTypes.isEmpty()){
                                    if(variableDecl.getType().indexOf(':')==-1){
                                        typeBuffer = variableDecl.getType();
                                    } else {
                                        OutputWriter.semanticErrWriting("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                        node.setType("ERR@!");
                                        return;
                                    }
                                } else {
                                    String[] funcTypes = variableDecl.getType().split(":");
                                    if(funcTypes[1].equals(paramTypes)){
                                        typeBuffer = funcTypes[0];
                                    } else {
                                        OutputWriter.semanticErrWriting("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
                                        node.setType("ERR@!");
                                        return;
                                    }
                                }
                                
                            }
                        } else {
                            // Can only be member function call
                            SymTabEntry classDecl = this.globalTable.containsName(typeBuffer);
                            SymTabEntry funcDecl = classDecl.getLink().containsParams(variable, paramTypes);
                            if(funcDecl == null){
                                OutputWriter.semanticErrWriting("ERROR: Function " + variable + " with params (" + paramTypes + ") not found, factor error!");
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
        } else if(node.checkContent("funcHead")){
            // Switch local table for each function definition
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }
            if(node.getChildNum() == 3){
                localTable = this.globalTable.containsName(node.getChild().getValue()).getLink();
                for(SymTabEntry entry : localTable.getTable()){
                    if(entry.getKind().equals("function")){
                        continue;
                    } else {
                        localScopeVar.add(entry);
                    }
                }
                classLevelTable = null;
                classScopeVar = null;
            } else if(node.getChildNum() == 4){
                String owner = node.getChild().getValue();
                classLevelTable = this.globalTable.containsName(owner).getLink();
                classScopeVar = new LinkedList<SymTabEntry>();
                for(SymTabEntry entry : classLevelTable.getTable()){
                    if(entry.getKind().equals("function")){
                        continue;
                    } else {
                        classScopeVar.add(entry);
                    }
                }
                this.localTable = classLevelTable.containsFunction("constructor", node.getTableEntry().getType()).getLink();
                for(SymTabEntry entry : localTable.getTable()){
                    if(entry.getKind().equals("function")){
                        continue;
                    } else {
                        localScopeVar.add(entry);
                    }
                }
            } else {
                String owner = node.getChild().getValue();
                classLevelTable = this.globalTable.containsName(owner).getLink();
                classScopeVar = new LinkedList<SymTabEntry>();
                for(SymTabEntry entry : classLevelTable.getTable()){
                    if(entry.getKind().equals("function")){
                        continue;
                    } else {
                        classScopeVar.add(entry);
                    }
                }
                this.localTable = classLevelTable.containsFunction(funcName, node.getTableEntry().getType()).getLink();
                for(SymTabEntry entry : localTable.getTable()){
                    if(entry.getKind().equals("function")){
                        continue;
                    } else {
                        localScopeVar.add(entry);
                    }
                }
            }
        } else if(node.checkContent("id")){
            // Check if the next node is assignOrFuncCall
            if(node.getRightSib() != null && node.getRightSib().checkContent("assignOrFuncCall")){
                SymTabEntry id = null;
                String variable = node.getValue();

                for(SymTabEntry entry : localScopeVar){
                    if(entry.getName().equals(variable)){
                        id = entry;
                        break;
                    }
                }
                if(id == null && classScopeVar != null){
                    for(SymTabEntry entry : classScopeVar){
                        if(entry.getName().equals(variable)){
                            id = entry;
                            break;
                        }
                    }
                } else if(id == null){
                    OutputWriter.semanticErrWriting("ERROR: Use of undeclared variable " + variable);
                    return;
                }
                node.setType(id.getType());
                typeBuffer = id.getType();
                idnestBuffer = id.getName();
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

                SymTabEntry varDecl = this.globalTable.containsName(variable);
                if(varDecl == null){
                    OutputWriter.semanticErrWriting("ERROR: Undeclared class/function");
                }
            }
        } else if(node.checkContent("relExpr")){
            SyntaxTreeNode lhs = node.getChild();
            SyntaxTreeNode rhs = lhs.getRightSib().getRightSib(); // Skip through the relOp node

            if(!lhs.getType().equals(rhs.getType())){
                OutputWriter.semanticErrWriting("ERROR: Type mismatch when comparing between " + lhs + " and " + rhs + ", relExpr error!");
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
                    OutputWriter.semanticErrWriting("ERROR: Incompatible type " + type + " for addition!");
                    node.setType("ERR@!");
                    return;
                } else if(!cur.getType().equals(type)){
                    OutputWriter.semanticErrWriting("ERROR: Mismatch type " + type + " and " + cur.getType() + " for addition!");
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
                    OutputWriter.semanticErrWriting("ERROR: Incompatible type " + type + " for addition!");
                    node.setType("ERR@!");
                    return;
                } else if(!cur.getType().equals(type)){
                    OutputWriter.semanticErrWriting("ERROR: Mismatch type " + type + " and " + cur.getType() + " for addition!");
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
        SymTabEntry parentEntry = this.globalTable.containsName(parent);
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
