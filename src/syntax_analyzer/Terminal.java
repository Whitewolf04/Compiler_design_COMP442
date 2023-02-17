package syntax_analyzer;

public class Terminal extends GrammarToken {
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
        return another.equals(this.stringValue);
    }

    public String toString(){
        return stringValue;
    }

    static Terminal privateW = new Terminal("private", TerminalType.RESERVED);
    static Terminal publicW = new Terminal("public", TerminalType.RESERVED);
    static Terminal id = new Terminal("id", TerminalType.ID);
    static Terminal floatLit = new Terminal("float", TerminalType.FLOAT);
    static Terminal intLit = new Terminal("integer", TerminalType.INT);
    static Terminal semi = new Terminal("semicol", TerminalType.SYMBOL);
    static Terminal lpar = new Terminal("lpar", TerminalType.SYMBOL);
    static Terminal rpar = new Terminal("rpar", TerminalType.SYMBOL);
    static Terminal lcurbr = new Terminal("lcurbr", TerminalType.SYMBOL);
    static Terminal rcurbr = new Terminal("rcurbr", TerminalType.SYMBOL);
    static Terminal minus = new Terminal("minus", TerminalType.SYMBOL);
    static Terminal plus = new Terminal("plus", TerminalType.SYMBOL);
    static Terminal comma = new Terminal("comma", TerminalType.SYMBOL);
    static Terminal geq = new Terminal("geq", TerminalType.SYMBOL);
    static Terminal leq = new Terminal("leq", TerminalType.SYMBOL);
    static Terminal gt = new Terminal("gt", TerminalType.SYMBOL);
    static Terminal lt = new Terminal("lt", TerminalType.SYMBOL);
    static Terminal noteq = new Terminal("noteq", TerminalType.SYMBOL);
    static Terminal eq = new Terminal("eq", TerminalType.SYMBOL);
    static Terminal div = new Terminal("div", TerminalType.SYMBOL);
    static Terminal mult = new Terminal("mult", TerminalType.SYMBOL);
    static Terminal col = new Terminal("col", TerminalType.SYMBOL);
    static Terminal arrow = new Terminal("arrow", TerminalType.SYMBOL);
    static Terminal lsqbr = new Terminal("lsqbr", TerminalType.SYMBOL);
    static Terminal rsqbr = new Terminal("rsqbr", TerminalType.SYMBOL);
    static Terminal dot = new Terminal("dot", TerminalType.SYMBOL);
    static Terminal sr = new Terminal("sr", TerminalType.SYMBOL);
    static Terminal assign = new Terminal("assign", TerminalType.SYMBOL);
    static Terminal returnW = new Terminal("return", TerminalType.RESERVED);
    static Terminal writeW = new Terminal("write", TerminalType.RESERVED);
    static Terminal readW = new Terminal("read", TerminalType.RESERVED);
    static Terminal whileW = new Terminal("while", TerminalType.RESERVED);
    static Terminal elseW = new Terminal("else", TerminalType.RESERVED);
    static Terminal thenW = new Terminal("then", TerminalType.RESERVED);
    static Terminal ifW = new Terminal("if", TerminalType.RESERVED);
    static Terminal voidW = new Terminal("void", TerminalType.RESERVED);
    static Terminal isaW = new Terminal("isa", TerminalType.RESERVED);
    static Terminal andW = new Terminal("and", TerminalType.RESERVED);
    static Terminal attributeW = new Terminal("attribute", TerminalType.RESERVED);
    static Terminal constructorW = new Terminal("constructor", TerminalType.RESERVED);
    static Terminal functionW = new Terminal("function", TerminalType.RESERVED);
    static Terminal localvarW = new Terminal("localvar", TerminalType.RESERVED);
    static Terminal notW = new Terminal("not", TerminalType.RESERVED);
    static Terminal classW = new Terminal("class", TerminalType.RESERVED);
    static Terminal orW = new Terminal("or", TerminalType.RESERVED);
}
