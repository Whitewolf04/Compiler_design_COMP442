package table_generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;

public class TypeCheckingVisitor extends Visitor {
    private SymbolTable globalTable;
    private SymbolTable localTable = null;

    public TypeCheckingVisitor(SymbolTable table){
        // Get the global table for type checking on functions
        this.globalTable = table;
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
                    System.out.println("ERROR: Circular inheritance found in class " + className + " with parent class " + parent + "!");
                }
            }
        } else if(node.checkContent("factor")){
            if(node.getChildNum() == 1){
                // Can be termList, floatLit or intLit
                node.setType(node.getChild().getType());
            } else if(node.getChildNum() == 2){
                node.setType(node.getChild().getRightSib().getType());
            } else {
                
            }
        } else if(node.checkContent("funcDef")){
            SyntaxTreeNode funcHead = node.getChild();
            if(funcHead.getChildNum() == 3){
                localTable = this.globalTable.accessFromGlobal(funcHead.getChild().getValue()).getLink();
            } else if(funcHead.getChildNum() == 4){
                String owner = funcHead.getChild().getValue();
                SymbolTable classTable = this.globalTable.accessFromGlobal(owner).getLink();
                funcHead.getTableEntry();
            }
        } else if(node.checkContent("localVarDecl")){
            // TODO: Check for constructor or global function call params
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
