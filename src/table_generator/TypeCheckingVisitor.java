package table_generator;

import java.util.LinkedList;
import java.util.ListIterator;
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

    public TypeCheckingVisitor(SymbolTable table){
        // Get the global table for type checking on functions
        this.globalTable = table;
        localScopeVar = new LinkedList<SymTabEntry>();
    }
    
    public void visit(SyntaxTreeNode node){
        if(node.checkContent("assignOrFuncCall")){
            SyntaxTreeNode origin = node.getLeftmostSib();
            SyntaxTreeNode idnestCall = node.getChild();

            if(idnestCall.checkContent("assign")){
                SyntaxTreeNode expr = idnestCall.getRightSib();
                if(origin.getType().equals("ERR@!") || expr.getType().equals("ERR@!")){
                    // Skip through error (Not raise error twice)
                    return;
                } else if(!origin.getType().equals(expr.getType())){
                    OutputWriter.semanticErrWriting("ERROR: Incompatible type assignment on line " + idnestCall.getLineCount());
                }
            } else {
                // Loop until the last idnest element right before assign
                while(idnestCall.getRightSib() != null){
                    if(idnestCall.getRightSib().checkContent("assign")){
                        break;
                    } else {
                        idnestCall = idnestCall.getRightSib();
                    }
                }

                // Skip through function call
                if(idnestCall.getRightSib() == null){
                    return;
                }

                SyntaxTreeNode expr = idnestCall.getRightSib().getRightSib();
                if(idnestCall.getType().equals("ERR@!") || expr.getType().equals("ERR@!")){
                    // Skip through error (Not raise error twice)
                    return;
                } else if(!idnestCall.getType().equals(expr.getType())){
                    OutputWriter.semanticErrWriting("ERROR: Incompatible type assignment on line " + expr.getLineCount());
                }
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
            SyntaxTreeNode termList = node.getChild();
            SyntaxTreeNode expr2 = termList.getRightSib();

            if(expr2.isEpsilon()){
                // meaning EPSILON
                node.setType(termList.getType());
            } else {
                SyntaxTreeNode termList2 = expr2.getChild().getRightSib();
                if(!termList.getType().equals(termList2.getType())){
                    OutputWriter.semanticErrWriting("ERROR: Uncompatible type for comparison on line " + termList2.getLineCount());
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
        } else if(node.checkContent("relExpr")){
            SyntaxTreeNode lhs = node.getChild();
            SyntaxTreeNode rhs = lhs.getRightSib().getRightSib(); // Skip through the relOp node
            String lhsType = lhs.getType();
            String rhsType = rhs.getType();

            if(lhsType.equals("ERR@!") || rhsType.equals("ERR@!")){
                OutputWriter.semanticErrWriting("WARNING: Cannot compare because either side has type error! Line " + node.getLineCount());
            } else if((!lhsType.equals("integer") && !lhsType.equals("float")) || (!rhsType.equals("integer") && !rhsType.equals("float"))){
                OutputWriter.semanticErrWriting("ERROR: Cannot compare non-primitive types, line " + node.getLineCount());
            } else if(!lhs.getType().equals(rhs.getType())){
                OutputWriter.semanticErrWriting("ERROR: Type mismatch when comparing on line " + node.getLineCount());
            }
        } else if(node.checkContent("term")){
            SyntaxTreeNode cur = node.getChild();
            if(cur.isEpsilon()){
                return;
            } else if(cur.checkContent("factor") && node.getChildNum() == 2){
                node.setType(cur.getType());
            } else {
                String type = cur.getType();
                while(cur != null && !cur.isEpsilon()){
                    if(cur.checkContent("multOp")){
                        cur = cur.getRightSib();
                        continue;
                    } else if(cur.checkContent("factor")){
                        if(cur.getType().equals("ERR@!")){
                            node.setType("ERR@!");
                            return;
                        }
                        if(!cur.getType().equals(type)){
                            OutputWriter.semanticErrWriting("ERROR: Incompatible type for multiply/division on line " + cur.getLineCount());
                            node.setType("ERR@!");
                            return;
                        } else {
                            cur = cur.getRightSib();
                        }
                    }
                }
                node.setType(type);
                System.out.println("A term has been set to type " + type);
            }
        } else if(node.checkContent("termList")){
            SyntaxTreeNode cur = node.getChild();
            if(cur.isEpsilon()){
                return;
            } else if(cur.checkContent("term") && node.getChildNum() == 2){
                node.setType(cur.getType());
            } else {
                String type = cur.getType();
                while(cur != null && !cur.isEpsilon()){
                    if(cur.checkContent("addOp")){
                        cur = cur.getRightSib();
                        continue;
                    } else if(cur.checkContent("term")){
                        if(cur.getType().equals("ERR@!")){
                            node.setType("ERR@!");
                            return;
                        }
                        if(!cur.getType().equals(type)){
                            OutputWriter.semanticErrWriting("ERROR: Incompatible type for add/subtract on line " + cur.getLineCount());
                            node.setType("ERR@!");
                            return;
                        } else {
                            cur = cur.getRightSib();
                        }
                    }
                }
                node.setType(type);
                System.out.println("A termList has been set to type " + type);
            }
        } else if(node.checkContent("statement")){
            // Type check for return statement
            SyntaxTreeNode statementType = node.getChild();
            if(statementType.checkContent("return")){
                SyntaxTreeNode expr = statementType.getRightSib();
                String returnType = getReturnTypeOfFunc(localTable.name);

                if(returnType == null){
                    OutputWriter.semanticErrWriting("ERROR: Return type mismatch for function " + localTable.name + " on line " + expr.getLineCount());
                } else if(!expr.getType().equals(returnType)){
                    OutputWriter.semanticErrWriting("ERROR: Return type mismatch for function " + localTable.name + " on line " + expr.getLineCount());
                }
            }
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

    /*
     * Description: Get the return types of a function through it's symbol table
     * Input: Function name
     * Output: Return type of the function. Return null if function not found or there was some error
     */
    private String getReturnTypeOfFunc(String funcName){
        String paramTypes = getParamTypesFromTable(localTable);

        if(funcName.indexOf("::") == -1){
            // Global function
            SymTabEntry funcEntry = globalTable.containsParams(funcName, paramTypes);

            // Return null if function not found
            if(funcEntry == null){
                return null;
            } else {
                return funcEntry.getReturnType();
            }
        } else {
            // Member function
            String className = funcName.substring(0, funcName.indexOf("::"));
            funcName = funcName.substring(funcName.indexOf("::")+2, funcName.length());
            SymTabEntry classTable = globalTable.containsName(className);

            // Return null if there's already an error
            if(classTable == null || !classTable.getKind().equals("class")){
                return null;
            }

            // Get the function entry
            SymTabEntry funcEntry = classTable.getLink().containsParams(funcName, paramTypes);

            if(funcEntry == null){
                return null;
            } else {
                return funcEntry.getReturnType();
            }
        }
    }

    private String getParamTypesFromTable(SymbolTable table){
        LinkedList<SymTabEntry> tableEntries = table.getTable();
        ListIterator<SymTabEntry> i = tableEntries.listIterator();
        String paramTypes = "";

        while(i.hasNext()){
            SymTabEntry cur = i.next();

            if(cur.getKind().equals("parameter")){
                paramTypes += cur.getType() + ",";
            }
        }
        if(!paramTypes.isEmpty()){
            paramTypes = paramTypes.substring(0, paramTypes.length()-1);
        }

        return paramTypes;
    }
}
