package table_generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;

public class TypeCheckingVisitor extends Visitor {
    private SymbolTable table;

    public TypeCheckingVisitor(SymbolTable table){
        // Get the global table for type checking on functions
        this.table = table;
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
        }
    }

    private boolean inheritListChecker(String className, String parent){
        SymTabEntry parentEntry = this.table.accessClass(parent);
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
