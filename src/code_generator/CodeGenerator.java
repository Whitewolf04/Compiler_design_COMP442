package code_generator;

import java.util.ListIterator;

import AST_generator.Factory;
import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;
import table_generator.SymbolTableGenerator;
import table_generator.Visitor;

public class CodeGenerator {
    public static CodeGenTable globalTable;

    public static void generate(String outputFileName){
        TableConversionVisitor tableConverter = new TableConversionVisitor(SymbolTableGenerator.globalTable);
        tableConverter.convertSymbolTable();
        globalTable = tableConverter.codeTable;

        TempVarVisitor tempVarVisitor = new TempVarVisitor(globalTable);
        treeTraversal(Factory.nodeStack.peek(), tempVarVisitor);

        printCodeGenTable(tableConverter.codeTable);

        OutputWriter.openCodeDeclGen(outputFileName);
        CodeGenerationVisitor1 codeGen1 = new CodeGenerationVisitor1(globalTable);
        treeTraversal(Factory.nodeStack.peek(), codeGen1);
        OutputWriter.closeCodeDeclGenStream();
    }
    
    public static void printCodeGenTable(CodeGenTable table){
        System.out.println(table.printTable());
        ListIterator<CodeTabEntry> i = table.getTable().listIterator();

        while(i.hasNext()){
            CodeTabEntry cur = i.next();
            if(cur.name.compareTo("self") == 0){
                continue;
            }
            if(cur.link != null){
                printCodeGenTable(cur.link);
            }
        }
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
}
