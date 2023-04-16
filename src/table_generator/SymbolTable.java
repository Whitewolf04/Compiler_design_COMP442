package table_generator;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolTable {
    private LinkedList<SymTabEntry> table;
    public SymbolTable outerTable;
    public String name;

    public SymbolTable(){
        table = new LinkedList<SymTabEntry>();
    }

    public SymbolTable(String name){
        table = new LinkedList<SymTabEntry>();
        this.name = name;
    }

    public LinkedList<SymTabEntry> getTable(){
        return table;
    }

    public void addEntry(SymTabEntry entry){
        table.add(entry);
    }

    public SymTabEntry containsFunction(String name, String type){
        // For function
        ListIterator<SymTabEntry> i = table.listIterator();

        while(i.hasNext()){
            SymTabEntry cur = i.next();
            if(cur.getName().equals(name) && compareFunctionTypes(cur.getType(), type)){
                return cur;
            }
        }
        return null;
    }

    public SymTabEntry containsParams(String name, String paramTypes){
        ListIterator<SymTabEntry> i = table.listIterator();

        if(paramTypes.isEmpty()){
            while(i.hasNext()){
                SymTabEntry cur = i.next();
                if(cur.getType().indexOf(':') == -1 && cur.getName().equals(name)){
                    return cur;
                }
            }
            return null;
        } else {
            while(i.hasNext()){
                SymTabEntry cur = i.next();
                if(cur.getType().indexOf(':') == -1){
                    continue;
                }
                String types = cur.getType().split(":")[1];
                if(compareParamTypes(types, paramTypes) && cur.getName().equals(name)){
                    return cur;
                }
            }
            return null;
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

    private boolean arrayIdentifier(String type){
        Pattern pattern = Pattern.compile("\\A.*(\\[.*\\])+\\Z");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()){
            return true;
        } else {
            return false;
        }
    }

    /*
     * Returns the first symbol table entry that is found
     */
    public SymTabEntry containsName(String name){
        ListIterator<SymTabEntry> i = table.listIterator();

        while(i.hasNext()){
            SymTabEntry cur = i.next();
            if(cur.getName().compareTo(name) == 0){
                return cur;
            }
        }
        return null;
    }

    public void pushToTable(SymbolTable other){
        while(table.peek() != null){
            SymTabEntry cur = table.poll();
            other.addEntry(cur);
        }
    }

    public void copyMemberToTable(SymbolTable other){
        ListIterator<SymTabEntry> i = table.listIterator();
        while(i.hasNext()){
            SymTabEntry cur = i.next();
            if(cur.getName().compareTo("self") == 0){
                // Only copy functions and variables that are not self
                cur = i.next();
                continue;
            } else if(cur.getKind().compareTo("function")==0){
                SymTabEntry duplicate = other.containsFunction(cur.getName(), cur.getType());
                if(duplicate != null){
                    duplicate.setLink(cur.getLink());
                } else {
                    other.addEntry(cur);
                }
            } else if(cur.getKind().compareTo("variable")==0){
                SymTabEntry duplicate = other.containsName(cur.getName());
                if(duplicate != null){
                    duplicate.setType(cur.getType());
                } else {
                    other.addEntry(cur);
                }
            }
        }
    }

    public boolean isEmpty(){
        return table.isEmpty();
    }

    public String printTable(){
        String output = "+----------------------------------------------------------------------------------------------------------------------------+\n";
        output += String.format("| %-122s |%n", this.name);
        output += "+----------------------------------------------------------------------------------------------------------------------------+\n";

        ListIterator<SymTabEntry> i = table.listIterator();
        while(i.hasNext()){
            SymTabEntry cur = i.next();
            SymbolTable link = cur.getLink();
            if(link == null){
                output += String.format("| %-10s | %-15s | %-15s | %-40s | %-30s |%n", cur.getVisibility(), cur.getName(), cur.getKind(), cur.getType(), "");
            } else {
                output += String.format("| %-10s | %-15s | %-15s | %-40s | %-30s |%n", cur.getVisibility(), cur.getName(), cur.getKind(), cur.getType(), cur.getLink().name);
            }
            output += "+----------------------------------------------------------------------------------------------------------------------------+\n";
        }

        return output;
    }

    public boolean inClass(SymbolTable classTable){
        // Must be a member function
        if(this.name.indexOf("::") == -1){
            return false;
        }

        String className = this.name.substring(0, this.name.indexOf("::"));
        if(className.equals(classTable.name)){
            return true;
        }
        return false;
    }

}
