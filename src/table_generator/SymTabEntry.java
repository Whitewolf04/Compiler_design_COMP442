package table_generator;

public class SymTabEntry {
    private String name;
    private String kind;
    private String type;
    private boolean publicVis = true;
    private SymbolTable link = null;

    public SymTabEntry(String name, String kind, String type){
        this.name = name;
        this.kind = kind;
        this.type = type;
    }

    public SymTabEntry(String name, String kind, String type, SymbolTable link){
        this.name = name;
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

    public String getReturnType(){
        if(type.indexOf(':') == -1){
            return type;
        } else {
            String returnType = type.substring(0, type.indexOf(':'));
            return returnType;
        }
    }

    public SymbolTable getLink(){
        return link;
    }

    public String getVisibility(){
        if(publicVis){
            return "public";
        } else {
            return "private";
        }
    }

    public boolean isPublic(){
        return publicVis;
    }

    public void setLink(SymbolTable link){
        this.link = link;
    }

    public void setKind(String kind){
        this.kind = kind;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setVisibility(String visibility){
        if(visibility.compareTo("public") == 0){
            publicVis = true;
        } else if(visibility.compareTo("private") == 0){
            publicVis = false;
        } else{
            System.out.println("Visibility error!");
        }
    }
}
