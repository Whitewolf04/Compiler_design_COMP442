package code_generator;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import table_generator.SymTabEntry;
import table_generator.SymbolTable;

public class TableConversionVisitor {
    SymbolTable globalTable;
    public CodeGenTable codeTable;

    public TableConversionVisitor(SymbolTable table){
        globalTable = table;
        codeTable = new CodeGenTable();
    }

    public void convertSymbolTable(){
        codeTable = convert(globalTable, null);
    }

    private CodeGenTable convert(SymbolTable table, CodeGenTable upperTable){
        int tempSizeCounter = 0;
        ListIterator<SymTabEntry> i = table.getTable().listIterator();
        CodeGenTable outputTable = new CodeGenTable();
        outputTable.outerTable = upperTable;
        outputTable.name = table.name;

        while(i.hasNext()){
            SymTabEntry cur = i.next();
            
            // Check for the type of symbol table entry to allocate the right size
            if(cur.getKind().equals("variable")){
                // Allocate size for variables
                if(cur.getType().equals("integer")){
                    tempSizeCounter += 4;
                    outputTable.addEntry(new CodeTabEntry(cur, 4, tempSizeCounter*(-1)));
                } else if(cur.getType().equals("float")){
                    tempSizeCounter += 8;
                    outputTable.addEntry(new CodeTabEntry(cur, 8, tempSizeCounter*(-1)));
                } else if(cur.getType().indexOf('[') != -1){
                    // array type, parameter might have no definite size
                    int arraySize = arraySizeCalculator(cur.getType(), outputTable);
                    tempSizeCounter += arraySize;
                    outputTable.addEntry(new CodeTabEntry(cur, arraySize, tempSizeCounter*(-1)));
                } else {
                    // Object type
                    if(cur.getName().equals("self")){
                        outputTable.addEntry(new CodeTabEntry(cur));
                    } else {
                        int objectSize = findObjectSize(cur.getType(), outputTable);
                        tempSizeCounter += objectSize;
                        outputTable.addEntry(new CodeTabEntry(cur, objectSize, tempSizeCounter*(-1)));
                    }
                }
            } else if(cur.getKind().equals("function")){
                // Allocate size for functions
                CodeGenTable functionTable = convert(cur.getLink(), outputTable);
                tempSizeCounter += functionTable.scopeSize;
                outputTable.addEntry(new CodeTabEntry(cur, functionTable.scopeSize, tempSizeCounter*(-1), functionTable));
            } else if(cur.getKind().equals("class")){
                // Allocate size for classes
                CodeGenTable classTable = convert(cur.getLink(), outputTable);
                tempSizeCounter += classTable.scopeSize;
                outputTable.addEntry(new CodeTabEntry(cur, classTable.scopeSize, tempSizeCounter*(-1), classTable));
            }
        }
        outputTable.scopeSize = tempSizeCounter;

        return outputTable;
    }

    private int arraySizeCalculator(String type, CodeGenTable localTable){
        Pattern pattern = Pattern.compile("\\A([^\\[]*)(\\[.*\\])\\Z");
        Matcher matcher = pattern.matcher(type);

        String arrayType = matcher.group(1);
        String dimension = matcher.group(2);
        int size = 0;

        if(arrayType.equals("integer")){
            // Resolve for integer array
            size = 4;
        } else if(arrayType.equals("float")){
            // Resolve for float array
            size = 8;
        } else {
            // Resolve for object array
            size = findObjectSize(arrayType, localTable);
        }
        while(dimension.indexOf('[') != -1){
            String dimensionSize = dimension.substring(dimension.lastIndexOf('[')+1, dimension.lastIndexOf(']'));
            size *= Integer.parseInt(dimensionSize);
            dimension = dimension.substring(0, dimension.lastIndexOf('['));
        }

        return size;
    }

    private int findObjectSize(String type, CodeGenTable localTable){
        CodeTabEntry temp = localTable.containsName(type);
        CodeGenTable curTable = localTable;
        while(temp == null){
            curTable = curTable.outerTable;
            temp = curTable.containsName(type);
        }

        return temp.size;
    }
}
