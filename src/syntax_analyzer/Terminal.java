package syntax_analyzer;

public class Terminal extends GrammarToken {
    String content = null;
    String stringValue;
    TerminalType type;

    public Terminal(String value){
        stringValue = value;
        type = TerminalType.OTHER;
    }

    public Terminal(String value, TerminalType type){
        stringValue = value;
        this.type = type;
    }

    public Terminal(String value, TerminalType type, String content){
        stringValue = value;
        this.type = type;
        this.content = content;
    }

    public boolean equals(Terminal anotherTerminal){
        if(anotherTerminal == null){
            return false;
        }  else if(anotherTerminal.stringValue.equals(this.stringValue)){
            return true;
        } else{
            return false;
        }
    }

    public boolean compareToString(String another){
        return (another.compareToIgnoreCase(this.stringValue)==0);
    }

    public String toString(){
        return stringValue;
    }

    public String getContent(){
        return content;
    }

    static public final Terminal START = new Terminal("$", TerminalType.OTHER);
    static public final Terminal privateW = new Terminal("private", TerminalType.RESERVED);
    static public final Terminal publicW = new Terminal("public", TerminalType.RESERVED);
    static public final Terminal id = new Terminal("id", TerminalType.ID);
    static public final Terminal floatLit = new Terminal("floatLit", TerminalType.FLOAT);
    static public final Terminal intLit = new Terminal("intLit", TerminalType.INT);
    static public final Terminal integerW = new Terminal("integer", TerminalType.RESERVED);
    static public final Terminal floatW = new Terminal("float", TerminalType.RESERVED);
    static public final Terminal semi = new Terminal("semi", TerminalType.SYMBOL);
    static public final Terminal lpar = new Terminal("lpar", TerminalType.SYMBOL);
    static public final Terminal rpar = new Terminal("rpar", TerminalType.SYMBOL);
    static public final Terminal lcurbr = new Terminal("lcurbr", TerminalType.SYMBOL);
    static public final Terminal rcurbr = new Terminal("rcurbr", TerminalType.SYMBOL);
    static public final Terminal minus = new Terminal("minus", TerminalType.SYMBOL);
    static public final Terminal plus = new Terminal("plus", TerminalType.SYMBOL);
    static public final Terminal comma = new Terminal("comma", TerminalType.SYMBOL);
    static public final Terminal geq = new Terminal("geq", TerminalType.SYMBOL);
    static public final Terminal leq = new Terminal("leq", TerminalType.SYMBOL);
    static public final Terminal gt = new Terminal("gt", TerminalType.SYMBOL);
    static public final Terminal lt = new Terminal("lt", TerminalType.SYMBOL);
    static public final Terminal noteq = new Terminal("noteq", TerminalType.SYMBOL);
    static public final Terminal eq = new Terminal("eq", TerminalType.SYMBOL);
    static public final Terminal div = new Terminal("div", TerminalType.SYMBOL);
    static public final Terminal mult = new Terminal("mult", TerminalType.SYMBOL);
    static public final Terminal col = new Terminal("col", TerminalType.SYMBOL);
    static public final Terminal arrow = new Terminal("arrow", TerminalType.SYMBOL);
    static public final Terminal lsqbr = new Terminal("lsqbr", TerminalType.SYMBOL);
    static public final Terminal rsqbr = new Terminal("rsqbr", TerminalType.SYMBOL);
    static public final Terminal dot = new Terminal("dot", TerminalType.SYMBOL);
    static public final Terminal sr = new Terminal("sr", TerminalType.SYMBOL);
    static public final Terminal assign = new Terminal("assign", TerminalType.SYMBOL);
    static public final Terminal returnW = new Terminal("return", TerminalType.RESERVED);
    static public final Terminal writeW = new Terminal("write", TerminalType.RESERVED);
    static public final Terminal readW = new Terminal("read", TerminalType.RESERVED);
    static public final Terminal whileW = new Terminal("while", TerminalType.RESERVED);
    static public final Terminal elseW = new Terminal("else", TerminalType.RESERVED);
    static public final Terminal thenW = new Terminal("then", TerminalType.RESERVED);
    static public final Terminal ifW = new Terminal("if", TerminalType.RESERVED);
    static public final Terminal voidW = new Terminal("void", TerminalType.RESERVED);
    static public final Terminal isaW = new Terminal("isa", TerminalType.RESERVED);
    static public final Terminal andW = new Terminal("and", TerminalType.RESERVED);
    static public final Terminal attributeW = new Terminal("attribute", TerminalType.RESERVED);
    static public final Terminal constructorW = new Terminal("constructor", TerminalType.RESERVED);
    static public final Terminal functionW = new Terminal("function", TerminalType.RESERVED);
    static public final Terminal localvarW = new Terminal("localvar", TerminalType.RESERVED);
    static public final Terminal notW = new Terminal("not", TerminalType.RESERVED);
    static public final Terminal classW = new Terminal("class", TerminalType.RESERVED);
    static public final Terminal orW = new Terminal("or", TerminalType.RESERVED);
    static public final Terminal EPSILON = new Terminal("EPSILON", TerminalType.EPSILON);
}
