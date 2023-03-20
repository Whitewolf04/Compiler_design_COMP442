package table_generator;

import java.util.ListIterator;

import AST_generator.Factory;
import AST_generator.SyntaxTreeNode;

public class Generator {
    public static void visitTree(){
        TableCreationVisitor visitor = new TableCreationVisitor();
        treeTraversal(Factory.nodeStack.peek(), visitor);
        printTable(visitor.table);
        
        TypeCheckingVisitor typeChecker = new TypeCheckingVisitor(visitor.table);
        treeTraversal(Factory.nodeStack.peek(), typeChecker);
    }

    public static void treeTraversal(SyntaxTreeNode node, Visitor visitor){
        if(node.getChild() != null){
            treeTraversal(node.getChild(), visitor);
        }
        System.out.println(node.toTree() + "\n");
        visitor.visit(node);
        if(node.getRightSib() != null){
            treeTraversal(node.getRightSib(), visitor);
        }
    }

    public static void printTable(SymbolTable table){
        System.out.println(table.printTable());
        ListIterator<SymTabEntry> i = table.getTable().listIterator();

        while(i.hasNext()){
            SymTabEntry cur = i.next();
            if(cur.getName().compareTo("self") == 0){
                continue;
            }
            if(cur.getLink() != null){
                printTable(cur.getLink());
            }
        }
    }
}
