package table_generator;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, SymTabEntry> table;
    public String name;

    public SymbolTable(){
        table = new HashMap<String, SymTabEntry>();
    }

    public SymbolTable(String name){
        table = new HashMap<String, SymTabEntry>();
        this.name = name;
    }

    public HashMap<String, SymTabEntry> getTable(){
        return table;
    }

    public void addEntry(String name, SymTabEntry entry){
        table.put(name, entry);
    }

    public SymTabEntry getEntry(String name){
        return table.get(name);
    }

    public void pushToTable(SymbolTable other){
        for(String key : table.keySet()){
            other.addEntry(key, table.get(key));
        }
    }

    public boolean isEmpty(){
        return table.isEmpty();
    }
}
