package code_generator;

import java.util.LinkedList;
import java.util.ListIterator;

public class CodeGenTable {
    private LinkedList<CodeTabEntry> table;
    public String name;
    int scopeSize;
    CodeGenTable outerTable = null;

    public CodeGenTable(){
        table = new LinkedList<CodeTabEntry>();
        scopeSize = 0;
    }

    public void addEntry(CodeTabEntry entry){
        table.add(entry);
        this.scopeSize += entry.size;
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

    public String printTable(){
        String output = "+----------------------------------------------------------------------------------------------+\n";
        output += String.format("| %-41s %50s |%n", this.name, "Scope size: "+this.scopeSize);
        output += "+----------------------------------------------------------------------------------------------+\n";

        ListIterator<CodeTabEntry> i = table.listIterator();
        while(i.hasNext()){
            CodeTabEntry cur = i.next();
            output += String.format("| %-15s | %-15s | %-40s | %-5s | %-5s |%n", cur.name, cur.kind, cur.type, cur.size, cur.offset);
            output += "+----------------------------------------------------------------------------------------------+\n";
        }

        return output;
    }

    public LinkedList<CodeTabEntry> getTable(){
        return this.table;
    }
}
