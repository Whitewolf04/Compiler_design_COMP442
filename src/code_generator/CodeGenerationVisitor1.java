package code_generator;

import java.util.ListIterator;
import java.util.Stack;

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

            // Non-idnest call
            if(cur.checkContent("assign")){
                SyntaxTreeNode expr = cur.getRightSib();
                assignWriter(origin, expr);
            } else {
                // TODO: idnest call
            }
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

                if(indiceOrExpr.checkContent("indiceList") && indiceOrExpr.getChild().isEpsilon() && idnestList.getChild().isEpsilon()){
                    // There's only id
                    CodeTabEntry idEntry = findVariable(id.getValue());
                    node.setValue(idEntry.name);
                    node.setOffset(idEntry.getOffset());
                } else {
                    if(idnestList.getChild().isEpsilon()){
                        // TODO: indice resolution or expr resolution
                        return;
                    }
                    if(indiceOrExpr.getChild().isEpsilon()){
                        // TODO: idnest resolution
                        return;
                    }
                }
            }
        } else if(node.checkContent("funcHead")){
            // Visitor is at a funcDef
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
            tableVisitor(localTable);
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
            tableVisitor(localTable);
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
        } else if(node.checkContent("write")){
            SyntaxTreeNode expr = node.getRightSib();

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
    }

    private void tableVisitor(CodeGenTable table){
        // Reserve a memory amount for the whole class/function
        if(table.scopeSize == 0){
            return;
        } else {
            OutputWriter.codeDeclGen("\n");
            OutputWriter.codeDeclGen("% Start of function/class " + table.name);
            OutputWriter.codeDeclGen("\taddi r14, r0, topaddr\t% initialize the stack pointer");
            OutputWriter.codeDeclGen("\taddi r13, r0, topaddr\t% initialize the frame pointer");
            OutputWriter.codeDeclGen("\tsubi r14, r14, " + table.scopeSize + "\t% set the stack pointer to the top position of the stack");
            OutputWriter.codeDeclGen(table.name + "\tres " + table.scopeSize);
        }

        // Declare all member variables
        ListIterator<CodeTabEntry> i = table.getTable().listIterator();

        while(i.hasNext()){
            CodeTabEntry cur = i.next();
            
            // Only declare variables
            if(cur.kind.equals("variable") || cur.kind.equals("parameter")){
                if(cur.size == 0){
                    continue;
                } else {
                    OutputWriter.codeDeclGen(nameConverter(localTable.name) + cur.name + "\tres " + cur.size);
                }
            } else if(cur.kind.equals("tempvar") || cur.kind.equals("litval")){
                if(cur.size == 0){
                    continue;
                } else {
                    OutputWriter.codeDeclGen(cur.name + "\tres " + cur.size);
                }
            }
        }
        
    }

    private void plusWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        OutputWriter.codeDeclGen("% Adding " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tadd r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());
    }

    private void minusWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        OutputWriter.codeDeclGen("% Subtracting " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tsub r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());
    }

    private void multWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        OutputWriter.codeDeclGen("% Multiplying " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tmul r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());;
    }

    private void divWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        OutputWriter.codeDeclGen("% Dividing " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + LHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tdiv r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setOffset(resultTempVar.getOffset());;
    }

    private void assignWriter(SyntaxTreeNode origin, SyntaxTreeNode target){
        OutputWriter.codeDeclGen("% Assigning " + target.getValue() + " to " + origin.getValue());
        CodeTabEntry originEntry = findVariable(origin.getValue());
        
        OutputWriter.codeDeclGen("\tlw r1," + target.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tsw " + originEntry.getOffset() + "(r13),r1");
    }

    private void writeIntWriter(SyntaxTreeNode expr){
        OutputWriter.codeDeclGen("% Printing " + expr.getValue() + " to console");
        OutputWriter.codeDeclGen("\tlw r10," + expr.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tjl r15,putint");
    }

    private void writeFloatWriter(SyntaxTreeNode expr){

    }

    private void writeIntComparison(SyntaxTreeNode LHS, SyntaxTreeNode relOp, SyntaxTreeNode RHS){
        OutputWriter.codeDeclGen("\tlw r1," + LHS.getOffset() + "(r13)");
        OutputWriter.codeDeclGen("\tlw r2," + RHS.getOffset() + "(r13)");
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
}
