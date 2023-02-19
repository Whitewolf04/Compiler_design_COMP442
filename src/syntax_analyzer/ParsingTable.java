package syntax_analyzer;

import java.util.HashMap;
import java.util.Stack;

public final class ParsingTable {
    private static HashMap<String, NonTerminal> table;

    public static void loadTable(){
        NonTerminal START = new NonTerminal("START", new Terminal[]{Terminal.functionW, Terminal.EPSILON, Terminal.classW}, new Terminal[]{Terminal.START});
        NonTerminal aParams = new NonTerminal("aParams", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.rpar});
        NonTerminal aParamsTail = new NonTerminal("aParamsTail", new Terminal[]{Terminal.comma}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal addOp = new NonTerminal("addOp", new Terminal[]{Terminal.orW, Terminal.plus, Terminal.minus}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal arithExpr = new NonTerminal("arithExpr", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.rpar, Terminal.semi, Terminal.comma, Terminal.rsqbr, Terminal.eq, Terminal.geq});
        NonTerminal arraySize = new NonTerminal("arraySize", new Terminal[]{Terminal.lsqbr}, new Terminal[]{Terminal.rpar, Terminal.lsqbr, Terminal.semi, Terminal.comma});
        NonTerminal arraySize1 = new NonTerminal("arraySize1", new Terminal[]{Terminal.intLit, Terminal.rsqbr}, new Terminal[]{Terminal.rpar, Terminal.lsqbr, Terminal.semi, Terminal.comma});
        NonTerminal assignOp = new NonTerminal("assignOp", new Terminal[]{Terminal.assign}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal assignStat = new NonTerminal("assignStat", new Terminal[]{Terminal.lpar, Terminal.lsqbr}, new Terminal[]{Terminal.semi});
        NonTerminal classDecl = new NonTerminal("classDecl", new Terminal[]{Terminal.classW}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal classDeclOrFuncDef = new NonTerminal("classDeclOrFuncDef", new Terminal[]{Terminal.functionW, Terminal.classW}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal expr = new NonTerminal("expr", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.rpar, Terminal.semi, Terminal.comma});
        NonTerminal expr1 = new NonTerminal("expr1", new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.EPSILON, Terminal.eq, Terminal.geq}, new Terminal[]{Terminal.rpar, Terminal.semi, Terminal.comma});
        NonTerminal fParams = new NonTerminal("fParams", new Terminal[]{Terminal.id, Terminal.EPSILON}, new Terminal[]{Terminal.rpar});
        NonTerminal fParamsTail = new NonTerminal("fParamsTail", new Terminal[]{Terminal.comma}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal factor = new NonTerminal("factor", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal factor1 = new NonTerminal("factor1", new Terminal[]{Terminal.lpar, Terminal.lsqbr}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal factor2 = new NonTerminal("factor2", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.EPSILON, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{});
        NonTerminal factor3 = new NonTerminal("factor3", new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.EPSILON, Terminal.eq, Terminal.geq}, new Terminal[]{});
        NonTerminal funcBody = new NonTerminal("funcBody", new Terminal[]{Terminal.lcurbr}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal funcDef = new NonTerminal("funcDef", new Terminal[]{Terminal.functionW}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal funcHead = new NonTerminal("funcHead", new Terminal[]{Terminal.functionW}, new Terminal[]{Terminal.lcurbr});
        NonTerminal funcHead1 = new NonTerminal("funcHead1", new Terminal[]{Terminal.lpar, Terminal.sr}, new Terminal[]{Terminal.lcurbr});
        NonTerminal funcHead2 = new NonTerminal("funcHead2", new Terminal[]{Terminal.constructorW, Terminal.id}, new Terminal[]{Terminal.lcurbr});
        NonTerminal functionCall = new NonTerminal("functionCall", new Terminal[]{Terminal.lpar}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal functionCall1 = new NonTerminal("functionCall1", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.EPSILON, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.rpar});
        NonTerminal idnest = new NonTerminal("idnest", new Terminal[]{Terminal.id}, new Terminal[]{Terminal.id});
        NonTerminal idnest1 = new NonTerminal("idnest1", new Terminal[]{Terminal.lpar, Terminal.lsqbr, Terminal.dot}, new Terminal[]{Terminal.id});
        NonTerminal idnest2 = new NonTerminal("idnest2", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.rpar, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{});
        NonTerminal indice = new NonTerminal("indice", new Terminal[]{Terminal.lsqbr}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.lsqbr, Terminal.semi, Terminal.rsqbr, Terminal.assign, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.dot, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal localVarDecl = new NonTerminal("localVarDecl", new Terminal[]{Terminal.localvarW}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.lsqbr, Terminal.ifW, Terminal.readW, Terminal.whileW});
        NonTerminal localVarDecl1 = new NonTerminal("localVarDecl1", new Terminal[]{Terminal.lpar, Terminal.lsqbr, Terminal.semi}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.lsqbr, Terminal.ifW, Terminal.readW, Terminal.whileW});
        NonTerminal localVarDecl2 = new NonTerminal("localVarDecl2", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.rpar, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.lsqbr, Terminal.ifW, Terminal.readW, Terminal.whileW});
        NonTerminal localVarDeclOrStmt = new NonTerminal("localVarDeclOrStmt", new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.lsqbr, Terminal.ifW, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.lsqbr, Terminal.ifW, Terminal.readW, Terminal.whileW});
        NonTerminal memberDecl = new NonTerminal("memberDecl", new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.attributeW}, new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.publicW, Terminal.rcurbr, Terminal.privateW, Terminal.attributeW});
        NonTerminal memberFuncDecl = new NonTerminal("memberFuncDecl", new Terminal[]{Terminal.functionW, Terminal.constructorW}, new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.publicW, Terminal.rcurbr, Terminal.privateW, Terminal.attributeW});
        NonTerminal memberVarDecl = new NonTerminal("memberVarDecl", new Terminal[]{Terminal.attributeW}, new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.publicW, Terminal.rcurbr, Terminal.privateW, Terminal.attributeW});
        NonTerminal multOp = new NonTerminal("multOp", new Terminal[]{Terminal.mult, Terminal.andW, Terminal.div}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal optClassDecl2 = new NonTerminal("optClassDecl2", new Terminal[]{Terminal.EPSILON, Terminal.isaW}, new Terminal[]{Terminal.lcurbr});
        NonTerminal relExpr = new NonTerminal("relExpr", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.rpar});
        NonTerminal relOp = new NonTerminal("relOp", new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.eq, Terminal.geq}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal reptSTART0 = new NonTerminal("reptSTART0", new Terminal[]{Terminal.functionW, Terminal.EPSILON, Terminal.classW}, new Terminal[]{Terminal.START});
        NonTerminal reptAParams1 = new NonTerminal("reptAParams1", new Terminal[]{Terminal.comma, Terminal.EPSILON}, new Terminal[]{Terminal.rpar});
        NonTerminal reptClassDecl5 = new NonTerminal("reptClassDecl5", new Terminal[]{Terminal.comma, Terminal.EPSILON}, new Terminal[]{Terminal.lcurbr});
        NonTerminal reptClassDecl8 = new NonTerminal("reptClassDecl8", new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.publicW, Terminal.EPSILON, Terminal.privateW, Terminal.attributeW}, new Terminal[]{Terminal.rcurbr});
        NonTerminal reptFParams3 = new NonTerminal("reptFParams3", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal reptFParams4 = new NonTerminal("reptFParams4", new Terminal[]{Terminal.comma, Terminal.EPSILON}, new Terminal[]{Terminal.rpar});
        NonTerminal reptFParamsTail4 = new NonTerminal("reptFParamsTail4", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal reptFuncBody1 = new NonTerminal("reptFuncBody1", new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.lsqbr, Terminal.ifW, Terminal.EPSILON, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.rcurbr});
        NonTerminal reptIdnest = new NonTerminal("reptIdnest", new Terminal[]{Terminal.id}, new Terminal[]{Terminal.id});
        NonTerminal reptIdnest0 = new NonTerminal("reptIdnest0", new Terminal[]{Terminal.lpar, Terminal.lsqbr, Terminal.EPSILON, Terminal.dot}, new Terminal[]{Terminal.id});
        NonTerminal reptIdnest1 = new NonTerminal("reptIdnest1", new Terminal[]{Terminal.lpar, Terminal.EPSILON}, new Terminal[]{Terminal.dot});
        NonTerminal reptLocalVarDecl4 = new NonTerminal("reptLocalVarDecl4", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.semi});
        NonTerminal reptMemberVarDecl4 = new NonTerminal("reptMemberVarDecl4", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.semi});
        NonTerminal reptStatBlock1 = new NonTerminal("reptStatBlock1", new Terminal[]{Terminal.writeW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.lsqbr, Terminal.ifW, Terminal.EPSILON, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.rcurbr});
        NonTerminal reptVariable2 = new NonTerminal("reptVariable2", new Terminal[]{Terminal.lsqbr}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.assign, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal reptVariable3 = new NonTerminal("reptVariable3", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.assign, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal returnType = new NonTerminal("returnType", new Terminal[]{Terminal.id, Terminal.integerW, Terminal.floatW, Terminal.voidW}, new Terminal[]{Terminal.lcurbr, Terminal.semi});
        NonTerminal rightRecArithExpr = new NonTerminal("rightRecArithExpr", new Terminal[]{Terminal.orW, Terminal.plus, Terminal.EPSILON, Terminal.minus}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.rpar, Terminal.semi, Terminal.comma, Terminal.rsqbr, Terminal.eq, Terminal.geq});
        NonTerminal rightRecTerm = new NonTerminal("rightRecTerm", new Terminal[]{Terminal.mult, Terminal.andW, Terminal.EPSILON, Terminal.div}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.eq, Terminal.geq});
        NonTerminal sign = new NonTerminal("sign", new Terminal[]{Terminal.plus, Terminal.minus}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal statBlock = new NonTerminal("statBlock", new Terminal[]{Terminal.lcurbr, Terminal.writeW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.lsqbr, Terminal.ifW, Terminal.EPSILON, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.elseW, Terminal.semi});
        NonTerminal statement = new NonTerminal("statement", new Terminal[]{Terminal.writeW, Terminal.lpar, Terminal.returnW, Terminal.lsqbr, Terminal.ifW, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.elseW, Terminal.lsqbr, Terminal.ifW, Terminal.semi, Terminal.readW, Terminal.whileW});
        NonTerminal statement0 = new NonTerminal("statement0", new Terminal[]{Terminal.id, Terminal.lsqbr}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.lpar, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.elseW, Terminal.lsqbr, Terminal.ifW, Terminal.semi, Terminal.readW, Terminal.whileW});
        NonTerminal term = new NonTerminal("term", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.eq, Terminal.geq});
        NonTerminal type = new NonTerminal("type", new Terminal[]{Terminal.id, Terminal.integerW, Terminal.floatW}, new Terminal[]{Terminal.lcurbr, Terminal.lpar, Terminal.rpar, Terminal.semi, Terminal.lsqbr, Terminal.rsqbr});
        NonTerminal variable = new NonTerminal("variable", new Terminal[]{Terminal.lsqbr}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.assign, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal visibility = new NonTerminal("visibility", new Terminal[]{Terminal.publicW, Terminal.EPSILON, Terminal.privateW}, new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.attributeW});

        START.tableEntry.put("function", new Stack<GrammarToken>(){{push(reptSTART0);}});
        START.tableEntry.put("class", new Stack<GrammarToken>(){{push(reptSTART0);}});

        aParams.tableEntry.put("id", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("minus", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("plus", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("not", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});
        aParams.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(expr); push(reptAParams1);}});

        aParamsTail.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(expr);}});

        addOp.tableEntry.put("-", new Stack<GrammarToken>(){{push(Terminal.minus);}});
        addOp.tableEntry.put("+", new Stack<GrammarToken>(){{push(Terminal.plus);}});
        addOp.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.orW);}});

        arithExpr.tableEntry.put("id", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("not", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});
        arithExpr.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr);}});

        arraySize.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(Terminal.lsqbr); push(arraySize1);}});

        arraySize1.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(Terminal.intLit); push(Terminal.rsqbr);}});
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
        expr.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});
        expr.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(arithExpr); push(expr1);}});

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
        factor.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(Terminal.floatLit);}});
        factor.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(Terminal.intLit);}});

        factor1.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(functionCall);}});
        factor1.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(variable);}});

        factor2.tableEntry.put("id", new Stack<GrammarToken>(){{push(arithExpr); push(factor3);}});
        factor2.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(arithExpr); push(factor3);}});
        factor2.tableEntry.put("minus", new Stack<GrammarToken>(){{push(arithExpr); push(factor3);}});
        factor2.tableEntry.put("plus", new Stack<GrammarToken>(){{push(arithExpr); push(factor3);}});
        factor2.tableEntry.put("not", new Stack<GrammarToken>(){{push(arithExpr); push(factor3);}});
        factor2.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(arithExpr); push(factor3);}});
        factor2.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(arithExpr); push(factor3);}});

        factor3.tableEntry.put("comma", new Stack<GrammarToken>(){{push(aParamsTail); push(reptAParams1);}});
        factor3.tableEntry.put("geq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(reptAParams1);}});
        factor3.tableEntry.put("leq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(reptAParams1);}});
        factor3.tableEntry.put("gt", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(reptAParams1);}});
        factor3.tableEntry.put("lt", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(reptAParams1);}});
        factor3.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(reptAParams1);}});
        factor3.tableEntry.put("eq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(reptAParams1);}});

        fParams.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(Terminal.col); push(type); push(reptFParams3); push(reptFParams4);}});
        fParams.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});

        fParamsTail.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(Terminal.id); push(Terminal.col); push(type); push(reptFParamsTail4);}});
        
        funcBody.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.lcurbr); push(reptFuncBody1); push(Terminal.rcurbr);}});

        funcDef.tableEntry.put("function", new Stack<GrammarToken>(){{push(funcHead); push(funcBody);}});

        funcHead.tableEntry.put("function", new Stack<GrammarToken>(){{push(Terminal.functionW); push(Terminal.id); push(funcHead1);}});

        funcHead1.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(fParams); push(Terminal.rpar); push(Terminal.arrow); push(returnType);}});
        funcHead1.tableEntry.put("sr", new Stack<GrammarToken>(){{push(Terminal.sr); push(funcHead2);}});

        funcHead2.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(Terminal.lpar); push(fParams); push(Terminal.rpar); push(Terminal.arrow); push(returnType);}});
        funcHead2.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(Terminal.constructorW); push(Terminal.lpar); push(fParams); push(Terminal.rpar);}});

        functionCall.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(functionCall1); push(Terminal.rpar);}});

        functionCall1.tableEntry.put("id", new Stack<GrammarToken>(){{push(aParams);}});
        functionCall1.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        functionCall1.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(aParams);}});
        functionCall1.tableEntry.put("minus", new Stack<GrammarToken>(){{push(aParams);}});
        functionCall1.tableEntry.put("plus", new Stack<GrammarToken>(){{push(aParams);}});
        functionCall1.tableEntry.put("not", new Stack<GrammarToken>(){{push(aParams);}});
        functionCall1.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(aParams);}});
        functionCall1.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(aParams);}});

        idnest.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(idnest1);}});

        idnest1.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar);}});
        idnest1.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptIdnest1); push(Terminal.dot);}});
        idnest1.tableEntry.put("dot", new Stack<GrammarToken>(){{push(reptIdnest1); push(Terminal.dot);}});

        idnest2.tableEntry.put("id", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.dot);}});
        idnest2.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.rpar); push(Terminal.dot);}});
        idnest2.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.dot);}});
        idnest2.tableEntry.put("minus", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.dot);}});
        idnest2.tableEntry.put("plus", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.dot);}});
        idnest2.tableEntry.put("not", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.dot);}});
        idnest2.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.dot);}});
        idnest2.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.dot);}});

        indice.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(Terminal.lsqbr); push(arithExpr); push(Terminal.rsqbr);}});

        localVarDecl.tableEntry.put("localvar", new Stack<GrammarToken>(){{push(Terminal.localvarW); push(Terminal.id); push(Terminal.col); push(type); push(localVarDecl1);}});

        localVarDecl1.tableEntry.put("semi", new Stack<GrammarToken>(){{push(reptLocalVarDecl4); push(Terminal.semi);}});
        localVarDecl1.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(localVarDecl2);}});
        localVarDecl1.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptLocalVarDecl4); push(Terminal.semi);}});

        localVarDecl2.tableEntry.put("id", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.semi);}});
        localVarDecl2.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.rpar); push(Terminal.semi);}});
        localVarDecl2.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.semi);}});
        localVarDecl2.tableEntry.put("minus", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.semi);}});
        localVarDecl2.tableEntry.put("plus", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.semi);}});
        localVarDecl2.tableEntry.put("not", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.semi);}});
        localVarDecl2.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.semi);}});
        localVarDecl2.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(aParams); push(Terminal.rpar); push(Terminal.semi);}});

        localVarDeclOrStmt.tableEntry.put("id", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("return", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("write", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("while", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("read", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("if", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("localvar", new Stack<GrammarToken>(){{push(statement);}});
        localVarDeclOrStmt.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(statement);}});

        memberDecl.tableEntry.put("attribute", new Stack<GrammarToken>(){{push(memberVarDecl);}});
        memberDecl.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(memberFuncDecl);}});
        memberDecl.tableEntry.put("function", new Stack<GrammarToken>(){{push(memberFuncDecl);}});

        memberFuncDecl.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(Terminal.constructorW); push(Terminal.col); push(Terminal.lpar); push(fParams); push(Terminal.rpar); push(Terminal.semi);}});
        memberFuncDecl.tableEntry.put("function", new Stack<GrammarToken>(){{push(Terminal.functionW); push(Terminal.id); push(Terminal.col); push(Terminal.lpar); push(fParams); push(Terminal.rpar); push(Terminal.arrow); push(returnType); push(Terminal.semi);}});

        memberVarDecl.tableEntry.put("attribute", new Stack<GrammarToken>(){{push(Terminal.attributeW); push(Terminal.id); push(Terminal.col); push(type); push(reptMemberVarDecl4); push(Terminal.semi);}});

        multOp.tableEntry.put("and", new Stack<GrammarToken>(){{push(Terminal.andW);}});
        multOp.tableEntry.put("div", new Stack<GrammarToken>(){{push(Terminal.div);}});
        multOp.tableEntry.put("mult", new Stack<GrammarToken>(){{push(Terminal.mult);}});

        optClassDecl2.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        optClassDecl2.tableEntry.put("isa", new Stack<GrammarToken>(){{push(Terminal.isaW); push(Terminal.id); push(reptClassDecl5);}});

        relExpr.tableEntry.put("id", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr);}});
        relExpr.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr);}});
        relExpr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr);}});
        relExpr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr);}});
        relExpr.tableEntry.put("not", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr);}});
        relExpr.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr);}});
        relExpr.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr);}});

        relOp.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.eq);}});
        relOp.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.noteq);}});
        relOp.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.lt);}});
        relOp.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.gt);}});
        relOp.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.leq);}});
        relOp.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.geq);}});

        reptAParams1.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptAParams1.tableEntry.put("comma", new Stack<GrammarToken>(){{push(aParamsTail); push(reptAParams1);}});

        reptClassDecl5.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptClassDecl5.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(Terminal.id); push(reptClassDecl5);}});

        reptClassDecl8.tableEntry.put("private", new Stack<GrammarToken>(){{push(visibility); push(memberDecl); push(reptClassDecl8);}});
        reptClassDecl8.tableEntry.put("public", new Stack<GrammarToken>(){{push(visibility); push(memberDecl); push(reptClassDecl8);}});
        reptClassDecl8.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptClassDecl8.tableEntry.put("attribute", new Stack<GrammarToken>(){{push(visibility); push(memberDecl); push(reptClassDecl8);}});
        reptClassDecl8.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(visibility); push(memberDecl); push(reptClassDecl8);}});
        reptClassDecl8.tableEntry.put("function", new Stack<GrammarToken>(){{push(visibility); push(memberDecl); push(reptClassDecl8);}});

        reptFParams3.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParams3.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParams3.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptFParams3);}});
        
        reptFParams4.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParams4.tableEntry.put("comma", new Stack<GrammarToken>(){{push(fParamsTail); push(reptFParams4);}});
        
        reptFParamsTail4.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParamsTail4.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFParamsTail4.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptFParamsTail4);}});

        reptFuncBody1.tableEntry.put("id", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("return", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("read", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("write", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("while", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("if", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("rcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptFuncBody1.tableEntry.put("localvar", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});
        reptFuncBody1.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(localVarDeclOrStmt); push(reptFuncBody1);}});

        reptIdnest.tableEntry.put("id", new Stack<GrammarToken>(){{push(idnest); push(Terminal.id); push(reptIdnest0);}});

        reptIdnest0.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptIdnest0.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(idnest1); push(Terminal.id); push(reptIdnest0);}});
        reptIdnest0.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(idnest1); push(Terminal.id); push(reptIdnest0);}});
        reptIdnest0.tableEntry.put("dot", new Stack<GrammarToken>(){{push(idnest1); push(Terminal.id); push(reptIdnest0);}});

        reptIdnest1.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(indice); push(reptIdnest1);}});
        reptIdnest1.tableEntry.put("dot", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});

        reptLocalVarDecl4.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptLocalVarDecl4.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptLocalVarDecl4);}});

        reptMemberVarDecl4.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptMemberVarDecl4.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptMemberVarDecl4);}});

        reptSTART0.tableEntry.put("function", new Stack<GrammarToken>(){{push(classDeclOrFuncDef); push(reptSTART0);}});
        reptSTART0.tableEntry.put("class", new Stack<GrammarToken>(){{push(classDeclOrFuncDef); push(reptSTART0);}});

        reptStatBlock1.tableEntry.put("id", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("return", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("write", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("read", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("while", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("if", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("rcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptStatBlock1.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});

        reptVariable2.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(indice); push(reptVariable3);}});

        reptVariable3.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("and", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("div", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("mult", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(indice); push(reptVariable3);}});
        reptVariable3.tableEntry.put("assign", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        reptVariable3.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});

        returnType.tableEntry.put("id", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(type);}});
        returnType.tableEntry.put("void", new Stack<GrammarToken>(){{push(Terminal.voidW);}});

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

        sign.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.plus);}});
        sign.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.minus);}});

        statBlock.tableEntry.put("id", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});
        statBlock.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("return", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("write", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("read", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("while", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("else", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("if", new Stack<GrammarToken>(){{push(statement);}});
        statBlock.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.lcurbr); push(reptStatBlock1); push(Terminal.rcurbr);}});
        statBlock.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(statement);}});

        statement.tableEntry.put("id", new Stack<GrammarToken>(){{push(reptIdnest);  push(Terminal.id); push(assignStat); push(Terminal.semi);}});
        statement.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(assignStat); push(Terminal.semi);}});
        statement.tableEntry.put("return", new Stack<GrammarToken>(){{push(Terminal.returnW);  push(Terminal.lpar); push(expr); push(Terminal.rpar); push(Terminal.semi);}});
        statement.tableEntry.put("write", new Stack<GrammarToken>(){{push(Terminal.writeW);  push(Terminal.lpar); push(expr); push(Terminal.rpar); push(Terminal.semi);}});
        statement.tableEntry.put("read", new Stack<GrammarToken>(){{push(Terminal.readW);  push(Terminal.lpar); push(Terminal.id); push(statement0);}});
        statement.tableEntry.put("return", new Stack<GrammarToken>(){{push(Terminal.whileW); push(Terminal.lpar);  push(relExpr); push(Terminal.rpar); push(statBlock); push(Terminal.semi);}});
        statement.tableEntry.put("if", new Stack<GrammarToken>(){{push(Terminal.ifW);  push(Terminal.lpar); push(relExpr); push(Terminal.rpar); push(Terminal.thenW); push(statBlock); push(Terminal.elseW); push(statBlock); push(Terminal.semi);}});
        statement.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(assignStat); push(Terminal.semi);}});

        statement0.tableEntry.put("id", new Stack<GrammarToken>(){{push(reptIdnest); push(Terminal.id); push(variable); push(Terminal.rpar); push(Terminal.semi);}});
        statement0.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(variable); push(Terminal.rpar); push(Terminal.semi);}});
        
        term.tableEntry.put("id", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("minus", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("plus", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("not", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});
        term.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm);}});

        type.tableEntry.put("integer", new Stack<GrammarToken>(){{push(Terminal.integerW);}});
        type.tableEntry.put("float", new Stack<GrammarToken>(){{push(Terminal.floatW);}});
        type.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id);}});

        variable.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptVariable2);}});

        visibility.tableEntry.put("public", new Stack<GrammarToken>(){{push(Terminal.publicW);}});
        visibility.tableEntry.put("private", new Stack<GrammarToken>(){{push(Terminal.privateW);}});
        visibility.tableEntry.put("epsilon", new Stack<GrammarToken>(){{push(Terminal.EPSILON);}});

        
        // Load the table
        table = new HashMap<String, NonTerminal>();
        table.put(START.name, START);
        table.put(aParams.name, aParams);
        table.put(aParamsTail.name, aParamsTail);
        table.put(addOp.name, addOp);
        table.put(arithExpr.name, arithExpr);
        table.put(arraySize.name, arraySize);
        table.put(arraySize1.name, arraySize1);
        table.put(assignOp.name, assignOp);
        table.put(assignStat.name, assignStat);
        table.put(classDecl.name, classDecl);
        table.put(classDeclOrFuncDef.name, classDeclOrFuncDef);
        table.put(expr1.name, expr1);
        table.put(expr.name, expr);
        table.put(fParams.name, fParams);
        table.put(fParamsTail.name, fParamsTail);
        table.put(factor1.name, factor1);
        table.put(factor2.name, factor2);
        table.put(factor3.name, factor3);
        table.put(factor.name, factor);
        table.put(funcBody.name, funcBody);
        table.put(funcDef.name, funcDef);
        table.put(funcHead1.name, funcHead1);
        table.put(funcHead2.name, funcHead2);
        table.put(funcHead.name, funcHead);
        table.put(functionCall.name, functionCall);
        table.put(functionCall1.name, functionCall1);
        table.put(idnest1.name, idnest1);
        table.put(idnest2.name, idnest2);
        table.put(idnest.name, idnest);
        table.put(indice.name, indice);
        table.put(localVarDecl1.name, localVarDecl1);
        table.put(localVarDecl2.name, localVarDecl2);
        table.put(localVarDecl.name, localVarDecl);
        table.put(localVarDeclOrStmt.name, localVarDeclOrStmt);
        table.put(memberDecl.name, memberDecl);
        table.put(memberFuncDecl.name, memberFuncDecl);
        table.put(memberVarDecl.name, memberVarDecl);
        table.put(multOp.name, multOp);
        table.put(optClassDecl2.name, optClassDecl2);
        table.put(relExpr.name, relExpr);
        table.put(relOp.name, relOp);
        table.put(reptSTART0.name, reptSTART0);
        table.put(reptAParams1.name, reptAParams1);
        table.put(reptClassDecl5.name, reptClassDecl5);
        table.put(reptClassDecl8.name, reptClassDecl8);
        table.put(reptFParams3.name, reptFParams3);
        table.put(reptFParams4.name, reptFParams4);
        table.put(reptFParamsTail4.name, reptFParamsTail4);
        table.put(reptFuncBody1.name, reptFuncBody1);
        table.put(reptIdnest0.name, reptIdnest0);
        table.put(reptIdnest1.name, reptIdnest1);
        table.put(reptIdnest.name, reptIdnest);
        table.put(reptLocalVarDecl4.name, reptLocalVarDecl4);
        table.put(reptMemberVarDecl4.name, reptMemberVarDecl4);
        table.put(reptStatBlock1.name, reptStatBlock1);
        table.put(reptVariable2.name, reptVariable2);
        table.put(reptVariable3.name, reptVariable3);
        table.put(returnType.name, returnType);
        table.put(rightRecArithExpr.name, rightRecArithExpr);
        table.put(rightRecTerm.name, rightRecTerm);
        table.put(sign.name, sign);
        table.put(statBlock.name, statBlock);
        table.put(statement0.name, statement0);
        table.put(statement.name, statement);
        table.put(term.name, term);
        table.put(type.name, type);
        table.put(variable.name, variable);
        table.put(visibility.name, visibility);

    }

    public static NonTerminal get(String key){
        return table.get(key);
    }
}
