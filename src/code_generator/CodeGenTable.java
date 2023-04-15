package code_generator;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeGenTable {
    private LinkedList<CodeTabEntry> table;
    public String name;
    public String moonName;
    int scopeSize;
    CodeGenTable outerTable = null;
    LinkedList<CodeTabEntry> litval;
    LinkedList<CodeTabEntry> tempvar;
    LinkedList<CodeTabEntry> statementList;
    LinkedList<CodeTabEntry> tempArrVar;
    LinkedList<CodeTabEntry> parameterList;

    public CodeGenTable(){
        table = new LinkedList<CodeTabEntry>();
        litval = new LinkedList<CodeTabEntry>();
        tempvar = new LinkedList<CodeTabEntry>();
        statementList = new LinkedList<CodeTabEntry>();
        tempArrVar = new LinkedList<CodeTabEntry>();
        parameterList = new LinkedList<CodeTabEntry>();
        scopeSize = 0;
    }

    public void addEntry(CodeTabEntry entry){
        table.add(entry);
    }

    public CodeTabEntry containsName(String name){
        ListIterator<CodeTabEntry> i = table.listIterator();

        while(i.hasNext()){
            CodeTabEntry cur = i.next();
            if(cur.name.compareTo(name) == 0){
                return cur;
            }
        }
        return null;
    }

    public CodeTabEntry containsFunction(String name, String type){
        // For function
        ListIterator<CodeTabEntry> i = table.listIterator();

        while(i.hasNext()){
            CodeTabEntry cur = i.next();
            if(cur.name.equals(name) && compareFunctionTypes(cur.type, type)){
                return cur;
            }
        }
        return null;
    }

    private boolean compareFunctionTypes(String functionType, String inputType){
        if(functionType.indexOf(':') != -1 && functionType.indexOf(':') != -1){
            String funcReturnType = functionType.substring(0, functionType.indexOf(':'));
            String inputReturnType = inputType.substring(0, inputType.indexOf(':'));
            if(!funcReturnType.equals(inputReturnType)){
                return false;
            }

            String funcParamType = functionType.substring(functionType.indexOf(':')+1);
            String inputParamType = inputType.substring(inputType.indexOf(':')+1);
            return compareParamTypes(funcParamType, inputParamType);
        } else if(functionType.indexOf(':') != -1 || functionType.indexOf(':') != -1){
            return false;
        } else {
            return functionType.equals(inputType) ? true : false;
        }
    }

    private boolean compareParamTypes(String originTypes, String paramTypes){
        if(originTypes.equals(paramTypes)){
            return true;
        } else {
            // Might contain array
            String[] origin = originTypes.split(",");
            String[] param = paramTypes.split(",");
            if(origin.length == param.length){
                for(int i = 0; i < origin.length; i++){
                    if(arrayIdentifier(origin[i]) && arrayIdentifier(param[i])){
                        String paramOG = origin[i].replaceAll("\\[[^\\]*]\\]", "[]");
                        String paramIP = param[i].replaceAll("\\[[^\\]*]\\]", "[]");
                        return paramOG.equals(paramIP) ? true : false;
                    } else if(arrayIdentifier(origin[i]) || arrayIdentifier(param[i])){
                        return false;
                    } else {
                        if(origin[i].equals(param[i])){
                            continue;
                        } else {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean arrayIdentifier(String type){
        Pattern pattern = Pattern.compile("\\A.*(\\[.*\\])+\\Z");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()){
            return true;
        } else {
            return false;
        }
    }

    public String printTable(){
        String output = "+----------------------------------------------------------------------------------------------+\n";
        output += String.format("| %-41s %50s |%n", this.name, "Scope size: "+this.scopeSize);
        output += "+----------------------------------------------------------------------------------------------+\n";

        ListIterator<CodeTabEntry> i = table.listIterator();
        while(i.hasNext()){
            CodeTabEntry cur = i.next();
            output += String.format("| %-15s | %-15s | %-40s | %-5s | %-5s |%n", cur.name, cur.kind, cur.type, cur.size, cur.getOffset());
            output += "+----------------------------------------------------------------------------------------------+\n";
        }

        return output;
    }

    public LinkedList<CodeTabEntry> getTable(){
        return this.table;
    }
}
