package table_generator;

import java.util.LinkedList;
import java.util.ListIterator;

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
            if(cur.getName().equals(name) && cur.getType().equals(type)){
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
                if(types.equals(paramTypes) && cur.getName().equals(name)){
                    return cur;
                }
            }
            return null;
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
        String output = "+----------------------------------------------+\n";
        output += String.format("| %-30s |%n", this.name);
        output += "+----------------------------------------------+\n";

        ListIterator<SymTabEntry> i = table.listIterator();
        while(i.hasNext()){
            SymTabEntry cur = i.next();
            SymbolTable link = cur.getLink();
            if(link == null){
                output += String.format("| %-6s | %-6s | %-10s | %-8s |%n", cur.getName(), cur.getKind(), cur.getType(), "");
            } else {
                output += String.format("| %-6s | %-6s | %-10s | %-8s |%n", cur.getName(), cur.getKind(), cur.getType(), cur.getLink().name);
            }
            output += "+----------------------------------------------+\n";
        }

        return output;
    }

}
