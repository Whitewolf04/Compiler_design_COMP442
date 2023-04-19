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
        codeTable = convert(globalTable, null, null);
    }

    private CodeGenTable convert(SymbolTable table, CodeGenTable upperTable, String isFunction){
        int tempSizeCounter = 4;
        ListIterator<SymTabEntry> i = table.getTable().listIterator();
        CodeGenTable outputTable = new CodeGenTable();
        outputTable.outerTable = upperTable;
        outputTable.name = table.name;

        if(isFunction != null){
            if(!isFunction.equals("void")){
                int returnSize = convertSize(isFunction, outputTable);

                outputTable.addEntry(new CodeTabEntry("return", "retVal", isFunction, returnSize, tempSizeCounter));
                tempSizeCounter += returnSize;
            } else {
                // Skip through void
            }
        }

        while(i.hasNext()){
            SymTabEntry cur = i.next();
            
            // Check for the type of symbol table entry to allocate the right size
            if(cur.getKind().equals("variable")){
                // Allocate size for variables and parameters
                if(cur.getType().equals("integer")){
                    outputTable.addEntry(new CodeTabEntry(cur, 4, tempSizeCounter));
                    tempSizeCounter += 4;
                } else if(cur.getType().equals("float")){
                    outputTable.addEntry(new CodeTabEntry(cur, 8, tempSizeCounter));
                    tempSizeCounter += 8;
                } else if(cur.getType().indexOf('[') != -1){
                    // array type, parameter might have no definite size
                    int arraySize = arraySizeCalculator(cur.getType(), outputTable);
                    outputTable.addEntry(new CodeTabEntry(cur, arraySize, tempSizeCounter));
                    tempSizeCounter += arraySize;
                } else {
                    // Object type
                    if(cur.getName().equals("self")){
                        outputTable.addEntry(new CodeTabEntry(cur));
                    } else {
                        int objectSize = findObjectSize(cur.getType(), outputTable);
                        outputTable.addEntry(new CodeTabEntry(cur, objectSize, tempSizeCounter));
                        tempSizeCounter += objectSize;
                    }
                }
            } else if(cur.getKind().equals("parameter")){
                CodeTabEntry newEntry = null;
                if(cur.getType().equals("integer")){
                    newEntry = new CodeTabEntry(cur, 4, tempSizeCounter);
                    outputTable.addEntry(newEntry);
                    outputTable.parameterList.add(newEntry);
                    tempSizeCounter += 4;
                } else if(cur.getType().equals("float")){
                    newEntry = new CodeTabEntry(cur, 8, tempSizeCounter);
                    outputTable.addEntry(newEntry);
                    outputTable.parameterList.add(newEntry);
                    tempSizeCounter += 8;
                } else if(cur.getType().indexOf('[') != -1){
                    int arraySize = arraySizeCalculator(cur.getType(), outputTable);
                    if(arraySize == -1){
                        // Skip through array-type parameter that has no definite size
                        continue;
                    } else {
                        newEntry = new CodeTabEntry(cur, arraySize, tempSizeCounter);
                        outputTable.addEntry(newEntry);
                        outputTable.parameterList.add(newEntry);
                        tempSizeCounter += arraySize;
                    }
                } else {
                    // Object type
                    int objectSize = findObjectSize(cur.getType(), outputTable);
                    newEntry = new CodeTabEntry(cur, objectSize, tempSizeCounter);
                    outputTable.addEntry(newEntry);
                    outputTable.parameterList.add(newEntry);
                    tempSizeCounter += objectSize;
                }
            } else if(cur.getKind().equals("function")){
                // Allocate size for functions
                String returnType = cur.getReturnType();
                CodeGenTable functionTable = convert(cur.getLink(), outputTable, returnType);

                // Add this entry to the output table
                outputTable.addEntry(new CodeTabEntry(cur, functionTable.scopeSize, tempSizeCounter, functionTable));
            } else if(cur.getKind().equals("class")){
                // Allocate size for classes
                CodeGenTable classTable = convert(cur.getLink(), outputTable, null);
                outputTable.addEntry(new CodeTabEntry(cur, classTable.scopeSize, tempSizeCounter, classTable));
            }
        }
        outputTable.scopeSize = tempSizeCounter;

        return outputTable;
    }

    private int arraySizeCalculator(String type, CodeGenTable localTable){
        Pattern pattern = Pattern.compile("\\A([^\\[]*){1}(\\[.*\\])\\Z");
        Matcher matcher = pattern.matcher(type);

        matcher.find();
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
            if(dimensionSize.isEmpty()){
                return -1;
            } else {
                size *= Integer.parseInt(dimensionSize);
                dimension = dimension.substring(0, dimension.lastIndexOf('['));
            }
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

    private int convertSize(String type, CodeGenTable localTable){
        if(type.equals("integer")){
            return 4;
        } else if(type.equals("float")){
            return 8;
        } else {
            // Handle type for object
            return findObjectSize(type, localTable);
        }
    }
}
