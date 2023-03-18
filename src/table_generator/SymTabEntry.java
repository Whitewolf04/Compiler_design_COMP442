package table_generator;

public class SymTabEntry {
    private String name;
    private String kind;
    private String type;
    private SymbolTable link = null;

    public SymTabEntry(String name, String kind, String type){
        this.kind = kind;
        this.type = type;
    }

    public SymTabEntry(String name, String kind, String type, SymbolTable link){
        this.kind = kind;
        this.type = type;
        this.link = link;
    }

    public SymTabEntry(SymTabEntry other){
        this.name = other.name;
        this.kind = other.kind;
        this.type = other.type;
        this.link = other.link;
    }

    public String getName(){
        return name;
    }

    public String getKind(){
        return kind;
    }

    public String getType(){
        return type;
    }

    public SymbolTable getLink(){
        return link;
    }

    public void setLink(SymbolTable link){
        this.link = link;
    }
}
