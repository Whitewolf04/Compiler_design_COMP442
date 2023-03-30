package table_generator;

import java.util.ListIterator;

import AST_generator.Factory;
import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;

public class SymbolTableGenerator {
    public static SymbolTable globalTable;

    public static void visitTree(){
        TableCreationVisitor visitor = new TableCreationVisitor();
        treeTraversal(Factory.nodeStack.peek(), visitor);
        printSymbolTable(visitor.table);
        globalTable = visitor.table;
        
        TypeAssignVisitor typeAssigner = new TypeAssignVisitor(globalTable);
        treeTraversal(Factory.nodeStack.peek(), typeAssigner);

        TypeCheckingVisitor typeChecker = new TypeCheckingVisitor(globalTable);
        treeTraversal(Factory.nodeStack.peek(), typeChecker);
        System.out.println("Done type checking!");
    }

    public static void treeTraversal(SyntaxTreeNode node, Visitor visitor){
        if(node.getChild() != null){
            treeTraversal(node.getChild(), visitor);
        }
        // System.out.println(node.toTree() + "\n");
        visitor.visit(node);
        if(node.getRightSib() != null){
            treeTraversal(node.getRightSib(), visitor);
        }
    }

    public static void printSymbolTable(SymbolTable table){
        // System.out.println(table.printTable());
        OutputWriter.semanticOutWriting(table.printTable());
        ListIterator<SymTabEntry> i = table.getTable().listIterator();

        while(i.hasNext()){
            SymTabEntry cur = i.next();
            if(cur.getName().compareTo("self") == 0){
                continue;
            } else if(cur.getLink() != null){
                printSymbolTable(cur.getLink());
            }
        }
    }
}
