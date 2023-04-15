package syntax_analyzer;

import java.util.HashMap;
import java.util.Stack;

import AST_generator.NodeFactory;
import AST_generator.SubTreeFactory;

public final class ParsingTable {
    private static HashMap<String, NonTerminal> table;

    public static void loadTable(){
        NonTerminal START = new NonTerminal("START", new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW}, new Terminal[]{});
        NonTerminal aParams = new NonTerminal("aParams", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.EPSILON, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.rpar});
        NonTerminal aParamsTail = new NonTerminal("aParamsTail", new Terminal[]{Terminal.comma}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal addOp = new NonTerminal("addOp", new Terminal[]{Terminal.orW, Terminal.plus, Terminal.minus}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal arithExpr = new NonTerminal("arithExpr", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.rpar, Terminal.semi, Terminal.comma, Terminal.rsqbr, Terminal.eq, Terminal.geq});
        NonTerminal arrayOrObject = new NonTerminal("arrayOrObject", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON, Terminal.lpar}, new Terminal[]{Terminal.semi});
        NonTerminal arraySize = new NonTerminal("arraySize", new Terminal[]{Terminal.lsqbr}, new Terminal[]{Terminal.rpar, Terminal.lsqbr, Terminal.semi, Terminal.comma});
        NonTerminal arraySize2 = new NonTerminal("arraySize2", new Terminal[]{Terminal.intLit, Terminal.rsqbr}, new Terminal[]{Terminal.rpar, Terminal.lsqbr, Terminal.semi, Terminal.comma});
        NonTerminal assignOp = new NonTerminal("assignOp", new Terminal[]{Terminal.assign}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal classDecl = new NonTerminal("classDecl", new Terminal[]{Terminal.classW}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal classDeclOrFuncDef = new NonTerminal("classDeclOrFuncDef", new Terminal[]{Terminal.functionW, Terminal.classW}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal expr = new NonTerminal("expr", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.rpar, Terminal.semi, Terminal.comma});
        NonTerminal expr2 = new NonTerminal("expr2", new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.EPSILON, Terminal.eq, Terminal.geq}, new Terminal[]{Terminal.rpar, Terminal.semi, Terminal.comma});
        NonTerminal fParams = new NonTerminal("fParams", new Terminal[]{Terminal.id, Terminal.EPSILON}, new Terminal[]{Terminal.rpar});
        NonTerminal fParamsTail = new NonTerminal("fParamsTail", new Terminal[]{Terminal.comma}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal factor = new NonTerminal("factor", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal factor2 = new NonTerminal("factor2", new Terminal[]{Terminal.EPSILON, Terminal.lpar, Terminal.lsqbr}, new Terminal[]{Terminal.semi, Terminal.mult, Terminal.div, Terminal.andW, Terminal.dot, Terminal.rsqbr, Terminal.eq, Terminal.noteq, Terminal.lt, Terminal.gt, Terminal.leq, Terminal.geq, Terminal.plus, Terminal.minus, Terminal.orW, Terminal.comma, Terminal.rpar});
        NonTerminal funcBody = new NonTerminal("funcBody", new Terminal[]{Terminal.lcurbr}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal funcDef = new NonTerminal("funcDef", new Terminal[]{Terminal.functionW}, new Terminal[]{Terminal.functionW, Terminal.START, Terminal.classW});
        NonTerminal funcHead = new NonTerminal("funcHead", new Terminal[]{Terminal.functionW}, new Terminal[]{Terminal.lcurbr});
        NonTerminal funcHeadMemberTail = new NonTerminal("funcHeadMemberTail", new Terminal[]{Terminal.constructorW, Terminal.id}, new Terminal[]{Terminal.lcurbr});
        NonTerminal funcHeadTail = new NonTerminal("funcHeadTail", new Terminal[]{Terminal.sr, Terminal.lpar}, new Terminal[]{Terminal.lcurbr});
        NonTerminal idnest = new NonTerminal("idnest", new Terminal[]{Terminal.dot}, new Terminal[]{Terminal.semi, Terminal.mult, Terminal.div, Terminal.andW, Terminal.dot, Terminal.rsqbr, Terminal.eq, Terminal.noteq, Terminal.lt, Terminal.gt, Terminal.leq, Terminal.geq, Terminal.plus, Terminal.minus, Terminal.orW, Terminal.comma, Terminal.rpar});
        NonTerminal idnest2 = new NonTerminal("idnest2", new Terminal[]{Terminal.lpar, Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.semi, Terminal.mult, Terminal.div, Terminal.andW, Terminal.dot, Terminal.rsqbr, Terminal.eq, Terminal.noteq, Terminal.lt, Terminal.gt, Terminal.leq, Terminal.geq, Terminal.plus, Terminal.minus, Terminal.orW, Terminal.comma, Terminal.rpar});
        NonTerminal indice = new NonTerminal("indice", new Terminal[]{Terminal.lsqbr}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.lsqbr, Terminal.semi, Terminal.rsqbr, Terminal.assign, Terminal.rpar, Terminal.mult, Terminal.andW, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.dot, Terminal.div, Terminal.eq, Terminal.geq});
        NonTerminal localVarDecl = new NonTerminal("localVarDecl", new Terminal[]{Terminal.localvarW}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.ifW, Terminal.readW, Terminal.whileW});
        NonTerminal localVarOrStat = new NonTerminal("localVarOrStat", new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.returnW, Terminal.id, Terminal.ifW, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.writeW, Terminal.localvarW, Terminal.returnW, Terminal.id, Terminal.rcurbr, Terminal.ifW, Terminal.readW, Terminal.whileW});
        NonTerminal memberDecl = new NonTerminal("memberDecl", new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.attributeW}, new Terminal[]{Terminal.publicW, Terminal.rcurbr, Terminal.privateW});
        NonTerminal memberFuncDecl = new NonTerminal("memberFuncDecl", new Terminal[]{Terminal.functionW, Terminal.constructorW}, new Terminal[]{Terminal.publicW, Terminal.rcurbr, Terminal.privateW});
        NonTerminal memberFuncHead = new NonTerminal("memberFuncHead", new Terminal[]{Terminal.functionW, Terminal.constructorW}, new Terminal[]{Terminal.semi});
        NonTerminal memberVarDecl = new NonTerminal("memberVarDecl", new Terminal[]{Terminal.attributeW}, new Terminal[]{Terminal.publicW, Terminal.rcurbr, Terminal.privateW});
        NonTerminal multOp = new NonTerminal("multOp", new Terminal[]{Terminal.mult, Terminal.andW, Terminal.div}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal optInherits = new NonTerminal("optInherits", new Terminal[]{Terminal.EPSILON, Terminal.isaW}, new Terminal[]{Terminal.lcurbr});
        NonTerminal prog = new NonTerminal("prog", new Terminal[]{Terminal.EPSILON, Terminal.functionW, Terminal.classW}, new Terminal[]{Terminal.START});
        NonTerminal relExpr = new NonTerminal("relExpr", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.rpar});
        NonTerminal relOp = new NonTerminal("relOp", new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.eq, Terminal.geq}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal reptProg0 = new NonTerminal("reptProg0", new Terminal[]{Terminal.functionW, Terminal.EPSILON, Terminal.classW}, new Terminal[]{Terminal.START});
        NonTerminal reptAParams1 = new NonTerminal("reptAParams1", new Terminal[]{Terminal.comma, Terminal.EPSILON}, new Terminal[]{Terminal.rpar});
        NonTerminal reptArraySize = new NonTerminal("reptArraySize", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.semi});
        NonTerminal reptFParams3 = new NonTerminal("reptFParams3", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal reptFParams4 = new NonTerminal("reptFParams4", new Terminal[]{Terminal.comma, Terminal.EPSILON}, new Terminal[]{Terminal.rpar});
        NonTerminal reptFParamsTail4 = new NonTerminal("reptFParamsTail4", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.rpar, Terminal.comma});
        NonTerminal reptIdnest1 = new NonTerminal("reptIdnest1", new Terminal[]{Terminal.lsqbr, Terminal.EPSILON}, new Terminal[]{Terminal.assign, Terminal.semi, Terminal.mult, Terminal.div, Terminal.andW, Terminal.dot, Terminal.rsqbr, Terminal.eq, Terminal.noteq, Terminal.lt, Terminal.gt, Terminal.leq, Terminal.geq, Terminal.plus, Terminal.minus, Terminal.orW, Terminal.comma, Terminal.rpar});
        NonTerminal reptInheritsList = new NonTerminal("reptInheritsList", new Terminal[]{Terminal.comma, Terminal.EPSILON}, new Terminal[]{Terminal.lcurbr});
        NonTerminal reptLocalVarOrStat = new NonTerminal("reptLocalVarOrStat", new Terminal[]{Terminal.writeW, Terminal.returnW, Terminal.id, Terminal.ifW, Terminal.EPSILON, Terminal.readW, Terminal.localvarW, Terminal.whileW}, new Terminal[]{Terminal.rcurbr});
        NonTerminal reptMemberDecl = new NonTerminal("reptMemberDecl", new Terminal[]{Terminal.publicW, Terminal.EPSILON, Terminal.privateW}, new Terminal[]{Terminal.rcurbr});
        NonTerminal reptStatBlock1 = new NonTerminal("reptStatBlock1", new Terminal[]{Terminal.writeW, Terminal.returnW, Terminal.id, Terminal.ifW, Terminal.EPSILON, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.rcurbr});
        NonTerminal reptVariable = new NonTerminal("reptVariable", new Terminal[]{Terminal.dot, Terminal.EPSILON}, new Terminal[]{Terminal.rpar});
        NonTerminal reptVariableOrFunctionCall = new NonTerminal("reptVariableOrFunctionCall", new Terminal[]{Terminal.dot, Terminal.EPSILON}, new Terminal[]{Terminal.semi, Terminal.mult, Terminal.div, Terminal.andW, Terminal.rsqbr, Terminal.eq, Terminal.noteq, Terminal.lt, Terminal.gt, Terminal.leq, Terminal.geq, Terminal.plus, Terminal.minus, Terminal.orW, Terminal.comma, Terminal.rpar});
        NonTerminal returnType = new NonTerminal("returnType", new Terminal[]{Terminal.id, Terminal.integerW, Terminal.floatW, Terminal.voidW}, new Terminal[]{Terminal.lcurbr, Terminal.semi});
        NonTerminal rightRecArithExpr = new NonTerminal("rightRecArithExpr", new Terminal[]{Terminal.orW, Terminal.plus, Terminal.EPSILON, Terminal.minus}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.rpar, Terminal.semi, Terminal.comma, Terminal.rsqbr, Terminal.eq, Terminal.geq});
        NonTerminal rightRecTerm = new NonTerminal("rightRecTerm", new Terminal[]{Terminal.mult, Terminal.andW, Terminal.EPSILON, Terminal.div}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.eq, Terminal.geq});
        NonTerminal sign = new NonTerminal("sign", new Terminal[]{Terminal.plus, Terminal.minus}, new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW});
        NonTerminal statBlock = new NonTerminal("statBlock", new Terminal[]{Terminal.lcurbr, Terminal.writeW, Terminal.returnW, Terminal.id, Terminal.ifW, Terminal.EPSILON, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.elseW, Terminal.semi});
        NonTerminal statement = new NonTerminal("statement", new Terminal[]{Terminal.writeW, Terminal.returnW, Terminal.id, Terminal.ifW, Terminal.readW, Terminal.whileW}, new Terminal[]{Terminal.elseW, Terminal.semi, Terminal.localvarW, Terminal.id, Terminal.ifW, Terminal.whileW, Terminal.readW, Terminal.writeW, Terminal.returnW, Terminal.rcurbr});
        NonTerminal statementIdnest = new NonTerminal("statementIdnest", new Terminal[]{Terminal.dot, Terminal.lpar, Terminal.lsqbr, Terminal.assign}, new Terminal[]{Terminal.semi});
        NonTerminal statementIdnest2 = new NonTerminal("statementIdnest2", new Terminal[]{Terminal.dot, Terminal.EPSILON}, new Terminal[]{Terminal.semi});
        NonTerminal statementIdnest3 = new NonTerminal("statementIdnest3", new Terminal[]{Terminal.dot, Terminal.assign}, new Terminal[]{Terminal.semi});
        NonTerminal term = new NonTerminal("term", new Terminal[]{Terminal.lpar, Terminal.id, Terminal.plus, Terminal.intLit, Terminal.minus, Terminal.floatLit, Terminal.notW}, new Terminal[]{Terminal.leq, Terminal.gt, Terminal.noteq, Terminal.lt, Terminal.orW, Terminal.semi, Terminal.rsqbr, Terminal.rpar, Terminal.plus, Terminal.comma, Terminal.minus, Terminal.eq, Terminal.geq});
        NonTerminal type = new NonTerminal("type", new Terminal[]{Terminal.id, Terminal.integerW, Terminal.floatW}, new Terminal[]{Terminal.lcurbr, Terminal.lpar, Terminal.rpar, Terminal.semi, Terminal.lsqbr, Terminal.comma});
        NonTerminal variable = new NonTerminal("variable", new Terminal[]{Terminal.id}, new Terminal[]{Terminal.rpar});
        NonTerminal variable2 = new NonTerminal("variable2", new Terminal[]{Terminal.dot, Terminal.EPSILON, Terminal.lpar, Terminal.lsqbr}, new Terminal[]{Terminal.rpar});
        NonTerminal varIdnest = new NonTerminal("varIdnest", new Terminal[]{Terminal.dot}, new Terminal[]{Terminal.rpar, Terminal.dot});
        NonTerminal varIdnest2 = new NonTerminal("varIdnest2", new Terminal[]{Terminal.EPSILON, Terminal.lpar, Terminal.lsqbr}, new Terminal[]{Terminal.rpar, Terminal.dot});
        NonTerminal visibility = new NonTerminal("visibility", new Terminal[]{Terminal.publicW, Terminal.privateW}, new Terminal[]{Terminal.functionW, Terminal.constructorW, Terminal.attributeW});

        START.tableEntry.put("$", new Stack<GrammarToken>(){{push(prog); push(Terminal.START);}});
        START.tableEntry.put("function", new Stack<GrammarToken>(){{push(prog); push(Terminal.START);}});
        START.tableEntry.put("class", new Stack<GrammarToken>(){{push(prog); push(Terminal.START);}});

        aParams.tableEntry.put("id", new Stack<GrammarToken>(){{push(expr); push(reptAParams1); push(new SubTreeFactory("exprList", "expr", "EPSILON"));}});
        aParams.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON")); push(new SubTreeFactory("exprList", 1));}});
        aParams.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(expr); push(reptAParams1); push(new SubTreeFactory("exprList", "expr", "EPSILON"));}});
        aParams.tableEntry.put("minus", new Stack<GrammarToken>(){{push(expr); push(reptAParams1); push(new SubTreeFactory("exprList", "expr", "EPSILON"));}});
        aParams.tableEntry.put("plus", new Stack<GrammarToken>(){{push(expr); push(reptAParams1); push(new SubTreeFactory("exprList", "expr", "EPSILON"));}});
        aParams.tableEntry.put("not", new Stack<GrammarToken>(){{push(expr); push(reptAParams1); push(new SubTreeFactory("exprList", "expr", "EPSILON"));}});
        aParams.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(expr); push(reptAParams1); push(new SubTreeFactory("exprList", "expr", "EPSILON"));}});
        aParams.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(expr); push(reptAParams1); push(new SubTreeFactory("exprList", "expr", "EPSILON"));}});

        aParamsTail.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(expr);}});

        addOp.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.plus); push(new NodeFactory("plus")); push(new SubTreeFactory("addOp", 1));}});
        addOp.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.minus); push(new NodeFactory("minus")); push(new SubTreeFactory("addOp", 1));}});
        addOp.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.orW); push(new NodeFactory("or")); push(new SubTreeFactory("addOp", 1));}});

        arithExpr.tableEntry.put("id", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr); push(new SubTreeFactory("termList", "term", "EPSILON", "addOp", true));}});
        arithExpr.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr); push(new SubTreeFactory("termList", "term", "EPSILON", "addOp", true));}});
        arithExpr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr); push(new SubTreeFactory("termList", "term", "EPSILON", "addOp", true));}});
        arithExpr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr); push(new SubTreeFactory("termList", "term", "EPSILON", "addOp", true));}});
        arithExpr.tableEntry.put("not", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr); push(new SubTreeFactory("termList", "term", "EPSILON", "addOp", true));}});
        arithExpr.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr); push(new SubTreeFactory("termList", "term", "EPSILON", "addOp", true));}});
        arithExpr.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(term); push(rightRecArithExpr); push(new SubTreeFactory("termList", "term", "EPSILON", "addOp", true));}});

        arrayOrObject.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(aParams); push(Terminal.rpar); push(new SubTreeFactory("arrayOrObject", 1));}});
        arrayOrObject.tableEntry.put("semi", new Stack<GrammarToken>(){{push(reptArraySize); push(new SubTreeFactory("arraySizeList", "intLit", "EPSILON")); push(new SubTreeFactory("arrayOrObject", 1));}});
        arrayOrObject.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptArraySize); push(new SubTreeFactory("arraySizeList", "intLit", "EPSILON")); push(new SubTreeFactory("arrayOrObject", 1));}});

        arraySize.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(Terminal.lsqbr); push(arraySize2);}});

        arraySize2.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(new NodeFactory("intLit")); push(Terminal.rsqbr);}});
        arraySize2.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(Terminal.intLit); push(new NodeFactory("intLit", "integer")); push(Terminal.rsqbr);}});

        assignOp.tableEntry.put("assign", new Stack<GrammarToken>(){{push(Terminal.assign); push(new NodeFactory("assign"));}});

        classDecl.tableEntry.put("class", new Stack<GrammarToken>(){{push(Terminal.classW); push(Terminal.id); push(new NodeFactory("id")); push(optInherits); push(new SubTreeFactory("inheritList", "isa", "EPSILON")); push(Terminal.lcurbr); push(reptMemberDecl); push(new SubTreeFactory("memberList", "memberDecl", "EPSILON")); push(Terminal.rcurbr); push(Terminal.semi); push(new SubTreeFactory("classDecl", 3));}});

        classDeclOrFuncDef.tableEntry.put("class", new Stack<GrammarToken>(){{push(classDecl);}});
        classDeclOrFuncDef.tableEntry.put("function", new Stack<GrammarToken>(){{push(funcDef);}});

        expr.tableEntry.put("id", new Stack<GrammarToken>(){{push(arithExpr); push(expr2); push(new SubTreeFactory("expr", 2));}});
        expr.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(arithExpr); push(expr2); push(new SubTreeFactory("expr", 2));}});
        expr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(arithExpr); push(expr2); push(new SubTreeFactory("expr", 2));}});
        expr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(arithExpr); push(expr2); push(new SubTreeFactory("expr", 2));}});
        expr.tableEntry.put("not", new Stack<GrammarToken>(){{push(arithExpr); push(expr2); push(new SubTreeFactory("expr", 2));}});
        expr.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(arithExpr); push(expr2); push(new SubTreeFactory("expr", 2));}});
        expr.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(arithExpr); push(expr2); push(new SubTreeFactory("expr", 2));}});

        expr2.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        expr2.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        expr2.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        expr2.tableEntry.put("geq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(new SubTreeFactory("expr2", 2));}});
        expr2.tableEntry.put("leq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(new SubTreeFactory("expr2", 2));}});
        expr2.tableEntry.put("gt", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(new SubTreeFactory("expr2", 2));}});
        expr2.tableEntry.put("lt", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(new SubTreeFactory("expr2", 2));}});
        expr2.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(new SubTreeFactory("expr2", 2));}});
        expr2.tableEntry.put("eq", new Stack<GrammarToken>(){{push(relOp); push(arithExpr); push(new SubTreeFactory("expr2", 2));}});

        factor.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(new NodeFactory("id")); push(factor2); push(reptVariableOrFunctionCall); push(new SubTreeFactory("idnestList", "idnest", "EPSILON")); push(new SubTreeFactory("factor", 3));}});
        factor.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(arithExpr); push(Terminal.rpar); push(new SubTreeFactory("factor", 1));}});
        factor.tableEntry.put("minus", new Stack<GrammarToken>(){{push(sign); push(factor); push(new SubTreeFactory("factor", 2));}});
        factor.tableEntry.put("plus", new Stack<GrammarToken>(){{push(sign); push(factor); push(new SubTreeFactory("factor", 2));}});
        factor.tableEntry.put("not", new Stack<GrammarToken>(){{push(Terminal.notW); push(new NodeFactory("not")); push(factor); push(new SubTreeFactory("factor", 2));}});
        factor.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(Terminal.floatLit); push(new NodeFactory("floatLit", "float")); push(new SubTreeFactory("factor", 1));}});
        factor.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(Terminal.intLit); push(new NodeFactory("intLit", "integer")); push(new SubTreeFactory("factor", 1));}});

        factor2.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(aParams); push(Terminal.rpar);}});
        factor2.tableEntry.put("dot", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("semi", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("minus", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("plus", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("comma", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("geq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("leq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("gt", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("lt", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("eq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("and", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("div", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("mult", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});
        factor2.tableEntry.put("or", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON", true));}});

        fParams.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(new NodeFactory("id")); push(Terminal.col); push(type); push(reptFParams3); push(new SubTreeFactory("arraySizeList", "intLit", "EPSILON")); push(new SubTreeFactory("fParams", 3)); push(reptFParams4);}});
        fParams.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});

        fParamsTail.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(Terminal.id); push(new NodeFactory("id")); push(Terminal.col); push(type); push(reptFParamsTail4); push(new SubTreeFactory("arraySizeList", "intLit", "EPSILON")); push(new SubTreeFactory("fParams", 3));}});
        
        funcBody.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.lcurbr); push(reptLocalVarOrStat); push(Terminal.rcurbr); push(new SubTreeFactory("funcBody", "localVarOrStat", "EPSILON"));}});

        funcDef.tableEntry.put("function", new Stack<GrammarToken>(){{push(funcHead); push(funcBody); push(new SubTreeFactory("funcDef", 2));}});

        funcHead.tableEntry.put("function", new Stack<GrammarToken>(){{push(Terminal.functionW); push(Terminal.id); push(new NodeFactory("id")); push(funcHeadTail);}});

        funcHeadMemberTail.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(new NodeFactory("id")); push(Terminal.lpar); push(fParams); push(new SubTreeFactory("fParamsList", "fParams", "EPSILON")); push(Terminal.rpar); push(Terminal.arrow); push(returnType); push(new SubTreeFactory("funcHead", 5));}});
        funcHeadMemberTail.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(Terminal.constructorW); push(new NodeFactory("constructor")); push(Terminal.lpar); push(fParams); push(new SubTreeFactory("fParamsList", "fParams", "EPSILON")); push(Terminal.rpar); push(new SubTreeFactory("funcHead", 4));}});

        funcHeadTail.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(fParams); push(new SubTreeFactory("fParamsList", "fParams", "EPSILON")); push(Terminal.rpar); push(Terminal.arrow); push(returnType); push(new SubTreeFactory("funcHead", 3));}});
        funcHeadTail.tableEntry.put("sr", new Stack<GrammarToken>(){{push(Terminal.sr); push(new NodeFactory("sr")); push(funcHeadMemberTail);}});

        idnest.tableEntry.put("dot", new Stack<GrammarToken>(){{push(Terminal.dot); push(new NodeFactory("dot")); push(Terminal.id); push(new NodeFactory("id")); push(idnest2); push(new SubTreeFactory("idnest", 3));}});

        idnest2.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(aParams); push(Terminal.rpar);}});
        idnest2.tableEntry.put("dot", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("semi", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("minus", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("plus", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("comma", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("geq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("leq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("gt", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("lt", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("eq", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("and", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("div", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("mult", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        idnest2.tableEntry.put("or", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});

        indice.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(Terminal.lsqbr); push(arithExpr); push(Terminal.rsqbr); push(new SubTreeFactory("indice", 1));}});

        localVarDecl.tableEntry.put("localvar", new Stack<GrammarToken>(){{push(Terminal.localvarW); push(Terminal.id); push(new NodeFactory("id")); push(Terminal.col); push(type); push(arrayOrObject); push(Terminal.semi); push(new SubTreeFactory("localVarDecl", 3));}});

        localVarOrStat.tableEntry.put("id", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("localVarOrStat", 1));}});
        localVarOrStat.tableEntry.put("return", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("localVarOrStat", 1));}});
        localVarOrStat.tableEntry.put("write", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("localVarOrStat", 1));}});
        localVarOrStat.tableEntry.put("while", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("localVarOrStat", 1));}});
        localVarOrStat.tableEntry.put("read", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("localVarOrStat", 1));}});
        localVarOrStat.tableEntry.put("if", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("localVarOrStat", 1));}});
        localVarOrStat.tableEntry.put("localvar", new Stack<GrammarToken>(){{push(localVarDecl); push(new SubTreeFactory("localVarOrStat", 1));}});

        memberDecl.tableEntry.put("attribute", new Stack<GrammarToken>(){{push(memberVarDecl);}});
        memberDecl.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(memberFuncDecl);}});
        memberDecl.tableEntry.put("function", new Stack<GrammarToken>(){{push(memberFuncDecl);}});

        memberFuncDecl.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(memberFuncHead); push(Terminal.semi);}});
        memberFuncDecl.tableEntry.put("function", new Stack<GrammarToken>(){{push(memberFuncHead); push(Terminal.semi);}});

        memberFuncHead.tableEntry.put("constructor", new Stack<GrammarToken>(){{push(Terminal.constructorW); push(Terminal.col); push(Terminal.lpar); push(fParams); push(new SubTreeFactory("fParamsList", "fParams", "EPSILON")); push(Terminal.rpar); push(new SubTreeFactory("constructorDecl", 1));}});
        memberFuncHead.tableEntry.put("function", new Stack<GrammarToken>(){{push(Terminal.functionW); push(Terminal.id); push(new NodeFactory("id")); push(Terminal.col); push(Terminal.lpar); push(fParams); push(new SubTreeFactory("fParamsList", "fParams", "EPSILON")); push(Terminal.rpar); push(Terminal.arrow); push(returnType); push(new SubTreeFactory("memFuncDecl", 3));}});

        memberVarDecl.tableEntry.put("attribute", new Stack<GrammarToken>(){{push(Terminal.attributeW); push(Terminal.id); push(new NodeFactory("id")); push(Terminal.col); push(type); push(reptArraySize); push(new SubTreeFactory("arraySizeList", "intLit", "EPSILON")); push(Terminal.semi); push(new SubTreeFactory("memberVarDecl", 3));}});

        multOp.tableEntry.put("and", new Stack<GrammarToken>(){{push(Terminal.andW); push(new NodeFactory("and")); push(new SubTreeFactory("multOp", 1));}});
        multOp.tableEntry.put("div", new Stack<GrammarToken>(){{push(Terminal.div); push(new NodeFactory("div")); push(new SubTreeFactory("multOp", 1));}});
        multOp.tableEntry.put("mult", new Stack<GrammarToken>(){{push(Terminal.mult); push(new NodeFactory("mult")); push(new SubTreeFactory("multOp", 1));}});

        optInherits.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        optInherits.tableEntry.put("isa", new Stack<GrammarToken>(){{push(Terminal.isaW); push(new NodeFactory("isa")); push(Terminal.id); push(new NodeFactory("id")); push(reptInheritsList);}});

        prog.tableEntry.put("$", new Stack<GrammarToken>(){{push(reptProg0);}});
        prog.tableEntry.put("function", new Stack<GrammarToken>(){{push(reptProg0);}});
        prog.tableEntry.put("class", new Stack<GrammarToken>(){{push(reptProg0);}});

        relExpr.tableEntry.put("id", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr); push(new SubTreeFactory("relExpr", 3));}});
        relExpr.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr); push(new SubTreeFactory("relExpr", 3));}});
        relExpr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr); push(new SubTreeFactory("relExpr", 3));}});
        relExpr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr); push(new SubTreeFactory("relExpr", 3));}});
        relExpr.tableEntry.put("not", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr); push(new SubTreeFactory("relExpr", 3));}});
        relExpr.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr); push(new SubTreeFactory("relExpr", 3));}});
        relExpr.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(arithExpr); push(relOp); push(arithExpr); push(new SubTreeFactory("relExpr", 3));}});

        relOp.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.eq); push(new NodeFactory("eq"));}});
        relOp.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.noteq); push(new NodeFactory("noteq"));}});
        relOp.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.lt); push(new NodeFactory("lt"));}});
        relOp.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.gt); push(new NodeFactory("gt"));}});
        relOp.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.leq); push(new NodeFactory("leq"));}});
        relOp.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.geq); push(new NodeFactory("geq"));}});

        reptAParams1.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptAParams1.tableEntry.put("comma", new Stack<GrammarToken>(){{push(aParamsTail); push(reptAParams1);}});

        reptArraySize.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptArraySize.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptArraySize);}});

        reptFParams3.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptFParams3.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptFParams3.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptFParams3);}});
        
        reptFParams4.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptFParams4.tableEntry.put("comma", new Stack<GrammarToken>(){{push(fParamsTail); push(reptFParams4);}});
        
        reptFParamsTail4.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptFParamsTail4.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptFParamsTail4.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(arraySize); push(reptFParamsTail4);}});

        reptIdnest1.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("dot", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("and", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("div", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("mult", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(indice); push(reptIdnest1);}});
        reptIdnest1.tableEntry.put("assign", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptIdnest1.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});

        reptInheritsList.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptInheritsList.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.comma); push(Terminal.id); push(new NodeFactory("id")); push(reptInheritsList);}});

        reptLocalVarOrStat.tableEntry.put("id", new Stack<GrammarToken>(){{push(localVarOrStat); push(reptLocalVarOrStat);}});
        reptLocalVarOrStat.tableEntry.put("return", new Stack<GrammarToken>(){{push(localVarOrStat); push(reptLocalVarOrStat);}});
        reptLocalVarOrStat.tableEntry.put("write", new Stack<GrammarToken>(){{push(localVarOrStat); push(reptLocalVarOrStat);}});
        reptLocalVarOrStat.tableEntry.put("while", new Stack<GrammarToken>(){{push(localVarOrStat); push(reptLocalVarOrStat);}});
        reptLocalVarOrStat.tableEntry.put("read", new Stack<GrammarToken>(){{push(localVarOrStat); push(reptLocalVarOrStat);}});
        reptLocalVarOrStat.tableEntry.put("if", new Stack<GrammarToken>(){{push(localVarOrStat); push(reptLocalVarOrStat);}});
        reptLocalVarOrStat.tableEntry.put("rcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptLocalVarOrStat.tableEntry.put("localvar", new Stack<GrammarToken>(){{push(localVarOrStat); push(reptLocalVarOrStat);}});

        reptMemberDecl.tableEntry.put("public", new Stack<GrammarToken>(){{push(visibility); push(memberDecl); push(new SubTreeFactory("memberDecl", 2)); push(reptMemberDecl);}});
        reptMemberDecl.tableEntry.put("private", new Stack<GrammarToken>(){{push(visibility); push(memberDecl); push(new SubTreeFactory("memberDecl", 2)); push(reptMemberDecl);}});
        reptMemberDecl.tableEntry.put("rcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});

        reptProg0.tableEntry.put("$", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptProg0.tableEntry.put("function", new Stack<GrammarToken>(){{push(classDeclOrFuncDef); push(reptProg0);}});
        reptProg0.tableEntry.put("class", new Stack<GrammarToken>(){{push(classDeclOrFuncDef); push(reptProg0);}});

        reptStatBlock1.tableEntry.put("id", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("return", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("write", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("read", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("while", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("if", new Stack<GrammarToken>(){{push(statement); push(reptStatBlock1);}});
        reptStatBlock1.tableEntry.put("rcurbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});

        reptVariable.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariable.tableEntry.put("dot", new Stack<GrammarToken>(){{push(varIdnest); push(reptVariable);}});

        reptVariableOrFunctionCall.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("dot", new Stack<GrammarToken>(){{push(idnest); push(reptVariableOrFunctionCall);}});
        reptVariableOrFunctionCall.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("and", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("div", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("mult", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        reptVariableOrFunctionCall.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});

        returnType.tableEntry.put("id", new Stack<GrammarToken>(){{push(type); push(new SubTreeFactory("returnType", 1));}});
        returnType.tableEntry.put("float", new Stack<GrammarToken>(){{push(type); push(new SubTreeFactory("returnType", 1));}});
        returnType.tableEntry.put("integer", new Stack<GrammarToken>(){{push(type); push(new SubTreeFactory("returnType", 1));}});
        returnType.tableEntry.put("void", new Stack<GrammarToken>(){{push(Terminal.voidW); push(new NodeFactory("void")); push(new SubTreeFactory("returnType", 1));}});

        rightRecArithExpr.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecArithExpr.tableEntry.put("minus", new Stack<GrammarToken>(){{push(addOp); push(term); push(rightRecArithExpr);}});
        rightRecArithExpr.tableEntry.put("plus", new Stack<GrammarToken>(){{push(addOp); push(term); push(rightRecArithExpr);}});
        rightRecArithExpr.tableEntry.put("or", new Stack<GrammarToken>(){{push(addOp); push(term); push(rightRecArithExpr);}});

        rightRecTerm.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("comma", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("geq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("leq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("gt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("lt", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("noteq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("eq", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("rsqbr", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("or", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        rightRecTerm.tableEntry.put("and", new Stack<GrammarToken>(){{push(multOp); push(factor); push(rightRecTerm);}});
        rightRecTerm.tableEntry.put("div", new Stack<GrammarToken>(){{push(multOp); push(factor); push(rightRecTerm);}});
        rightRecTerm.tableEntry.put("mult", new Stack<GrammarToken>(){{push(multOp); push(factor); push(rightRecTerm);}});

        sign.tableEntry.put("plus", new Stack<GrammarToken>(){{push(Terminal.plus); push(new NodeFactory("plus"));}});
        sign.tableEntry.put("minus", new Stack<GrammarToken>(){{push(Terminal.minus); push(new NodeFactory("minus"));}});

        statBlock.tableEntry.put("id", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("statBlock", 1));}});
        statBlock.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        statBlock.tableEntry.put("return", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("statBlock", 1));}});
        statBlock.tableEntry.put("write", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("statBlock", 1));}});
        statBlock.tableEntry.put("read", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("statBlock", 1));}});
        statBlock.tableEntry.put("while", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("statBlock", 1));}});
        statBlock.tableEntry.put("else", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});
        statBlock.tableEntry.put("if", new Stack<GrammarToken>(){{push(statement); push(new SubTreeFactory("statBlock", 1));}});
        statBlock.tableEntry.put("lcurbr", new Stack<GrammarToken>(){{push(Terminal.lcurbr); push(reptStatBlock1); push(new SubTreeFactory("statementList", "statement", "EPSILON")); push(Terminal.rcurbr); push(new SubTreeFactory("statBlock", 1));}});

        statement.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(new NodeFactory("id")); push(statementIdnest); push(new SubTreeFactory("assignOrFuncCall", "id", true, "EPSILON", "expr")); push(Terminal.semi); push(new SubTreeFactory("statement", 2));}});
        statement.tableEntry.put("return", new Stack<GrammarToken>(){{push(Terminal.returnW); push(new NodeFactory("return")); push(Terminal.lpar); push(expr); push(Terminal.rpar); push(Terminal.semi); push(new SubTreeFactory("statement", 2));}});
        statement.tableEntry.put("write", new Stack<GrammarToken>(){{push(Terminal.writeW); push(new NodeFactory("write")); push(Terminal.lpar); push(expr); push(Terminal.rpar); push(Terminal.semi); push(new SubTreeFactory("statement", 2));}});
        statement.tableEntry.put("read", new Stack<GrammarToken>(){{push(Terminal.readW); push(new NodeFactory("read")); push(Terminal.lpar); push(variable); push(Terminal.rpar); push(Terminal.semi); push(new SubTreeFactory("statement", 2));}});
        statement.tableEntry.put("while", new Stack<GrammarToken>(){{push(Terminal.whileW); push(new NodeFactory("while")); push(Terminal.lpar);  push(relExpr); push(Terminal.rpar); push(new NodeFactory("startWhile")); push(statBlock); push(new NodeFactory("endWhile")); push(Terminal.semi); push(new SubTreeFactory("statement", 5));}});
        statement.tableEntry.put("if", new Stack<GrammarToken>(){{push(Terminal.ifW); push(new NodeFactory("if")); push(Terminal.lpar); push(relExpr); push(Terminal.rpar); push(Terminal.thenW); push(new NodeFactory("then")); push(statBlock); push(Terminal.elseW); push(new NodeFactory("else")); push(statBlock); push(new NodeFactory("endIf")); push(Terminal.semi); push(new SubTreeFactory("statement", 7));}});

        statementIdnest.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(aParams); push(Terminal.rpar); push(statementIdnest2);}});
        statementIdnest.tableEntry.put("dot", new Stack<GrammarToken>(){{push(Terminal.dot); push(new NodeFactory("dot")); push(Terminal.id); push(new NodeFactory("id")); push(statementIdnest);}});
        statementIdnest.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(indice); push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON")); push(statementIdnest3);}});
        statementIdnest.tableEntry.put("assign", new Stack<GrammarToken>(){{push(assignOp); push(expr);}});

        statementIdnest2.tableEntry.put("dot", new Stack<GrammarToken>(){{push(Terminal.dot); push(new NodeFactory("dot")); push(Terminal.id); push(new NodeFactory("id")); push(statementIdnest);}});
        statementIdnest2.tableEntry.put("semi", new Stack<GrammarToken>(){{push(Terminal.EPSILON); push(new NodeFactory("EPSILON"));}});

        statementIdnest3.tableEntry.put("dot", new Stack<GrammarToken>(){{push(Terminal.dot); push(new NodeFactory("dot")); push(Terminal.id); push(new NodeFactory("id")); push(statementIdnest);}});
        statementIdnest3.tableEntry.put("assign", new Stack<GrammarToken>(){{push(assignOp); push(expr);}});
        
        term.tableEntry.put("id", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm); push(new SubTreeFactory("term", "factor", "EPSILON", "multOp", true));}});
        term.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm); push(new SubTreeFactory("term", "factor", "EPSILON", "multOp", true));}});
        term.tableEntry.put("minus", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm); push(new SubTreeFactory("term", "factor", "EPSILON", "multOp", true));}});
        term.tableEntry.put("plus", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm); push(new SubTreeFactory("term", "factor", "EPSILON", "multOp", true));}});
        term.tableEntry.put("not", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm); push(new SubTreeFactory("term", "factor", "EPSILON", "multOp", true));}});
        term.tableEntry.put("floatLit", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm); push(new SubTreeFactory("term", "factor", "EPSILON", "multOp", true));}});
        term.tableEntry.put("intLit", new Stack<GrammarToken>(){{push(factor); push(rightRecTerm); push(new SubTreeFactory("term", "factor", "EPSILON", "multOp", true));}});

        type.tableEntry.put("integer", new Stack<GrammarToken>(){{push(Terminal.integerW); push(new NodeFactory("integer")); push(new SubTreeFactory("type", 1));}});
        type.tableEntry.put("float", new Stack<GrammarToken>(){{push(Terminal.floatW); push(new NodeFactory("float")); push(new SubTreeFactory("type", 1));}});
        type.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(new NodeFactory("id")); push(new SubTreeFactory("type", 1));}});

        variable.tableEntry.put("id", new Stack<GrammarToken>(){{push(Terminal.id); push(new NodeFactory("id")); push(variable2);}});

        variable2.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON")); push(reptVariable); push(new SubTreeFactory("varIdnest", "dot", "EPSILON")); push(new SubTreeFactory("variable", 3));}});
        variable2.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(aParams); push(Terminal.rpar); push(varIdnest); push(new SubTreeFactory("varIdnest", "dot", "indiceList")); push(new SubTreeFactory("variable", 3));}});
        variable2.tableEntry.put("dot", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON")); push(reptVariable); push(new SubTreeFactory("varIdnest", "dot", "EPSILON")); push(new SubTreeFactory("variable", 3));}});
        variable2.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON")); push(reptVariable); push(new SubTreeFactory("varIdnest", "dot", "EPSILON")); push(new SubTreeFactory("variable", 3));}});

        varIdnest.tableEntry.put("dot", new Stack<GrammarToken>(){{push(Terminal.dot); push(new NodeFactory("dot")); push(Terminal.id); push(new NodeFactory("id")); push(varIdnest2);}});

        varIdnest2.tableEntry.put("rpar", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        varIdnest2.tableEntry.put("lpar", new Stack<GrammarToken>(){{push(Terminal.lpar); push(aParams); push(Terminal.rpar); push(varIdnest);}});
        varIdnest2.tableEntry.put("dot", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});
        varIdnest2.tableEntry.put("lsqbr", new Stack<GrammarToken>(){{push(reptIdnest1); push(new SubTreeFactory("indiceList", "indice", "EPSILON"));}});

        visibility.tableEntry.put("public", new Stack<GrammarToken>(){{push(Terminal.publicW); push(new NodeFactory("public")); push(new SubTreeFactory("visibility", 1));}});
        visibility.tableEntry.put("private", new Stack<GrammarToken>(){{push(Terminal.privateW); push(new NodeFactory("private")); push(new SubTreeFactory("visibility", 1));}});
        
        // Load the table
        table = new HashMap<String, NonTerminal>();
        table.put(START.name, START);
        table.put(aParams.name, aParams);
        table.put(aParamsTail.name, aParamsTail);
        table.put(addOp.name, addOp);
        table.put(arithExpr.name, arithExpr);
        table.put(arrayOrObject.name, arrayOrObject);
        table.put(arraySize.name, arraySize);
        table.put(arraySize2.name, arraySize2);
        table.put(assignOp.name, assignOp);
        table.put(classDecl.name, classDecl);
        table.put(classDeclOrFuncDef.name, classDeclOrFuncDef);
        table.put(expr.name, expr);
        table.put(expr2.name, expr2);
        table.put(fParams.name, fParams);
        table.put(fParamsTail.name, fParamsTail);
        table.put(factor2.name, factor2);
        table.put(factor.name, factor);
        table.put(funcBody.name, funcBody);
        table.put(funcDef.name, funcDef);
        table.put(funcHeadMemberTail.name, funcHeadMemberTail);
        table.put(funcHeadTail.name, funcHeadTail);
        table.put(funcHead.name, funcHead);
        table.put(idnest.name, idnest);
        table.put(idnest2.name, idnest2);
        table.put(indice.name, indice);
        table.put(localVarDecl.name, localVarDecl);
        table.put(localVarOrStat.name, localVarOrStat);
        table.put(memberDecl.name, memberDecl);
        table.put(memberFuncDecl.name, memberFuncDecl);
        table.put(memberFuncHead.name, memberFuncHead);
        table.put(memberVarDecl.name, memberVarDecl);
        table.put(multOp.name, multOp);
        table.put(optInherits.name, optInherits);
        table.put(prog.name, prog);
        table.put(relExpr.name, relExpr);
        table.put(relOp.name, relOp);
        table.put(reptProg0.name, reptProg0);
        table.put(reptAParams1.name, reptAParams1);
        table.put(reptArraySize.name, reptArraySize);
        table.put(reptFParams3.name, reptFParams3);
        table.put(reptFParams4.name, reptFParams4);
        table.put(reptFParamsTail4.name, reptFParamsTail4);
        table.put(reptIdnest1.name, reptIdnest1);
        table.put(reptInheritsList.name, reptInheritsList);
        table.put(reptLocalVarOrStat.name, reptLocalVarOrStat);
        table.put(reptMemberDecl.name, reptMemberDecl);
        table.put(reptStatBlock1.name, reptStatBlock1);
        table.put(reptVariable.name, reptVariable);
        table.put(reptVariableOrFunctionCall.name, reptVariableOrFunctionCall);
        table.put(returnType.name, returnType);
        table.put(rightRecArithExpr.name, rightRecArithExpr);
        table.put(rightRecTerm.name, rightRecTerm);
        table.put(sign.name, sign);
        table.put(statBlock.name, statBlock);
        table.put(statement.name, statement);
        table.put(statementIdnest.name, statementIdnest);
        table.put(statementIdnest2.name, statementIdnest2);
        table.put(statementIdnest3.name, statementIdnest3);
        table.put(term.name, term);
        table.put(type.name, type);
        table.put(variable.name, variable);
        table.put(variable2.name, variable2);
        table.put(varIdnest.name, varIdnest);
        table.put(varIdnest2.name, varIdnest2);
        table.put(visibility.name, visibility);

    }

    public static NonTerminal get(String key){
        return table.get(key);
    }

    public static int size(){
        return table.size();
    }
}
