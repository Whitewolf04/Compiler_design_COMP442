package table_generator;

import java.util.LinkedList;
import java.util.ListIterator;

public class SymbolTable {
    private LinkedList<SymTabEntry> table;
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

    public SymTabEntry contains(String name, String type){
        ListIterator<SymTabEntry> i = table.listIterator();

        while(i.hasNext()){
            SymTabEntry cur = i.next();
            if(cur.getName().compareTo(name) == 0 && cur.getType().compareTo(type) == 0){
                return cur;
            }
        }
        return null;
    }

    public SymTabEntry accessClass(String name){
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
            if(cur.getName().compareTo("self") != 0){
                // Only copy functions and variables that are not self
                other.addEntry(new SymTabEntry(cur));
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
