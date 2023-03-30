package code_generator;

import java.util.ListIterator;

import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;
import table_generator.Visitor;

public class CodeGenerationVisitor1 extends Visitor{
    private CodeGenTable globalTable;
    private CodeGenTable localTable;

    public CodeGenerationVisitor1(CodeGenTable table){
        this.globalTable = table;
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("funcHead")){
            // Visitor is at a funcDef
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }
            if(node.getChildNum() == 3){
                // Global function scope
                localTable = this.globalTable.containsName(node.getChild().getValue()).link;
            } else {
                // Member function scope
                String owner = node.getChild().getValue();
                CodeGenTable classTable = this.globalTable.containsName(owner).link;
                localTable = classTable.containsFunction(funcName, node.getTableEntry().getType()).link;
            }
            tableVisitor(localTable);
        } else if(node.checkContent("inheritList")){
            // Visitor is at a classDecl
            String className = node.getLeftmostSib().getValue();
            localTable = this.globalTable.containsName(className).link;
            tableVisitor(localTable);
        }
    }

    private void tableVisitor(CodeGenTable table){
        // Reserve a memory amount for the whole class/function
        if(table.scopeSize == 0){
            return;
        } else {
            OutputWriter.codeDeclGen(table.name + "\tres " + table.scopeSize);
        }

        // Declare all member variables
        ListIterator<CodeTabEntry> i = table.getTable().listIterator();

        while(i.hasNext()){
            CodeTabEntry cur = i.next();
            
            // Only declare variables
            if(cur.kind.equals("variable")){
                if(cur.size == 0){
                    continue;
                } else {
                    OutputWriter.codeDeclGen(cur.name + "\tres " + cur.size);
                }
            }
        }
        OutputWriter.codeDeclGen("\n");
        
    }
}
