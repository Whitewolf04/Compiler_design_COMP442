package syntax_analyzer;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public final class ParsingTable {
    static HashMap<Terminal, HashMap<NonTerminal, Stack<NonTerminal>>> table;
    static Scanner scanner;

    public static void loadTable(){
        // NonTerminal addOp = new NonTerminal(null, null);
    }

    public static void initTerminals(){
        NonTerminal addOp = new NonTerminal(null, null);
        addOp.tableEntry.put("-", new Stack<GrammarToken>(){{push(Terminal.minus);}});
        addOp.tableEntry.put("+", new Stack<GrammarToken>(){{push(Terminal.plus);}});
        addOp.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.orW);}});

        NonTerminal arraySize1 = new NonTerminal(null, null);
        arraySize1.tableEntry.put("intlit", new Stack<GrammarToken>(){{push(Terminal.intLit); push(Terminal.rsqbr);}});
        arraySize1.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.rsqbr);}});

        NonTerminal arraySize = new NonTerminal(null, null);
        arraySize.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(Terminal.lsqbr); push(arraySize1);}});

        NonTerminal assignOp = new NonTerminal(null, null);
        assignOp.tableEntry.put("equal", new Stack<GrammarToken>(){{push(Terminal.assign);}});

        NonTerminal multOp = new NonTerminal(null, null);
        multOp.tableEntry.put("and", new Stack<GrammarToken>(){{push(Terminal.andW);}});
        multOp.tableEntry.put("div", new Stack<GrammarToken>(){{push(Terminal.div);}});
        multOp.tableEntry.put("mult", new Stack<GrammarToken>(){{push(Terminal.mult);}});

        NonTerminal relOp = new NonTerminal(null, null);
        relOp.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.eq);}});
        relOp.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.noteq);}});
        relOp.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.lt);}});
        relOp.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.gt);}});
        relOp.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.leq);}});
        relOp.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.geq);}});

        NonTerminal sign = new NonTerminal(null, null);
        sign.tableEntry.put("plus", new Stack<GrammarToken>(){{push(new Terminal("plus", TerminalType.SYMBOL));}});
        sign.tableEntry.put("minus", new Stack<GrammarToken>(){{push(new Terminal("minus", TerminalType.SYMBOL));}});

        NonTerminal type = new NonTerminal(null, null);
        type.tableEntry.put("integer", new Stack<GrammarToken>(){{push(new Terminal("integer", TerminalType.INT));}});
        type.tableEntry.put("float", new Stack<GrammarToken>(){{push(new Terminal("float", TerminalType.FLOAT));}});
        type.tableEntry.put("id", new Stack<GrammarToken>(){{push(new Terminal("id", TerminalType.ID));}});

        NonTerminal returnType = new NonTerminal(null, null);
        returnType.tableEntry.put("id", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("float", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("integer", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("void", new Stack<GrammarToken>(){{push(new Terminal("void", TerminalType.RESERVED));}});

        NonTerminal visibility = new NonTerminal(null, null);
        visibility.tableEntry.put("public", new Stack<GrammarToken>(){{push(new Terminal("public", TerminalType.RESERVED));}});
        visibility.tableEntry.put("private", new Stack<GrammarToken>(){{push(new Terminal("private", TerminalType.RESERVED));}});
        visibility.tableEntry.put("epsilon", new Stack<GrammarToken>(){{push(new Terminal("epsilon", TerminalType.EPSILON));}});

        NonTerminal reptClassDecl5 = new NonTerminal(null, null);
        reptClassDecl5.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(new Terminal("epsilon", TerminalType.EPSILON));}});
        reptClassDecl5.tableEntry.put("comma", new Stack<GrammarToken>(){{push(new Terminal("comma", TerminalType.SYMBOL));}});

    }
}
