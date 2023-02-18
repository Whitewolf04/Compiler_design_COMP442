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
        NonTerminal START = new NonTerminal(null, null);
        NonTerminal aParams = new NonTerminal(null, null);
        NonTerminal aParamsTail = new NonTerminal(null, null);
        NonTerminal addOp = new NonTerminal(null, null);
        NonTerminal arithExpr = new NonTerminal(null, null);
        NonTerminal arraySize = new NonTerminal(null, null);
        NonTerminal arraySize1 = new NonTerminal(null, null);
        NonTerminal assignOp = new NonTerminal(null, null);
        NonTerminal assignStat = new NonTerminal(null, null);
        NonTerminal classDecl = new NonTerminal(null, null);
        NonTerminal classDeclOrFuncDef = new NonTerminal(null, null);
        NonTerminal expr = new NonTerminal(null, null);
        NonTerminal expr1 = new NonTerminal(null, null);
        NonTerminal fParams = new NonTerminal(null, null);
        NonTerminal fParamsTail = new NonTerminal(null, null);
        NonTerminal factor = new NonTerminal(null, null);
        NonTerminal factor1 = new NonTerminal(null, null);
        NonTerminal factor2 = new NonTerminal(null, null);
        NonTerminal factor3 = new NonTerminal(null, null);
        NonTerminal funcBody = new NonTerminal(null, null);
        NonTerminal funcDef = new NonTerminal(null, null);
        NonTerminal funcHead = new NonTerminal(null, null);
        NonTerminal funcHead1 = new NonTerminal(null, null);
        NonTerminal funcHead2 = new NonTerminal(null, null);
        NonTerminal functionCall = new NonTerminal(null, null);
        NonTerminal functionCall1 = new NonTerminal(null, null);
        NonTerminal idnest = new NonTerminal(null, null);
        NonTerminal idnest1 = new NonTerminal(null, null);
        NonTerminal idnest2 = new NonTerminal(null, null);
        NonTerminal indice = new NonTerminal(null, null);
        NonTerminal localVarDecl = new NonTerminal(null, null);
        NonTerminal localVarDecl1 = new NonTerminal(null, null);
        NonTerminal localVarDecl2 = new NonTerminal(null, null);
        NonTerminal localVarDeclOrStmt = new NonTerminal(null, null);
        NonTerminal memberDecl = new NonTerminal(null, null);
        NonTerminal memberFuncDecl = new NonTerminal(null, null);
        NonTerminal memberVarDecl = new NonTerminal(null, null);
        NonTerminal multOp = new NonTerminal(null, null);
        NonTerminal optClassDecl2 = new NonTerminal(null, null);
        NonTerminal relExpr = new NonTerminal(null, null);
        NonTerminal relOp = new NonTerminal(null, null);
        NonTerminal reptSTART0 = new NonTerminal(null, null);
        NonTerminal reptAParams1 = new NonTerminal(null, null);
        NonTerminal reptClassDecl5 = new NonTerminal(null, null);
        NonTerminal reptClassDecl8 = new NonTerminal(null, null);
        NonTerminal reptFParams3 = new NonTerminal(null, null);
        NonTerminal reptFParams4 = new NonTerminal(null, null);
        NonTerminal reptFParamsTail4 = new NonTerminal(null, null);
        NonTerminal reptFuncBody1 = new NonTerminal(null, null);
        NonTerminal reptIdnest = new NonTerminal(null, null);
        NonTerminal reptIdnest0 = new NonTerminal(null, null);
        NonTerminal reptIdnest1 = new NonTerminal(null, null);
        NonTerminal reptLocalVarDecl4 = new NonTerminal(null, null);
        NonTerminal reptMemberVarDecl4 = new NonTerminal(null, null);
        NonTerminal reptStatBlock1 = new NonTerminal(null, null);
        NonTerminal reptVariable2 = new NonTerminal(null, null);
        NonTerminal reptVariable3 = new NonTerminal(null, null);
        NonTerminal returnType = new NonTerminal(null, null);
        NonTerminal rightRecArithExpr = new NonTerminal(null, null);
        NonTerminal rightRecTerm = new NonTerminal(null, null);
        NonTerminal sign = new NonTerminal(null, null);
        NonTerminal statBlock = new NonTerminal(null, null);
        NonTerminal statement = new NonTerminal(null, null);
        NonTerminal statement0 = new NonTerminal(null, null);
        NonTerminal term = new NonTerminal(null, null);
        NonTerminal type = new NonTerminal(null, null);
        NonTerminal variable = new NonTerminal(null, null);
        NonTerminal visibility = new NonTerminal(null, null);

        START.tableEntry.put("function", new Stack<GrammarToken>(){{push(reptSTART0);}});
        START.tableEntry.put("class", new Stack<GrammarToken>(){{push(reptSTART0);}});

        aParams.tableEntry.put("id", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("minus", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("plus", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("not", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("float", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("integer", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});

        aParamsTail.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(expr);}});

        addOp.tableEntry.put("-", new Stack<GrammarToken>(){{push(Terminal.minus);}});
        addOp.tableEntry.put("+", new Stack<GrammarToken>(){{push(Terminal.plus);}});
        addOp.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.orW);}});

        arithExpr.tableEntry.put("id", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("not", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("float", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("integer", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});

        arraySize.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(Terminal.lsqbr); push(arraySize1);}});

        arraySize1.tableEntry.put("intlit", new Stack<GrammarToken>(){{push(Terminal.intLit); push(Terminal.rsqbr);}});
        arraySize1.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.rsqbr);}});

        assignOp.tableEntry.put("equal", new Stack<GrammarToken>(){{push(Terminal.assign);}});

        assignStat.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(functionCall);}});
        assignStat.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(variable); push(assignOp); push(expr);}});

        classDecl.tableEntry.put("class", new Stack<GrammarToken>(){{push(Terminal.classW); push(Terminal.id); push(optClassDecl2); push(Terminal.lcurbr); push(reptClassDecl8); push(Terminal.rcurbr); push(Terminal.semi);}});

        classDeclOrFuncDef.tableEntry.put("class", new Stack<GrammarToken>(){{push(classDecl);}});
        classDeclOrFuncDef.tableEntry.put("function", new Stack<GrammarToken>(){{push(funcDef);}});

        expr.tableEntry.put("id", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});
        expr.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});
        expr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});
        expr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});
        expr.tableEntry.put("not", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});
        expr.tableEntry.put("float", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});
        expr.tableEntry.put("integer", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});

        expr1.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        expr1.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        expr1.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        expr1.tableEntry.put("geq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr);}});
        expr1.tableEntry.put("leq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr);}});
        expr1.tableEntry.put("gt", new Stack<GrammarToken>(){{push(relOp); push(arithExpr);}});
        expr1.tableEntry.put("lt", new Stack<GrammarToken>(){{push(relOp); push(arithExpr);}});
        expr1.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr);}});
        expr1.tableEntry.put("eq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr);}});

        factor.tableEntry.put("id", new Stack<GrammarToken>(){{push(reptIdnest); push(Terminal.id); push(factor1);}});
        factor.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(arithExpr); push(Terminal.rpar);}});
        factor.tableEntry.put("minus", new Stack<GrammarToken>(){{push(sign); push(factor);}});
        factor.tableEntry.put("plus", new Stack<GrammarToken>(){{push(sign); push(factor);}});
        factor.tableEntry.put("not", new Stack<GrammarToken>(){{push(Terminal.notW); push(factor);}});
        factor.tableEntry.put("float", new Stack<GrammarToken>(){{push(Terminal.floatLit);}});
        factor.tableEntry.put("integer", new Stack<GrammarToken>(){{push(Terminal.intLit);}});

        multOp.tableEntry.put("and", new Stack<GrammarToken>(){{push(Terminal.andW);}});
        multOp.tableEntry.put("div", new Stack<GrammarToken>(){{push(Terminal.div);}});
        multOp.tableEntry.put("mult", new Stack<GrammarToken>(){{push(Terminal.mult);}});

        relOp.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.eq);}});
        relOp.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.noteq);}});
        relOp.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.lt);}});
        relOp.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.gt);}});
        relOp.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.leq);}});
        relOp.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.geq);}});

        sign.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.plus);}});
        sign.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.minus);}});

        type.tableEntry.put("integer", new Stack<GrammarToken>(){{push(Terminal.intLit);}});
        type.tableEntry.put("float", new Stack<GrammarToken>(){{push(Terminal.floatLit);}});
        type.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id);}});

        returnType.tableEntry.put("id", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("float", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("integer", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("void", new Stack<GrammarToken>(){{push(Terminal.voidW);}});

        visibility.tableEntry.put("public", new Stack<GrammarToken>(){{push(Terminal.publicW);}});
        visibility.tableEntry.put("private", new Stack<GrammarToken>(){{push(Terminal.privateW);}});
        visibility.tableEntry.put("epsilon", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});

        reptClassDecl5.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptClassDecl5.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma);}});

        reptFParams3.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParams3.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParams3.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptFParams3);}});

        reptFParamsTail4.tableEntry.put("rpar", new Stack<GrammarToken >(){{push(Terminal.EPSILON);}});
        reptFParamsTail4.tableEntry.put("comma", new Stack<GrammarToken >(){{push(Terminal.EPSILON);}});
        reptFParamsTail4.tableEntry.put("lsqbr", new Stack<GrammarToken >(){{push(arraySize); push(reptFParamsTail4);}});

        fParamsTail.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(Terminal.id); push(Terminal.col); push(type); push(reptFParamsTail4);}});

        reptFParams4.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParams4.tableEntry.put("comma", new Stack<GrammarToken>(){{push(fParamsTail); push(reptFParams4);}});

        reptLocalVarDecl4.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptLocalVarDecl4.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptLocalVarDecl4);}});

        reptMemberVarDecl4.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptMemberVarDecl4.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptMemberVarDecl4);}});


        rightRecTerm.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecTerm.tableEntry.put("and", new Stack<GrammarToken>(){{push(multOp); push(factor); push(rightRecTerm);}});
        rightRecTerm.tableEntry.put("div", new Stack<GrammarToken>(){{push(multOp); push(factor); push(rightRecTerm);}});
        rightRecTerm.tableEntry.put("mult", new Stack<GrammarToken>(){{push(multOp); push(factor); push(rightRecTerm);}});

        term.tableEntry.put("id", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("minus", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("plus", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("not", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("float", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("integer", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});

        rightRecArithExpr.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        rightRecArithExpr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(addOp); push(term); push(rightRecArithExpr);}});
        rightRecArithExpr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(addOp); push(term); push(rightRecArithExpr);}});
        rightRecArithExpr.tableEntry.put("or", new Stack<GrammarToken>(){{push(addOp); push(term); push(rightRecArithExpr);}});

        reptAParams1.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptAParams1.tableEntry.put("comma", new Stack<GrammarToken>(){{push(aParamsTail); push(reptAParams1);}});

    }
}
