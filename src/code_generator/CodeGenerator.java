package code_generator;

import java.util.ListIterator;

import table_generator.SymbolTableGenerator;

public class CodeGenerator {
    public static CodeGenTable globalTable;

    public static void createTable(){
        TableConversionVisitor tableConverter = new TableConversionVisitor(SymbolTableGenerator.globalTable);
        tableConverter.convertSymbolTable();
        printCodeGenTable(tableConverter.codeTable);
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
}
