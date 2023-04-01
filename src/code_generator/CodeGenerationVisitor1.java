package code_generator;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;
import table_generator.Visitor;

public class CodeGenerationVisitor1 extends Visitor{
    private CodeGenTable globalTable;
    private CodeGenTable localTable;
    int statementCount = 0;
    Stack<String> statementStack;

    public CodeGenerationVisitor1(CodeGenTable table){
        this.globalTable = table;
        statementStack = new Stack<String>();
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("assignOrFuncCall")){
            SyntaxTreeNode cur = node.getChild();
            SyntaxTreeNode origin = node.getLeftmostSib();
            CodeTabEntry originEntry = findVariable(origin.getValue());

            // Check if origin has been resolved
            if(originEntry == null){
                return;
            }

            // Non-idnest call
            if(cur.checkContent("assign")){
                SyntaxTreeNode expr = cur.getRightSib();
                assignWriter(origin.getValue(), originEntry.getOffset(), expr);
            } else if(cur.checkContent("indiceList") && cur.getRightSib().checkContent("assign")){
                // Get type size of origin
                int size = arrayTypeSize(originEntry.type);
                if(size == -1){
                    return;
                }

                // Calculate array offset
                OutputWriter.codeDeclGen("% Offsetting array " + originEntry.name);
                Stack<Integer> indexUnpack = indiceHandling(origin.getType());
                SyntaxTreeNode expr = cur.getRightSib().getRightSib();

                if(indexUnpack == null){
                    // Skip unresolved array indicing
                    OutputWriter.codeDeclGen("% Offsetting fail for array " + originEntry.name);
                    return;
                } else {
                    // Go through each indice
                    SyntaxTreeNode indice = cur.getChild();

                    if(indice.isEpsilon()){
                        return;
                    }

                    // Store the base address offset into register 9
                    OutputWriter.codeDeclGen("\tsubi r9,r0," + originEntry.getOffset());
                    while(indice != null && !indice.isEpsilon()){
                        OutputWriter.codeDeclGen("\tlw r1," + indice.getChild().getOffset() + "(r13)\t% Loading index " + indice.getChild().getValue());
                        OutputWriter.codeDeclGen(("\tmuli r2,r1," + indexUnpack.pop() + "\t% Multiply with number of columns"));
                        OutputWriter.codeDeclGen("\tmuli r2,r2," + size + "\t% Multiply with array type");
                        OutputWriter.codeDeclGen("\tadd r9,r9,r2");

                        indice = indice.getRightSib();
                    }
                    // Convert back to negative offset
                    OutputWriter.codeDeclGen("\tsub r9,r0,r9");
                    

                    // Assign this array index to the new value
                    OutputWriter.codeDeclGen("% Storing " + expr.getValue() + " into " + originEntry.name);
                    OutputWriter.codeDeclGen("\tlw r1," + expr.getOffset() + "(r13)");
                    OutputWriter.codeDeclGen("\tsw r9(r13),r1");
                }
            } else {
                // TODO: Idnest calls

            }
        } else if(node.checkContent("classDecl")){
            OutputWriter.codeDeclGen(localTable.name + "\tres " + localTable.scopeSize);
        } else if(node.checkContent("else")){
            String statementName = statementStack.peek();
            OutputWriter.codeDeclGen("\tj " + statementName + "ENDIF");
            OutputWriter.codeDeclGen(statementName + "ELSE\taddi r0,r0,0");
            node.setValue(statementName + "ELSE");
        } else if(node.checkContent("endIf")){
            String statementName = statementStack.pop();
            OutputWriter.codeDeclGen(statementName + "ENDIF\taddi r0,r0,0");
            node.setValue(statementName + "ENDIF");
        } else if(node.checkContent("endWhile")){
            String statementName = statementStack.pop();
            OutputWriter.codeDeclGen("\tj " + statementName + "WHILE");
            OutputWriter.codeDeclGen(statementName + "ENDWHILE\taddi r0,r0,0");
            node.setValue(statementName + "ENDWHILE");
        } else if(node.checkContent("expr")){
            SyntaxTreeNode termList = node.getChild();
            SyntaxTreeNode expr2 = termList.getRightSib();

            if(expr2.isEpsilon()){
                node.setValue(termList.getValue());
                node.setOffset(termList.getOffset());
            } else {
                // TODO: Action for comparison expression
            }
        } else if(node.checkContent("factor")){
            int childNum = node.getChildNum();

            if(childNum == 1){
                // Use tempvar to store the number
                CodeTabEntry tempvar = findCorrectTempVar(node.getChild().getType(), false);
                litvalStoreWriter(node.getChild().getValue(), tempvar);
                node.setValue(tempvar.name);
                node.setOffset(tempvar.getOffset());
            } else if(childNum == 2){
                // TODO: Handling for signed variable
                SyntaxTreeNode factor = node.getChild().getRightSib();
                node.setValue(factor.getValue());
                node.setOffset(factor.getOffset());
            } else {
                SyntaxTreeNode id = node.getChild();
                SyntaxTreeNode indiceOrExpr = id.getRightSib();
                SyntaxTreeNode idnestList = indiceOrExpr.getRightSib();
                CodeTabEntry idEntry = findVariable(id.getValue());

                if(indiceOrExpr.checkContent("indiceList") && indiceOrExpr.getChild().isEpsilon() && idnestList.getChild().isEpsilon()){
                    // There's only id
                    node.setValue(idEntry.name);
                    node.setOffset(idEntry.getOffset());
                } else {
                    if(idnestList.getChild().isEpsilon()){
                        if(indiceOrExpr.checkContent("indiceList")){
                            Stack<Integer> indexUnpack = indiceHandling(id.getType());
                            int arrayTypeSize = arrayTypeSize(id.getType());

                            if(indexUnpack == null){
                                // SKip through unresolved array
                                return;
                            }

                            // Go through indice list
                            SyntaxTreeNode indice = indiceOrExpr.getChild();
                            OutputWriter.codeDeclGen("% Getting array element from array " + id.getValue());
                            OutputWriter.codeDeclGen("\taddi r9,r0,0");
                            while(indice != null && !indice.isEpsilon()){
                                OutputWriter.codeDeclGen("\tlw r1," + indice.getChild().getOffset() + "(r13)\t% Loading index " + indice.getChild().getValue());
                                OutputWriter.codeDeclGen(("\tmuli r2,r1," + indexUnpack.pop() + "\t% Multiply with number of columns"));
                                OutputWriter.codeDeclGen("\tmuli r2,r2," + arrayTypeSize + "\t% Multiply with array type");
                                OutputWriter.codeDeclGen("\tadd r9,r9,r2");

                                indice = indice.getRightSib();
                            }
                            
                            // Store the offset in a temp variable
                            CodeTabEntry tempArrVar = localTable.tempArrVar.pop();
                            OutputWriter.codeDeclGen("% Store the array offset in a temp array variable");
                            OutputWriter.codeDeclGen("\tsw -" + tempArrVar.offset + "(r13),r9");
                            node.setValue(tempArrVar.name);
                            node.setOffset(1);
                        } else {
                            // TODO: Handle for func call
                        }
                        
                    } else if(indiceOrExpr.getChild().isEpsilon()){
                        // TODO: idnest resolution
                        return;
                    } else {
                        // Both funcall and idnest
                        return;
                    }
                }
            }
        } else if(node.checkContent("funcDef")){
            OutputWriter.codeDeclGen(localTable.name + "\tres " + localTable.scopeSize);
        } else if(node.checkContent("funcHead")){
            String funcName = node.getTableEntry().getName();
            if(funcName.indexOf(':') != -1){
                funcName = funcName.substring(funcName.lastIndexOf(':')+1, funcName.length());
            }
            if(node.getChildNum() == 3){
                // Global function scope
                localTable = this.globalTable.containsName(node.getChild().getValue()).link;
            } else {
                // Member function scope
                String owner = node.getChild().getValue();
                CodeGenTable classTable = this.globalTable.containsName(owner).link;
                localTable = classTable.containsFunction(funcName, node.getTableEntry().getType()).link;
            }

            if(localTable.scopeSize == 0){
                return;
            } else {
                OutputWriter.codeDeclGen("\n");
                OutputWriter.codeDeclGen("% Start of function/class " + localTable.name);
                OutputWriter.codeDeclGen("\taddi r14, r0, topaddr\t% initialize the stack pointer");
                OutputWriter.codeDeclGen("\taddi r13, r0, topaddr\t% initialize the frame pointer");
                OutputWriter.codeDeclGen("\tsubi r14, r14, " + localTable.scopeSize + "\t% set the stack pointer to the top position of the stack");
            }
            funcDeclWriter();
        } else if(node.checkContent("if")){
            String statementName = "s"+statementCount++;
            localTable.statementList.add(new CodeTabEntry(statementName, "ifStat", "void", 0, 0));
            statementStack.push(statementName);
            OutputWriter.codeDeclGen("% If statement");
            OutputWriter.codeDeclGen(statementName + "IF\taddi r0,r0,0");
            node.setValue(statementName + "IF");
        } else if(node.checkContent("inheritList")){
            // Visitor is at a classDecl
            String className = node.getLeftmostSib().getValue();
            localTable = this.globalTable.containsName(className).link;
            OutputWriter.codeDeclGen("\n");
            OutputWriter.codeDeclGen("% Start of function/class " + localTable.name);
            OutputWriter.codeDeclGen("\taddi r14, r0, topaddr\t% initialize the stack pointer");
            OutputWriter.codeDeclGen("\taddi r13, r0, topaddr\t% initialize the frame pointer");
            OutputWriter.codeDeclGen("\tsubi r14, r14, " + localTable.scopeSize + "\t% set the stack pointer to the top position of the stack");
        } else if(node.checkContent("relExpr")){
            SyntaxTreeNode LHS = node.getChild();
            SyntaxTreeNode relOp = LHS.getRightSib();
            SyntaxTreeNode RHS = relOp.getRightSib();

            if(LHS.getType().equals("integer")){
                // Comparison for integer type
                writeIntComparison(LHS, relOp, RHS);
            } else if(LHS.getType().equals("float")){
                // Comparison for float type
            } else {
                // Comparison for incompatible types
                return;
            }
        } else if(node.checkContent("startWhile")){
            String statementName = statementStack.peek();
            OutputWriter.codeDeclGen("\tbz r11," + statementName + "ENDWHILE");
            OutputWriter.codeDeclGen(statementName + "STARTWHILE\taddi r0,r0,0");
            node.setValue(statementName + "STARTWHILE");
        } else if(node.checkContent("statement")){
            SyntaxTreeNode statementType = node.getChild();

            if(statementType.checkContent("write")){
                SyntaxTreeNode expr = statementType.getRightSib();

            if(expr.getType() == null || expr.getType().equals("ERR@!")){
                // Skip through unknown or error types
                return;
            } else {
                if(expr.getType().equals("integer")){
                    writeIntWriter(expr);
                } else if(expr.getType().equals("float")){
                    writeFloatWriter(expr);
                } else {
                    //TODO: Writer for objects
                }
            }
            }
        } else if(node.checkContent("term")){
            if(node.getChildNum() > 2){
                SyntaxTreeNode cur = node.getChild();
                SyntaxTreeNode prev = cur;
                while(cur != null){
                    if(cur.checkContent("multOp")){
                        if(prev.getValue() == null || cur.getRightSib().getValue() == null){
                            // Skip through if factor is not resolved
                        } else if(cur.getChild().checkContent("mult")){
                            multWriter(prev, cur.getRightSib());
                        } else if(cur.getChild().checkContent("div")){
                            divWriter(prev, cur.getRightSib());
                        }
                    }
                    prev = cur;
                    cur = cur.getRightSib();
                    if(cur.isEpsilon()){
                        // Store the final result of the calculation
                        node.setValue(prev.getValue());
                        node.setOffset(prev.getOffset());
                        break;
                    }
                }
            } else {
                // There's only 1 factor under this term
                node.setValue(node.getChild().getValue());
                node.setOffset(node.getChild().getOffset());
            }
        } else if(node.checkContent("termList")){
            if(node.getChildNum() > 2){
                SyntaxTreeNode cur = node.getChild();
                SyntaxTreeNode prev = cur;

                while(cur != null){
                    boolean unresolved = false;
                    if(cur.checkContent("plus")){
                        if(prev.getValue() == null || cur.getRightSib().getValue() == null){
                            // Skip through if term is not resolved
                            unresolved = true;
                        }
                        plusWriter(prev, cur.getRightSib());
                    } else if(cur.checkContent("minus")){
                        if(prev.getValue() == null || cur.getRightSib().getValue() == null){
                            // Skip through if term is not resolved
                            unresolved = true;
                        }
                        minusWriter(prev, cur.getRightSib());
                    }
                    prev = cur;
                    cur = cur.getRightSib();
                    if(cur.isEpsilon() && !unresolved){
                        // Store the final result of the calculation
                        node.setValue(prev.getValue());
                        node.setOffset(prev.getOffset());
                        break;
                    }
                    unresolved = false;
                }
            } else {
                // There's only one term
                node.setValue(node.getChild().getValue());
                node.setOffset(node.getChild().getOffset());
            }
        } else if(node.checkContent("then")){
            String statementName = statementStack.peek();
            OutputWriter.codeDeclGen("\tbnz r11," + statementName + "THEN");
            OutputWriter.codeDeclGen("\tj " + statementName + "ELSE");
            OutputWriter.codeDeclGen(statementName + "THEN\talign");
            node.setValue(statementName + "THEN");
        } else if(node.checkContent("while")){
            String statementName = "s" + statementCount++;
            localTable.statementList.add(new CodeTabEntry(statementName, "whileStat", "void", 0, 0));
            statementStack.push(statementName);
            OutputWriter.codeDeclGen("% While statement");
            OutputWriter.codeDeclGen(statementName + "WHILE\taddi r11,r0,0");
            node.setValue(statementName+"WHILE");
        }
    }

    private void funcDeclWriter(){
        // Save the link to this function
        CodeTabEntry link = findVariable("link");
        OutputWriter.codeDeclGen("\tsw " + link.getOffset() + "(r13),r15\t% Put link onto stack frame");

        LinkedList<CodeTabEntry> parameterList = localTable.parameterList;
        int counter = 1;
        while(!parameterList.isEmpty()){
            CodeTabEntry parameter = parameterList.poll();
            OutputWriter.codeDeclGen("\tsw " + parameter.getOffset() + "(r13),r" + counter++ + "\t% Storing parameter " + parameter.name + " into stack frame");
        }
    }

    private void plusWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        String LHSoffset = Integer.toString(LHS.getOffset());
        String RHSoffset = Integer.toString(RHS.getOffset());
        if(LHS.getOffset() > 0){
            LHSoffset = LHS.getValue();
        }
        if(RHS.getOffset() > 0) {
            RHSoffset = RHS.getValue();
        }

        OutputWriter.codeDeclGen("% Adding " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tadd r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());
    }

    private void minusWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        String LHSoffset = Integer.toString(LHS.getOffset());
        String RHSoffset = Integer.toString(RHS.getOffset());
        if(LHS.getOffset() > 0){
            LHSoffset = LHS.getValue();
        }
        if(RHS.getOffset() > 0) {
            RHSoffset = RHS.getValue();
        }

        OutputWriter.codeDeclGen("% Subtracting " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tsub r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());
    }

    private void multWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        String LHSoffset = Integer.toString(LHS.getOffset());
        String RHSoffset = Integer.toString(RHS.getOffset());
        if(LHS.getOffset() > 0){
            LHSoffset = LHS.getValue();
        }
        if(RHS.getOffset() > 0) {
            RHSoffset = RHS.getValue();
        }

        OutputWriter.codeDeclGen("% Multiplying " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tmul r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());;
    }

    private void divWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        String LHSoffset = Integer.toString(LHS.getOffset());
        String RHSoffset = Integer.toString(RHS.getOffset());
        if(LHS.getOffset() > 0){
            LHSoffset = LHS.getValue();
        }
        if(RHS.getOffset() > 0) {
            RHSoffset = RHS.getValue();
        }

        OutputWriter.codeDeclGen("% Dividing " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tdiv r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());;
    }

    private void assignWriter(String originValue, int originOffset, SyntaxTreeNode target){
        String targetOffset = Integer.toString(target.getOffset());
        if(target.getOffset() > 0){
            targetOffset = target.getValue();
        }
        OutputWriter.codeDeclGen("% Assigning " + target.getValue() + " to " + originValue);
        OutputWriter.codeDeclGen("\tlw r1," + targetOffset + "(r13)");
        OutputWriter.codeDeclGen("\tsw " + originOffset + "(r13),r1");
    }

    private void writeIntWriter(SyntaxTreeNode expr){
        String exprOffset = Integer.toString(expr.getOffset());
        if(expr.getOffset() > 0){
            exprOffset = expr.getValue();
        }
        OutputWriter.codeDeclGen("% Printing " + expr.getValue() + " to console");
        OutputWriter.codeDeclGen("\tlw r10," + exprOffset + "(r13)");
        OutputWriter.codeDeclGen("\tjl r15,putint");
    }

    private void writeFloatWriter(SyntaxTreeNode expr){

    }

    private void writeIntComparison(SyntaxTreeNode LHS, SyntaxTreeNode relOp, SyntaxTreeNode RHS){
        String LHSoffset = Integer.toString(LHS.getOffset());
        String RHSoffset = Integer.toString(RHS.getOffset());

        if(LHS.getOffset() > 0){
            LHSoffset = LHS.getValue();
        }
        if(RHS.getOffset() > 0){
            RHSoffset = RHS.getValue();
        }

        OutputWriter.codeDeclGen("% Comparision between " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHSoffset + "(r13)");
        OutputWriter.codeDeclGen("\tsub r3,r1,r2");

        if(relOp.checkContent("eq")){
            OutputWriter.codeDeclGen("\tceqi r11,r3,0");
        } else if(relOp.checkContent("noteq")){
            OutputWriter.codeDeclGen("\tcnei r11,r3,0");
        } else if(relOp.checkContent("gt")){
            OutputWriter.codeDeclGen("\tcgti r11,r3,0");
        } else if(relOp.checkContent("lt")){
            OutputWriter.codeDeclGen("\tclti r11,r3,0");
        } else if(relOp.checkContent("geq")){
            OutputWriter.codeDeclGen("\tcgei r11,r3,0");
        } else {
            OutputWriter.codeDeclGen("\tclei r11,r3,0");
        }
    }

    private CodeTabEntry findVariable(String name){
        CodeTabEntry variable = localTable.containsName(name);

        if(variable != null){
            // Variable is in local table
            return variable;
        } else {
            // Try to search in outer tables
            CodeGenTable outerTable = localTable.outerTable;
            while(outerTable != null){
                variable = outerTable.containsName(name);
                if(variable != null){
                    return variable;
                }
                outerTable = outerTable.outerTable;
            }

        }
        return variable;
    }

    // private String findVariableFromOffset(String offset){
    //     int offsetToFind = Integer.parseInt(offset)*(-1);
    //     ListIterator<CodeTabEntry> i = localTable.getTable().listIterator();

    //     while(i.hasNext()){
    //         CodeTabEntry cur = i.next();
    //         if(cur.offset < offsetToFind){
    //             continue;
    //         } else if(cur.offset == offsetToFind){
    //             return cur.name;
    //         } else {
    //             break;
    //         }
    //     }
    //     // Return null if offset not found
    //     return null;
    // }

    private String nameConverter(String name){
        String[] names = name.split("::");
        String output = "";
        for(String str : names){
            output += str;
        }
        output += 1;
        return output;
    }

    private void litvalStoreWriter(String value, CodeTabEntry tempvar){
        OutputWriter.codeDeclGen("% Storing " + value + " into " + tempvar.name);
        OutputWriter.codeDeclGen("\taddi r1, r0, 0");
        OutputWriter.codeDeclGen("\taddi r1, r0, " + value);
        OutputWriter.codeDeclGen("\tsw " + tempvar.getOffset() + "(r13), r1");
    }

    /*
     * Only applies to Integer or Float type
     */
    private CodeTabEntry findCorrectTempVar(String type, boolean tempvar){
        int size = -1;

        if(type.equals("integer")){
            size = 4;
        } else if(type.equals("float")){
            size = 8;
        } else {
            // TODO: Handling for array type
            return null;
        }

        if(tempvar){
            ListIterator<CodeTabEntry> i = localTable.tempvar.listIterator();
            int index = 0;

            while(i.hasNext()){
                CodeTabEntry cur = i.next();
                if(cur.size == size){
                    return localTable.tempvar.remove(index);
                }
                index++;
            }
            return null;
        } else {
            ListIterator<CodeTabEntry> i = localTable.litval.listIterator();
            int index = 0;

            while(i.hasNext()){
                CodeTabEntry cur = i.next();
                if(cur.size == size){
                    return localTable.litval.remove(index);
                }
                index++;
            }
            return null;
        }
        
    }

    private Stack<Integer> indiceHandling(String idType){
        Pattern pattern = Pattern.compile("\\A([^\\[]*){1}(\\[.*\\])\\Z");
        Matcher matcher = pattern.matcher(idType);
        Stack<Integer> sizeList = new Stack<Integer>();

        matcher.find();
        String dimension = matcher.group(2);

        // Store column data onto sizeList stack
        sizeList.push(1);
        while(dimension.indexOf('[') != -1){
            String dimensionSize = dimension.substring(dimension.lastIndexOf('[')+1, dimension.lastIndexOf(']'));
            if(dimensionSize.isEmpty()){
                return null;
            } else {
                int col = Integer.parseInt(dimensionSize) * sizeList.peek();
                sizeList.push(col);
                dimension = dimension.substring(0, dimension.lastIndexOf('['));
            }
        }
        sizeList.pop();

        return sizeList;
    }

    private int arrayTypeSize(String type){
        String elementType = type.substring(0, type.indexOf('['));

        if(elementType.equals("integer")){
            return 4;
        } else if(elementType.equals("float")){
            return 8;
        } else {
            return -1;
        }
    }
}
