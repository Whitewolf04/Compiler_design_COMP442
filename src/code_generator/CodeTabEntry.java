package code_generator;


import table_generator.SymTabEntry;

public class CodeTabEntry {
    String name;
    String kind;
    String type;
    int size;
    int offset;
    CodeGenTable link = null;

    public CodeTabEntry(SymTabEntry entry){
        name = entry.getName();
        kind = entry.getKind();
        type = entry.getType();
        size = 0;
        offset = 0;
    }

    public CodeTabEntry(SymTabEntry entry, int size, int offset){
        name = entry.getName();
        kind = entry.getKind();
        type = entry.getType();
        this.size = size;
        this.offset = offset;
    }

    public CodeTabEntry(SymTabEntry entry, int size, int offset, CodeGenTable link){
        name = entry.getName();
        kind = entry.getKind();
        type = entry.getType();
        this.size = size;
        this.offset = offset;
        this.link = link;
    }

    public CodeTabEntry(String name, String kind, String type, int size, int offset){
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.size = size;
        this.offset = offset;
    }

    public int getOffset(){
        return offset*(-1);
    }
}
