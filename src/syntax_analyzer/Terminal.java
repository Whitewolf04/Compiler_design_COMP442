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

    static final Terminal privateW = new Terminal("private", TerminalType.RESERVED);
    static final Terminal publicW = new Terminal("public", TerminalType.RESERVED);
    static final Terminal id = new Terminal("id", TerminalType.ID);
    static final Terminal floatLit = new Terminal("float", TerminalType.FLOAT);
    static final Terminal intLit = new Terminal("integer", TerminalType.INT);
    static final Terminal semi = new Terminal("semicol", TerminalType.SYMBOL);
    static final Terminal lpar = new Terminal("lpar", TerminalType.SYMBOL);
    static final Terminal rpar = new Terminal("rpar", TerminalType.SYMBOL);
    static final Terminal lcurbr = new Terminal("lcurbr", TerminalType.SYMBOL);
    static final Terminal rcurbr = new Terminal("rcurbr", TerminalType.SYMBOL);
    static final Terminal minus = new Terminal("minus", TerminalType.SYMBOL);
    static final Terminal plus = new Terminal("plus", TerminalType.SYMBOL);
    static final Terminal comma = new Terminal("comma", TerminalType.SYMBOL);
    static final Terminal geq = new Terminal("geq", TerminalType.SYMBOL);
    static final Terminal leq = new Terminal("leq", TerminalType.SYMBOL);
    static final Terminal gt = new Terminal("gt", TerminalType.SYMBOL);
    static final Terminal lt = new Terminal("lt", TerminalType.SYMBOL);
    static final Terminal noteq = new Terminal("noteq", TerminalType.SYMBOL);
    static final Terminal eq = new Terminal("eq", TerminalType.SYMBOL);
    static final Terminal div = new Terminal("div", TerminalType.SYMBOL);
    static final Terminal mult = new Terminal("mult", TerminalType.SYMBOL);
    static final Terminal col = new Terminal("col", TerminalType.SYMBOL);
    static final Terminal arrow = new Terminal("arrow", TerminalType.SYMBOL);
    static final Terminal lsqbr = new Terminal("lsqbr", TerminalType.SYMBOL);
    static final Terminal rsqbr = new Terminal("rsqbr", TerminalType.SYMBOL);
    static final Terminal dot = new Terminal("dot", TerminalType.SYMBOL);
    static final Terminal sr = new Terminal("sr", TerminalType.SYMBOL);
    static final Terminal assign = new Terminal("assign", TerminalType.SYMBOL);
    static final Terminal returnW = new Terminal("return", TerminalType.RESERVED);
    static final Terminal writeW = new Terminal("write", TerminalType.RESERVED);
    static final Terminal readW = new Terminal("read", TerminalType.RESERVED);
    static final Terminal whileW = new Terminal("while", TerminalType.RESERVED);
    static final Terminal elseW = new Terminal("else", TerminalType.RESERVED);
    static final Terminal thenW = new Terminal("then", TerminalType.RESERVED);
    static final Terminal ifW = new Terminal("if", TerminalType.RESERVED);
    static final Terminal voidW = new Terminal("void", TerminalType.RESERVED);
    static final Terminal isaW = new Terminal("isa", TerminalType.RESERVED);
    static final Terminal andW = new Terminal("and", TerminalType.RESERVED);
    static final Terminal attributeW = new Terminal("attribute", TerminalType.RESERVED);
    static final Terminal constructorW = new Terminal("constructor", TerminalType.RESERVED);
    static final Terminal functionW = new Terminal("function", TerminalType.RESERVED);
    static final Terminal localvarW = new Terminal("localvar", TerminalType.RESERVED);
    static final Terminal notW = new Terminal("not", TerminalType.RESERVED);
    static final Terminal classW = new Terminal("class", TerminalType.RESERVED);
    static final Terminal orW = new Terminal("or", TerminalType.RESERVED);
    static final Terminal EPSILON = new Terminal("epsilon", TerminalType.EPSILON);
}
