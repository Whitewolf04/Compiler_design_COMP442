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
    LinkedList<CodeTabEntry> bufferStack;
    boolean main = false;

    public CodeGenerationVisitor1(CodeGenTable table){
        this.globalTable = table;
        statementStack = new Stack<String>();
        bufferStack = new LinkedList<CodeTabEntry>();
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

            String originOffset = Integer.toString(originEntry.getOffset());
            if(localTable.containsName(origin.getValue()) == null){
                // Class member variable
                originOffset = "objectOffsetBuf";
            }

            // Non-idnest call
            if(cur.checkContent("assign")){
                SyntaxTreeNode expr = cur.getRightSib();
                if(originOffset.equals("objectOffsetBuf")){
                    OutputWriter.codeDeclGen("\taddi r7,r0,objectOffsetBuf\t% Load buffer address onto r7");
                    OutputWriter.codeDeclGen("\tlw r7,0(r7)\t% Load variable address onto r7");
                    originOffset = "0(r7)";
                } else {
                    originOffset += "(r13)";
                }
                assignWriter(origin.getValue(), originOffset, expr);
            } else if(cur.checkContent("indiceList") && cur.getRightSib().checkContent("assign")){
                SyntaxTreeNode expr = cur.getRightSib().getRightSib();
                arrayIndiceWriter2(cur, originEntry, (originOffset.equals("objectOffsetBuf") ? true : false), (originEntry.kind.equals("parameter") ? true : false));

                // Assign this array element to the new value
                OutputWriter.codeDeclGen("% Storing " + expr.getValue() + " into " + originEntry.name);
                OutputWriter.codeDeclGen("\taddi r7,r0,arrayOffsetBuf\t% Load buffer address onto r7");
                OutputWriter.codeDeclGen("\tlw r1,0(r7)\t\t% Get address stored in buffer");
                OutputWriter.codeDeclGen("\tlw r2," + expr.getAddress());
                OutputWriter.codeDeclGen("\tsw 0(r1),r2");
                
            } else if(cur.checkContent("exprList") && cur.getRightSib().isEpsilon()) {
                // Must be a global function
                globalFuncCallWriter(cur, originEntry);
            } else if(cur.checkContent("dot") && cur.getRightSib().getRightSib().checkContent("assign")){
                // Object mem-var access
                String varName = cur.getRightSib().getValue();
                SyntaxTreeNode expr = cur.getRightSib().getRightSib().getRightSib();

                // Member variable assign
                CodeTabEntry idEntry = findVariableIn(varName, findVariableIn(origin.getType(), globalTable).link);

                // Get the offset of member variable
                OutputWriter.codeDeclGen("\taddi r1,r0," + idEntry.getOffset() + "\t% Get offset of member variable " + idEntry.name + " in object");
                
                String tempAddress = memVarAssignWriter(origin.getValue(), varName);
                tempAddress = bufferUnpacker(tempAddress);
                OutputWriter.codeDeclGen("\tlw r7,0(r7)\t% Load mem-var into r7");

                assignWriter(origin.getValue() + "." + varName, tempAddress, expr);
            } else {
                // TODO: Idnest calls

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
                node.setAddress(termList.getAddress());
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
                node.setAddress(Integer.toString(tempvar.getOffset()) + "(r13)");
            } else if(childNum == 2){
                SyntaxTreeNode sign = node.getChild();
                SyntaxTreeNode factor = sign.getRightSib();

                if(sign.checkContent("minus")){
                    negativeSignedIntWriter(factor);
                }

                // Set the current node to the previous node's address
                node.setValue(factor.getValue());
                node.setAddress(factor.getAddress());
            } else {
                SyntaxTreeNode id = node.getChild();
                SyntaxTreeNode indiceOrExpr = id.getRightSib();
                SyntaxTreeNode idnestList = indiceOrExpr.getRightSib();
                CodeTabEntry idEntry = findVariable(id.getValue());

                String idName = idEntry.name;
                String idOffset = Integer.toString(idEntry.getOffset());

                if(localTable.containsName(id.getValue()) == null){
                    // ID is a variable from class
                    idOffset = "objectOffsetBuf";
                }

                if(indiceOrExpr.checkContent("indiceList") && indiceOrExpr.getChild().isEpsilon() && idnestList.getChild().isEpsilon()){
                    // There's only id
                    node.setValue(idName);
                    if(idOffset.equals("objectOffsetBuf")){
                        OutputWriter.codeDeclGen("\taddi r7,r0,objectOffsetBuf\t% Load buffer address onto r7");
                        OutputWriter.codeDeclGen("\tlw r7,0(r7)\t% Load variable address onto r7");
                        idOffset = "0(r7)";
                    } else {
                        idOffset += "(r13)";
                    }
                    node.setAddress(idOffset);
                } else {
                    if(idnestList.getChild().isEpsilon()){
                        if(indiceOrExpr.checkContent("indiceList")){
                            // Handling for array
                            arrayIndiceWriter2(indiceOrExpr, idEntry, (idOffset.equals("objectOffsetBuf") ? true : false), (idEntry.kind.equals("parameter") ? true : false));
                            
                            // Store the offset in a temp variable
                            CodeTabEntry tempArrVar = localTable.tempArrVar.pop();
                            OutputWriter.codeDeclGen("% Store the array offset in a temp variable " + tempArrVar.name);
                            OutputWriter.codeDeclGen("\taddi r7,r0,arrayOffsetBuf\t% Load buffer address onto r7");
                            OutputWriter.codeDeclGen("\tlw r1,0(r7)\t\t% Get offset stored in buffer");
                            OutputWriter.codeDeclGen("\tlw r3,0(r1)\t% Load the element onto r3");
                            OutputWriter.codeDeclGen("\tsw " + tempArrVar.getOffset() + "(r13),r3\t% Store element into tempvar");
                            node.setValue(tempArrVar.name);
                            node.setAddress(tempArrVar.getOffset() + "(r13)");
                        } else {
                            // Handling for global function call
                            globalFuncCallWriter(indiceOrExpr, idEntry);
                        }
                        
                    } else if(indiceOrExpr.getChild().isEpsilon()){
                        // Handling for idnest calls
                        // WARNING: Handle only 1 idnest and not the entire chain
                        SyntaxTreeNode idnest = idnestList.getChild();
                        SyntaxTreeNode idIdnest = idnest.getChild().getRightSib();
                        SyntaxTreeNode indiceOrExprIdnest = idIdnest.getRightSib();

                        if(indiceOrExprIdnest.checkContent("indiceList")){
                            if(indiceOrExprIdnest.getChild().isEpsilon()){
                                // class member variable call
                                CodeTabEntry idIdnestEntry = findVariableIn(idIdnest.getValue(), findVariableIn(idEntry.type, globalTable).link);

                                // Get the offset of member variable
                                OutputWriter.codeDeclGen("\taddi r1,r0,0");
                                OutputWriter.codeDeclGen("\taddi r1,r0," + idIdnestEntry.getOffset() + "\t% Get offset of member variable " + idIdnestEntry.name + " in object");
                                
                                String tempAddress = memVarCallWriter(idEntry.name, idIdnestEntry.name);
                                node.setValue(idIdnestEntry.name);
                                node.setAddress(tempAddress);
                            } else {
                                // class member array indicing

                            }
                        } else {
                            // class member function call
                            CodeTabEntry funcEntry = findVariableIn(idIdnest.getValue(), findVariable(idEntry.type).link);
                            CodeTabEntry objectEntry = findVariableIn(idName, localTable);
                            memFuncCallWriter(indiceOrExprIdnest, funcEntry, objectEntry);
                            if(!funcEntry.getReturnType().equals("void")){
                                CodeTabEntry buffer = getBuffer("funcReturnBuf");
                                OutputWriter.codeDeclGen("\taddi r7,r0," + buffer.name + "\t% Load buffer address");
                                OutputWriter.codeDeclGen("\tsw 0(r7),r8\t% Store return value in buffer");
                                node.setValue(buffer.name);
                                node.setAddress("funcReturnBuf");
                            }
                        }
                    } else {
                        // Both funcall and idnest
                        return;
                    }
                }
            }
        } else if(node.checkContent("funcDef")){
            CodeTabEntry link = findVariable("link");

            if(!main){
                OutputWriter.codeDeclGen("\tlw r15," + link.getOffset() + "(r13)");
                OutputWriter.codeDeclGen("\tjr r15\t% Jump back to the calling function");
            }

            ListIterator<CodeTabEntry> i = localTable.getTable().listIterator();
            while(i.hasNext()){
                CodeTabEntry cur = i.next();

                if(cur.kind.equals("tempvar") || cur.kind.equals("tempArrVar")){
                    bufferStack.add(cur);
                }
            }
            
            // reset main boolean
            main = false;
        } else if(node.checkContent("funcHead")){
            String funcName = node.getTableEntry().getName();
            // Get the func name for member functions
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
                OutputWriter.codeDeclGen("% Start of function " + localTable.name);
                OutputWriter.codeDeclGen(localTable.moonName + "\tnop");

                // Add the entry point for main function
                if(funcName.equals("main")){
                    OutputWriter.codeDeclGen("\tentry");
                    OutputWriter.codeDeclGen("\talign");
                    main = true;
                    OutputWriter.codeDeclGen("\taddi r13,r0,topaddr\t% initialize the frame pointer");
                } else {
                    OutputWriter.codeDeclGen("\tsubi r13,r13," + localTable.scopeSize + "\t% set the stack pointer to the top position of the stack");
                }
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
            OutputWriter.codeDeclGen("% Start of class " + localTable.name);
            OutputWriter.codeDeclGen(localTable.name + "\tnop");
            OutputWriter.codeDeclGen("\taddi r14, r0, topaddr\t% initialize the stack pointer");
            OutputWriter.codeDeclGen("\taddi r13, r0, topaddr\t% initialize the frame pointer");
            OutputWriter.codeDeclGen("\tsubi r14, r14, " + localTable.scopeSize + "\t% set the stack pointer to the top position of the stack");
        } else if(node.checkContent("localVarDecl") && main){
            // Handle for object declaration
            SyntaxTreeNode id = node.getChild();
            SyntaxTreeNode type = id.getRightSib();
            SyntaxTreeNode arrayOrObject = type.getRightSib();

            if(arrayOrObject.getChild().checkContent("exprList")){
                // Handle for object declaration
                CodeTabEntry classEntry = findVariable(type.getChild().getValue());
                if(classEntry.kind.equals("class")){
                    bufferStack.add(new CodeTabEntry(id.getValue(), "object", classEntry.type, classEntry.size, 0));
                } else {
                    // Skip on non-object declaration
                    return;
                }

                CodeTabEntry constructorEntry = classEntry.link.containsName("constructor");
                CodeTabEntry objectEntry = findVariableIn(id.getValue(), localTable);
                memFuncCallWriter(arrayOrObject.getChild(), constructorEntry, objectEntry);
            } else {
                // Handle for object array
                if(arrayOrObject.getChild().getChild().isEpsilon()){
                    // Skip for primitive type declaration
                    return;
                } else if(type.getChild().getValue().equals("integer") || type.getChild().getValue().equals("float")){
                    // Skip for primitive type arrays
                    return;
                } else {
                    // TODO: Handle for object array
                    return;
                }
            }
        } else if(node.checkContent("prog")){
            ListIterator<CodeTabEntry> i = bufferStack.listIterator();

            OutputWriter.codeDeclGen("\thlt");
            OutputWriter.codeDeclGen("\n% End of program, declaring variables");
            while(i.hasNext()){
                CodeTabEntry cur = i.next();

                OutputWriter.codeDeclGen(cur.name + "\tres " + cur.size);
            }
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
            } else if(statementType.checkContent("return")){
                SyntaxTreeNode expr = statementType.getRightSib();

                if(expr.getType() == null || expr.getType().equals("ERR@!")){
                    // Skip through unknown or error types
                    return;
                } else {
                    OutputWriter.codeDeclGen("\tlw r1," + expr.getAddress() + "\t% Get the return value " + expr.getValue());
                    OutputWriter.codeDeclGen("\tsw 0(r13),r1");
                }
            } else if(statementType.checkContent("read")){
                SyntaxTreeNode variable = statementType.getRightSib();

                if(variable.getType().equals("integer")){
                    // WARNING: Can only handle variable as a local variable for now
                    // Get address of variable
                    SyntaxTreeNode id = variable.getChild();
                    CodeTabEntry idEntry = findVariableIn(id.getValue(), localTable);
                    id.setAddress(idEntry.getOffset() + "(r13)");

                    // Write to moon code
                    readIntWriter(id);
                } else {
                    // Skip through non-integer type variables
                    return;
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
                        node.setAddress(prev.getAddress());
                        break;
                    }
                }
            } else {
                // There's only 1 factor under this term
                node.setValue(node.getChild().getValue());
                node.setAddress(node.getChild().getAddress());
            }
        } else if(node.checkContent("termList")){
            if(node.getChildNum() > 2){
                SyntaxTreeNode cur = node.getChild();
                SyntaxTreeNode prev = cur;

                while(cur != null){
                    if(cur.checkContent("addOp")){
                        if(prev.getValue() == null || cur.getRightSib().getValue() == null){
                            // Skip through if factor is not resolved
                        } else if(cur.getChild().checkContent("plus")){
                            plusWriter(prev, cur.getRightSib());
                        } else if(cur.getChild().checkContent("minus")){
                            minusWriter(prev, cur.getRightSib());
                        }
                    }
                    prev = cur;
                    cur = cur.getRightSib();
                    if(cur.isEpsilon()){
                        // Store the final result of the calculation
                        node.setValue(prev.getValue());
                        node.setAddress(prev.getAddress());
                        break;
                    }
                }
            } else {
                // There's only one term
                node.setValue(node.getChild().getValue());
                node.setAddress(node.getChild().getAddress());
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
        if(!main){
            CodeTabEntry link = findVariable("link");
            OutputWriter.codeDeclGen("\tsw " + link.getOffset() + "(r13),r15\t% Put link onto stack frame");
        }
        

        CodeTabEntry self = findVariable("self");
        if(self != null){
            OutputWriter.codeDeclGen("\tsw " + self.getOffset() + "(r13),r12\t% Store the object address onto the self");
        }

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

        OutputWriter.codeDeclGen("% Adding " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(LHS.getAddress()));
        OutputWriter.codeDeclGen("\tlw r2," + bufferUnpacker(RHS.getAddress()));
        OutputWriter.codeDeclGen("\tadd r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setAddress(Integer.toString(resultTempVar.getOffset()) + "(r13)");
    }

    private void minusWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        OutputWriter.codeDeclGen("% Subtracting " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(LHS.getAddress()));
        OutputWriter.codeDeclGen("\tlw r2," + bufferUnpacker(RHS.getAddress()));
        OutputWriter.codeDeclGen("\tsub r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setAddress(Integer.toString(resultTempVar.getOffset()) + "(r13)");
    }

    private void multWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        // Get the tempvar to store the result of this calculation
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        OutputWriter.codeDeclGen("% Multiplying " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(LHS.getAddress()));
        OutputWriter.codeDeclGen("\tlw r2," + bufferUnpacker(RHS.getAddress()));
        OutputWriter.codeDeclGen("\tmul r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setAddress(Integer.toString(resultTempVar.getOffset()) + "(r13)");;
    }

    private void divWriter(SyntaxTreeNode LHS, SyntaxTreeNode RHS){
        CodeTabEntry resultTempVar = findCorrectTempVar(LHS.getType(), true);

        OutputWriter.codeDeclGen("% Dividing " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(LHS.getAddress()));
        OutputWriter.codeDeclGen("\tlw r2," + bufferUnpacker(RHS.getAddress()));
        OutputWriter.codeDeclGen("\tdiv r3,r1,r2");
        OutputWriter.codeDeclGen("\tsw " + resultTempVar.getOffset() + "(r13),r3\t% Store result into " + resultTempVar.name);

        // Set the result for the next calculation
        RHS.setValue(resultTempVar.name);
        RHS.setAddress(Integer.toString(resultTempVar.getOffset()) + "(r13)");;
    }

    private void negativeSignedIntWriter(SyntaxTreeNode factor){
        OutputWriter.codeDeclGen("% Changing the sign of " + factor.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(factor.getAddress()));
        OutputWriter.codeDeclGen("\tmuli r1,r1,-1");
        OutputWriter.codeDeclGen("\tsw " + bufferUnpacker(factor.getAddress()) + ",r1");
    }

    private void assignWriter(String originValue, String originAddress, SyntaxTreeNode target){
        OutputWriter.codeDeclGen("% Assigning " + target.getValue() + " to " + originValue);
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(target.getAddress()));
        OutputWriter.codeDeclGen("\tsw " + bufferUnpacker(originAddress) + ",r1");
    }

    private void writeIntWriter(SyntaxTreeNode expr){
        OutputWriter.codeDeclGen("% Printing " + expr.getValue() + " to console");
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(expr.getAddress()));
        OutputWriter.codeDeclGen("\tjl r15,putint");
        OutputWriter.codeDeclGen("\taddi r12,r0,32");
        OutputWriter.codeDeclGen("\tputc r12");
    }

    private void writeFloatWriter(SyntaxTreeNode expr){

    }

    private void writeIntComparison(SyntaxTreeNode LHS, SyntaxTreeNode relOp, SyntaxTreeNode RHS){

        OutputWriter.codeDeclGen("% Comparision between " + LHS.getValue() + " and " + RHS.getValue());
        OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(LHS.getAddress()));
        OutputWriter.codeDeclGen("\tlw r2," + bufferUnpacker(RHS.getAddress()));
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

    private void arrayIndiceWriter2(SyntaxTreeNode indiceList, CodeTabEntry id, boolean memvar, boolean param){
        // Get type size
        int size = arrayTypeSize(id.type);
        if(size == -1){
            return;
        }

        // Get all indices
        Stack<Integer> indexUnpack = indiceHandling(id.type);
        if(indexUnpack == null){
            return;
        }
        
        // Go through each indice
        SyntaxTreeNode indice = indiceList.getChild();
        if(indice.isEpsilon()){
            return;
        }

        // Store the base address offset into register 9
        OutputWriter.codeDeclGen("% Get base address offset of array " + id.name);
        if(memvar){
            OutputWriter.codeDeclGen("\taddi r1,r0,objectOffsetBuf\t% Load buffer's address onto r1");
            OutputWriter.codeDeclGen("\tlw r1,0(r1)");
            OutputWriter.codeDeclGen("\tadd r9,r0,r1");
        } else if(param){
            OutputWriter.codeDeclGen("\tlw r1," + id.getOffset() + "(r13)\t\t% Load array address onto r1");
            OutputWriter.codeDeclGen("\tadd r9,r0,r1");
        } else {
            OutputWriter.codeDeclGen("\tadd r9,r0,r13");
            OutputWriter.codeDeclGen("\taddi r9,r9," + id.getOffset());
        }

        // Offset the base address
        OutputWriter.codeDeclGen("% Offsetting array " + id.name);
        while(indice != null && !indice.isEpsilon()){
            OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(indice.getChild().getAddress()) + "\t\t% Loading index " + indice.getChild().getValue());
            OutputWriter.codeDeclGen(("\tmuli r2,r1," + indexUnpack.pop() + "\t\t% Multiply with number of columns"));
            OutputWriter.codeDeclGen("\tmuli r2,r2," + size + "\t\t% Multiply with array type");
            OutputWriter.codeDeclGen("\tmuli r2,r2,-1\t\t% Convert back to negative offset");
            OutputWriter.codeDeclGen("\tadd r9,r9,r2");

            indice = indice.getRightSib();
        }

        // Store the offset into a buffer
        CodeTabEntry buffer = getBuffer("arrayOffsetBuf");
        OutputWriter.codeDeclGen("\taddi r7,r0," + buffer.name + "\t\t% Load memory address of " + buffer.name + " onto r7");
        OutputWriter.codeDeclGen("\tsw 0(r7),r9");
    }

    private void arrayIndiceWriter(SyntaxTreeNode indiceList, CodeTabEntry id, boolean memvar){
        // Get type size
        int size = arrayTypeSize(id.type);
        if(size == -1){
            return;
        }

        // Get all indices
        Stack<Integer> indexUnpack = indiceHandling(id.type);
        if(indexUnpack == null){
            return;
        }
        
        // Go through each indice
        SyntaxTreeNode indice = indiceList.getChild();
        if(indice.isEpsilon()){
            return;
        }

        // Store the base address offset into register 9
        OutputWriter.codeDeclGen("% Get base address offset of array " + id.name);
        if(memvar){
            OutputWriter.codeDeclGen("\tlw r1,objectOffsetBuf(r0)");
            OutputWriter.codeDeclGen("\tsub r9,r0,r1");
        } else {
            OutputWriter.codeDeclGen("\tsubi r9,r0," + id.getOffset());
        }

        // Offset the base address
        OutputWriter.codeDeclGen("% Offsetting array " + id.name);
        while(indice != null && !indice.isEpsilon()){
            OutputWriter.codeDeclGen("\tlw r1," + bufferUnpacker(indice.getChild().getAddress()) + "\t% Loading index " + indice.getChild().getValue());
            OutputWriter.codeDeclGen(("\tmuli r2,r1," + indexUnpack.pop() + "\t% Multiply with number of columns"));
            OutputWriter.codeDeclGen("\tmuli r2,r2," + size + "\t% Multiply with array type");
            OutputWriter.codeDeclGen("\tadd r9,r9,r2");

            indice = indice.getRightSib();
        }
        // Convert back to negative offset
        OutputWriter.codeDeclGen("\tsub r9,r0,r9");

        // Store the offset into a buffer
        CodeTabEntry buffer = getBuffer("arrayOffsetBuf");
        OutputWriter.codeDeclGen("\taddi r7,r0," + buffer.name + "\t% Load memory address of " + buffer.name + " onto r7");
        OutputWriter.codeDeclGen("\tsw 0(r7),r9");
    }

    /*
     * Description: Member function call, write to Moon code
     * Input: List of parameters (exprList), function table entry (funcEntry), calling object table entry (objectEntry)
     */
    private void memFuncCallWriter(SyntaxTreeNode exprList, CodeTabEntry funcEntry, CodeTabEntry objectEntry){
        OutputWriter.codeDeclGen("% Calling member function " + funcEntry.link.name);
        int funcSize = funcEntry.link.scopeSize;
                
        SyntaxTreeNode expr = exprList.getChild();
        int counter = 1;
        while(expr != null && !expr.isEpsilon()){
            // Check if parameter is an array
            if(arrayIdentifier(expr.getType())){
                CodeTabEntry arrayEntry = findVariableIn(expr.getValue(), localTable);
                String arrayAddress = null;

                if(arrayEntry == null){
                    // Array is a member variable
                    arrayAddress = memVarCallWriter("self", expr.getValue());
                    OutputWriter.codeDeclGen("\tlw r" + counter++ + "," + bufferUnpacker(arrayAddress) + "\t% Load parameter array " + expr.getValue());
                } else if(arrayEntry.kind.equals("parameter")){
                    // Array is a parameter
                    arrayAddress = expr.getAddress();
                    OutputWriter.codeDeclGen("\tlw r" + counter++ + "," + bufferUnpacker(arrayAddress) + "\t% Load parameter array " + expr.getValue());
                } else {
                    // Array is a local variable
                    String offset = expr.getAddress().substring(0, expr.getAddress().indexOf('('));
                    String register = "r" + counter++;
                    OutputWriter.codeDeclGen("% Loading array address onto " + register + " as a paramter");
                    OutputWriter.codeDeclGen("\taddi " + register + ",r0," + offset);
                    OutputWriter.codeDeclGen("\tadd " + register + "," + register + ",r13");
                }
            } else {
                OutputWriter.codeDeclGen("\tlw r" + counter++ + "," + bufferUnpacker(expr.getAddress()) + "\t% Load parameter " + expr.getValue());
            }
            expr = expr.getRightSib();
        }
        OutputWriter.codeDeclGen("\taddi r12,r13," + objectEntry.offset*(-1) + "\t% Load object address onto r12");
        OutputWriter.codeDeclGen("\tjl r15," + funcEntry.link.moonName);

        // Store return value if there is a return
        if(!funcEntry.getReturnType().equals("void")){
            OutputWriter.codeDeclGen("\tlw r8,0(r13)\t% Load return value onto r8");
        }

        // Go back to current function
        OutputWriter.codeDeclGen("\taddi r13,r13," + funcSize);
        // OutputWriter.codeDeclGen("\taddi r14,r14," + funcSize);
    }

    /*
     * Description: Global function call, write to moon code
     * Input: Parameter list node (exprList), function table entry (idEntry)
     */
    private void globalFuncCallWriter(SyntaxTreeNode exprList, CodeTabEntry idEntry){
        int funcSize = idEntry.link.scopeSize;
                
        SyntaxTreeNode expr = exprList.getChild();
        int counter = 1;
        while(expr != null && !expr.isEpsilon()){
            // Check if parameter is an array
            if(arrayIdentifier(expr.getType())){
                CodeTabEntry arrayEntry = findVariableIn(expr.getValue(), localTable);
                String arrayAddress = null;

                if(arrayEntry == null){
                    // Array is a member variable
                    arrayAddress = memVarCallWriter("self", expr.getValue());
                    OutputWriter.codeDeclGen("\tlw r" + counter++ + "," + bufferUnpacker(arrayAddress) + "\t% Load parameter array " + expr.getValue());
                } else if(arrayEntry.kind.equals("parameter")){
                    // Array is a parameter
                    arrayAddress = expr.getAddress();
                    OutputWriter.codeDeclGen("\tlw r" + counter++ + "," + bufferUnpacker(arrayAddress) + "\t% Load parameter array " + expr.getValue());
                } else {
                    // Array is a local variable
                    String offset = expr.getAddress().substring(0, expr.getAddress().indexOf('('));
                    String register = "r" + counter++;
                    OutputWriter.codeDeclGen("% Loading array address onto " + register + " as a paramter");
                    OutputWriter.codeDeclGen("\taddi " + register + ",r0," + offset);
                    OutputWriter.codeDeclGen("\tadd " + register + "," + register + ",r13");
                }
            } else {
                OutputWriter.codeDeclGen("\tlw r" + counter++ + "," + bufferUnpacker(expr.getAddress()) + "\t% Load parameter " + expr.getValue());
            }
            expr = expr.getRightSib();
        }
        OutputWriter.codeDeclGen("\tjl r15," + idEntry.link.moonName);

        // Store return value if there is a return
        if(!idEntry.getReturnType().equals("void")){
            OutputWriter.codeDeclGen("\tlw r8,0(r13)\t% Load return value onto r8");
        }

        // Go back to current function
        OutputWriter.codeDeclGen("\taddi r13,r13," + funcSize);
        OutputWriter.codeDeclGen("\taddi r14,r14," + funcSize);
    }

    // WARNING: Only for local object or self member variable access
    // Return: Member variable value stored in buffer
    // Prerequisite: Load the offset of member variable in object onto r1
    private String memVarCallWriter(String objectID, String varName){
        // String outputAddress = null;
        if(objectID.equals("self")){
            // Accessing from self variable
            CodeTabEntry self = findVariableIn("self", localTable);
            OutputWriter.codeDeclGen("\tlw r2," + self.getOffset() + "(r13)\t% Get object address");
            OutputWriter.codeDeclGen("\tadd r1,r1,r2\t% Get address of member variable " + varName);
            OutputWriter.codeDeclGen("\tlw r1,0(r1)\t% Load mem-var onto r1");

            // Store the member variable offset into a buffer
            CodeTabEntry buffer = getBuffer("memVarBuf");
            OutputWriter.codeDeclGen("\taddi r3,r0," + buffer.name + "\t% Load buffer address");
            OutputWriter.codeDeclGen("\tsw 0(r3),r1\t% Store mem-var in buffer");
        } else {
            // Calling from an object
            CodeTabEntry objectEntry = findVariableIn(objectID, localTable);
            OutputWriter.codeDeclGen("\taddi r2,r13," + objectEntry.getOffset() + "\t% Get the object address");
            OutputWriter.codeDeclGen("\tadd r2,r2,r1\t% Get the member variable address");
            OutputWriter.codeDeclGen("\tlw r2,0(r2)\t% Load mem-var onto r2");

            // Store the member variable into a buffer
            CodeTabEntry buffer = getBuffer("memVarBuf");
            OutputWriter.codeDeclGen("\taddi r3,r0," + buffer.name + "\t% Get the buffer address");
            OutputWriter.codeDeclGen("\tsw 0(r3),r2\t% Store mem-var in buffer");
        }
        // OutputWriter.codeDeclGen("% Get member variable");
        // OutputWriter.codeDeclGen("\tlw r3,0(r3)\t% Get member variable address");
        // outputAddress = "0(r3)";

        return "memVarBuf";
    }

    // WARNING: Only for local object or self member variable access
    // Return: Global address for the member variable
    // Prerequisite: Load the offset of member variable in object onto r1
    private String memVarAssignWriter(String objectID, String varName){
        // String outputAddress = null;
        if(objectID.equals("self")){
            // Accessing from self variable
            CodeTabEntry self = findVariableIn("self", localTable);
            OutputWriter.codeDeclGen("\tlw r2," + self.getOffset() + "(r13)\t% Get object address");
            OutputWriter.codeDeclGen("\tadd r1,r1,r2\t% Get address of member variable " + varName);

            // Store the member variable offset into a buffer
            CodeTabEntry buffer = getBuffer("memVarAddressBuf");
            OutputWriter.codeDeclGen("\taddi r3,r0," + buffer.name + "\t% Load buffer address");
            OutputWriter.codeDeclGen("\tsw 0(r3),r1\t% Store mem-var address in buffer");
        } else {
            // Calling from an object
            CodeTabEntry objectEntry = findVariableIn(objectID, localTable);
            OutputWriter.codeDeclGen("\taddi r2,r13," + objectEntry.getOffset() + "\t% Get the object address");
            OutputWriter.codeDeclGen("\tadd r2,r2,r1\t% Get the member variable address");

            // Store the member variable into a buffer
            CodeTabEntry buffer = getBuffer("memVarAddressBuf");
            OutputWriter.codeDeclGen("\taddi r3,r0," + buffer.name + "\t% Get the buffer address");
            OutputWriter.codeDeclGen("\tsw 0(r3),r2\t% Store mem-var address in buffer");
        }
        // OutputWriter.codeDeclGen("% Get member variable");
        // OutputWriter.codeDeclGen("\tlw r3,0(r3)\t% Get member variable address");
        // outputAddress = "0(r3)";

        return "memVarAddressBuf";
    }

    private String bufferUnpacker(String address){
        if(address.contains("Buf")){
            OutputWriter.codeDeclGen("% Get value from " + address);
            String register = null;
            if(address.contains("Offset")){
                register = "r9";
            } else if(address.contains("Return")){
                register = "r8";
            } else if(address.contains("Address")){
                register = "r7";
            } else {
                register = "r6";
            }
            OutputWriter.codeDeclGen("\taddi " + register + ",r0," + address + "\t\t% Load buffer address onto " + register);
            return ("0(" + register + ")");
        } else {
            return address;
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
                    // Variable has been found in an upper table
                    // Load the self pointer
                    CodeTabEntry self = findVariable("self");

                    if(self != null){
                        // Handle for member function calling to the class variables
                        OutputWriter.codeDeclGen("% Load the calling object from class " + outerTable.name);
                        OutputWriter.codeDeclGen("\tlw r1," + self.getOffset() + "(r13)");

                        // Get the offset of variable
                        OutputWriter.codeDeclGen("\taddi r1,r1," + variable.getOffset() + "\t% Get offset of member variable " + variable.name + " in object");

                        // Look for offset buffer in buffer list
                        CodeTabEntry buffer = getBuffer("objectOffsetBuf");

                        // Store the offset onto the buffer
                        OutputWriter.codeDeclGen("\taddi r7,r0," + buffer.name + "\t% Load buffer address onto r7");
                        OutputWriter.codeDeclGen("\tsw 0(r7),r1\t% Store offset in " + buffer.name);
                    }
                    
                    return variable;
                }
                outerTable = outerTable.outerTable;
            }
        }
        return variable;
    }

    private CodeTabEntry findVariableIn(String name, CodeGenTable table){
        CodeTabEntry variable = table.containsName(name);

        if(variable != null){
            return variable;
        } else {
            return null;
        }
    }

    private CodeTabEntry getBuffer(String name){
        ListIterator<CodeTabEntry> i = bufferStack.listIterator();

        while(i.hasNext()){
            CodeTabEntry cur = i.next();

            if(cur.name.equals(name)){
                return cur;
            }
        }
        CodeTabEntry output = new CodeTabEntry(name, "buffer", "integer", 4, 0);
        bufferStack.add(output);
        return output;
    }

    private void litvalStoreWriter(String value, CodeTabEntry tempvar){
        OutputWriter.codeDeclGen("% Storing " + value + " into " + tempvar.name);
        OutputWriter.codeDeclGen("\taddi r1, r0, 0");
        OutputWriter.codeDeclGen("\taddi r1, r0, " + value);
        OutputWriter.codeDeclGen("\tsw " + tempvar.getOffset() + "(r13), r1");
    }

    private void readIntWriter(SyntaxTreeNode variable){
        OutputWriter.codeDeclGen("% Reading in an integer");
        OutputWriter.codeDeclGen("\tjl r15,getint");
        OutputWriter.codeDeclGen("\tsw " + bufferUnpacker(variable.getAddress()) + ",r1\t% Store the integer into the variable");
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

    private boolean arrayIdentifier(String type){
        Pattern pattern = Pattern.compile("\\A.*(\\[.*\\])+\\Z");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()){
            return true;
        } else {
            return false;
        }
    }
}
