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

    public TypeCheckingVisitor(SymbolTable table){
        // Get the global table for type checking on functions
        this.globalTable = table;
        localScopeVar = new LinkedList<SymTabEntry>();
    }
    
    public void visit(SyntaxTreeNode node){
        if(node.checkContent("classDecl")){
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
                    OutputWriter.semanticErrWriting("ERROR: Uncompatible type for comparison! Calling from " + localTable.name);
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
            } else if(!lhsType.equals("integer") || !rhsType.equals("integer") || !lhsType.equals("float") || !rhsType.equals("float")){
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
                            OutputWriter.semanticErrWriting("ERROR: Incompatible type for multiply/division! Calling from " + localTable.name);
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
                            OutputWriter.semanticErrWriting("ERROR: Incompatible type for add/subtract! Calling from " + localTable.name);
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
