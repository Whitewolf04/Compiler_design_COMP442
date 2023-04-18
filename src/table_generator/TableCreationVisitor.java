package table_generator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;
import lexical_analyzer.OutputWriter;

/*
 * List of functionalities
 * - Create global table
 * - Create class table
 * - Create function table
 * - Assign type for localvar declarations
 */
public class TableCreationVisitor extends Visitor{
    SymbolTable table;
    boolean mainDeclared = false;

    public TableCreationVisitor(){
        // Global table for the program
        table = new SymbolTable("global");
    }

    public void visit(SyntaxTreeNode node){
        if(node.checkContent("classDecl")){
            SyntaxTreeNode cur = node.getChild();
            
            // Get ID
            String name = cur.getValue();
            SymbolTable classTable = new SymbolTable(name);

            // Check if class has already been declared
            if(this.table.containsName(name) != null){
                OutputWriter.semanticErrWriting("ERROR: Class " + name + " on line " + cur.getLineCount() + " has already been declared before, so this declardation will be skipped!");
                return;
            }

            // Get inherit list
            cur = cur.getRightSib();
            SyntaxTreeNode parent = cur.getChild().getRightSib(); // skip through isa operator
            String type = name + ":";
            while(parent != null && !parent.isEpsilon()){
                // Check if this class has been declared
                SymTabEntry parentEntry = this.table.containsName(parent.getValue());
                if(parentEntry == null){
                    OutputWriter.semanticErrWriting("ERROR: Class " + parent.getValue() + " has not been declared and cannot be inherited!");
                    parent = parent.getRightSib();
                    continue;
                }

                // Add to inheritance list 
                type += parent.getValue() + ",";
                SymbolTable parentTable = parentEntry.getLink();
                classTable.addToInheritanceList(parentTable);
                parentTable.copyMemberToTable(classTable);
                parent = parent.getRightSib();
            }
            type = type.substring(0, type.length()-1);

            // Loop through member declaration list
            cur = cur.getRightSib().getChild();
            // Add self pointer
            classTable.addEntry(new SymTabEntry("self", "variable", name, classTable));
            // Add all variable and functions declared in this class
            while(cur != null && !cur.isEpsilon()){
                // Check for duplicates/overshadowing
                if(cur.getTableEntry().getKind().compareTo("function")==0){
                    SymTabEntry duplicate = classTable.containsFunction(cur.getTableEntry().getName(), cur.getTableEntry().getType());
                    if(duplicate != null){
                        duplicate.setLink(cur.getTableEntry().getLink());
                        duplicate.getLink().outerTable = classTable;
                        OutputWriter.semanticErrWriting("WARNING: Override of function " + cur.getTableEntry().getName() + " in class " + name);
                    } else {
                        cur.getTableEntry().getLink().outerTable = classTable;
                        classTable.addEntry(cur.getTableEntry());
                    }
                } else if(cur.getTableEntry().getKind().compareTo("variable")==0){
                    SymTabEntry duplicate = classTable.containsName(cur.getTableEntry().getName());
                    if(duplicate != null){
                        duplicate.setType(cur.getTableEntry().getType());
                        OutputWriter.semanticErrWriting("WARNING: Override of attribute " + cur.getTableEntry().getName() + " in class " + name);
                    } else {
                        classTable.addEntry(cur.getTableEntry());
                    }
                }
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(name, "class", type, classTable));
            this.table.addEntry(node.getTableEntry());
        } else if(node.checkContent("constructorDecl")){
            SyntaxTreeNode fParams = node.getChild().getChild();
            String paramTypes = "";
            SymbolTable table = new SymbolTable("constructor");
            while(fParams != null && !fParams.isEpsilon()){
                table.addEntry(fParams.getTableEntry());
                paramTypes += fParams.getTableEntry().getType() + ",";
                fParams = fParams.getRightSib();
            }

            String type = "void:" + paramTypes;
            type = type.substring(0, type.length()-1);

            node.setTableEntry(new SymTabEntry("constructor", "function", type, table));
        } else if(node.checkContent("floatLit")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "floatLit", "float"));
        } else if(node.checkContent("funcBody")){
            // Skip through
            return;
        } else if(node.checkContent("funcDef")){
            SymbolTable table = null;
            SymbolTable ownerTable = null;
            SyntaxTreeNode funcHead = node.getChild();
            Pattern pattern = Pattern.compile("\\A(.*?)::(.*?)\\Z");
            Matcher matcher = pattern.matcher(funcHead.getTableEntry().getName());
            String funcName = null;
            if(matcher.find()){
                // Member function definition
                String ownerName = matcher.group(1);
                funcName = matcher.group(2);
                SymTabEntry ownerEntry = this.table.containsName(ownerName);
                if(ownerEntry == null){
                    OutputWriter.semanticErrWriting("ERROR: Class " + ownerName + " has not been declared, so its function " + funcName + " cannot be defined!");
                    return;
                }
                ownerTable = ownerEntry.getLink();
                SymTabEntry funcEntry = ownerTable.containsFunction(funcName, funcHead.getTableEntry().getType());
                if(funcEntry == null){
                    OutputWriter.semanticErrWriting("ERROR: Function " + funcName + " has not been declared in class " + ownerName + ", so it cannot be defined!");
                    return;
                } else if(ownerTable.containsName(funcName) != null){
                    OutputWriter.semanticErrWriting("WARNING: Function " + funcName + " is being overloaded on line " + funcHead.getLineCount());
                }
                table = funcEntry.getLink();
                table.name = funcHead.getTableEntry().getName();
            } else {
                // Global function definition
                funcName = funcHead.getTableEntry().getName();
                table = new SymbolTable(funcName);
                table.outerTable = this.table;
                SymbolTable paramList = funcHead.getTableEntry().getLink();
                paramList.pushToTable(table);
                funcHead.getTableEntry().setLink(null);
                node.setTableEntry(new SymTabEntry(funcName, "function", funcHead.getTableEntry().getType(), table));
                this.table.addEntry(node.getTableEntry());
            }
            
            // Traverse funcBody node
            SyntaxTreeNode cur = funcHead.getRightSib().getChild();
            while(cur != null && !cur.isEpsilon()){
                if(cur.getTableEntry() == null){
                    // Skip through statement
                } else {
                    // Check for any duplicate declaration locally
                    SymTabEntry dupEntry = table.containsName(cur.getTableEntry().getName());
                    if(dupEntry != null){
                        OutputWriter.semanticErrWriting("WARNING: New declaration of " + cur.getTableEntry().getName() + " with type " + cur.getTableEntry().getType() + " will replace " + dupEntry.getName() + " of type " + dupEntry.getType() + " in function " + table.name);
                        dupEntry.setType(cur.getTableEntry().getType());
                        dupEntry.setKind(cur.getTableEntry().getKind());
                    } else {
                        if(ownerTable != null && ownerTable.containsName(cur.getTableEntry().getName()) != null){
                            OutputWriter.semanticErrWriting("WARNING: Overshadowing of member variable " + cur.getTableEntry().getName() + " in function " + funcName + ", line " + cur.getLineCount());
                        }

                        table.addEntry(cur.getTableEntry());
                    }
                }
                cur = cur.getRightSib();
            }
        } else if(node.checkContent("funcHead")){
            // Get ID and attach to class if this is member fund def
            SyntaxTreeNode cur = node.getChild();
            String name = cur.getValue();
            SymbolTable table = null;
            boolean isGlobal = false;
            if(node.getChildNum() == 3){
                // Global function
                isGlobal = true;
            } else if(node.getChildNum() == 4){
                // Constructor
                cur = cur.getRightSib().getRightSib(); // Skip through sr to constructor node
                name += "::constructor";
            } else if(node.getChildNum() == 5){
                // Member function
                cur = cur.getRightSib().getRightSib(); // Skip through sr node
                name += "::" + cur.getValue();
            }

            // Switch to fParamsList
            cur = cur.getRightSib();
            SyntaxTreeNode fParams = cur.getChild();
            String paramTypes = "";
            table = new SymbolTable();
            while(fParams != null && !fParams.isEpsilon()){
                table.addEntry(fParams.getTableEntry());
                paramTypes += fParams.getTableEntry().getType() + ",";
                fParams = fParams.getRightSib();
            }

            // Switch to returnType
            String type = null;
            if(node.getChildNum() == 4){
                type = "void:" + paramTypes;
                type = type.substring(0, type.length()-1);
            } else {
                cur = cur.getRightSib();
                type = cur.getTableEntry().getType() + ":" + paramTypes;
                type = type.substring(0, type.length()-1);
            }

            // Check if this function is main
            if(name.equals("main")){
                mainDeclared = true;
            }

            // Check for global function overloading/overriding
            if(isGlobal){
                if(this.table.containsParams(name, paramTypes) != null){
                    // Multiple declaration of a free function
                    OutputWriter.semanticErrWriting("ERROR: Function " + name + " has been declared before, line " + node.getLineCount());
                } else if(this.table.containsName(name) != null){
                    // Overloading a free function
                    OutputWriter.semanticErrWriting("WARNING: Function " + name + " is being overloaded on line " + node.getLineCount());
                }
            }
            
            node.setTableEntry(new SymTabEntry(name, "function", type, table));
        } else if(node.checkContent("fParamsList")){
            // Skip through
            return;
        } else if(node.checkContent("fParams")){
            // get ID
            SyntaxTreeNode cur = node.getChild();
            String name = cur.getTableEntry().getName();

            // get Type
            cur = cur.getRightSib();
            String type = cur.getTableEntry().getType();

            // get ArraySize
            cur = cur.getRightSib().getChild();
            while(cur != null && !cur.isEpsilon()){
                if(cur.getValue() == null){
                    type += "[" + "]";
                } else {
                    type += "[" + cur.getValue() + "]";
                }
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(name, "parameter", type));
        } else if(node.checkContent("id")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "id", null));
        } else if(node.checkContent("inheritList")){
            // Skip through
            return;
        } else if(node.checkContent("intLit")){
            node.setTableEntry(new SymTabEntry(node.getValue(), "intLit", "integer"));
        } else if(node.checkContent("localVarDecl")){
            SyntaxTreeNode cur = node.getChild();
            SyntaxTreeNode id = node.getChild();

            // Get ID
            String name = cur.getValue();

            // Get type
            cur = cur.getRightSib();
            String type = null;
            if(cur.getChild().checkContent("id")){
                SymTabEntry ownerEntry = this.table.containsName(cur.getChild().getValue());
                // Error handling
                if(ownerEntry == null){
                    OutputWriter.semanticErrWriting("ERROR: Class " + cur.getChild().getValue() + " hasn't been declared, so variable " + name + " cannot be created, line " + cur.getLineCount());
                    node.setTableEntry(new SymTabEntry(name, "variable", "ERR@!"));
                    return;
                }

                // Get the correct type
                if(ownerEntry.getKind().compareTo("class") == 0){
                    // type is a class
                    type = ownerEntry.getName();
                    cur = cur.getRightSib().getChild();
                    if(cur.checkContent("arraySizeList")){
                        cur = cur.getChild();
                        while(cur != null && !cur.isEpsilon()){
                            if(cur.getValue() == null){
                                type += "[" + "]";
                            } else {
                                type += "[" + cur.getValue() + "]";
                            }
                        }
                    }
                } else {
                    // type is returned by a function
                    Pattern pattern = Pattern.compile("\\A(.*?):(.*?)\\Z");
                    Matcher matcher = pattern.matcher(ownerEntry.getType());
                    if(matcher.find()){
                        type = matcher.group(1);
                    } else {
                        // In case there's no parameter
                        type = ownerEntry.getType();
                    }

                    // Error handling
                    cur = cur.getRightSib().getChild();
                    if(cur.checkContent("arraySizeList")){
                        OutputWriter.semanticErrWriting("ERROR: " + type + " is not an object!");
                        node.setTableEntry(new SymTabEntry(name, "variable", "ERR@!"));
                        return;
                    }
                }
            } else {
                type = cur.getChild().toString();
                // Get arraySizeList if available
                cur = cur.getRightSib().getChild();
                cur = cur.getChild();
                while(cur != null && !cur.isEpsilon()){
                    if(cur.getValue() == null){
                        type += "[" + "]";
                    } else {
                        type += "[" + cur.getValue() + "]";
                    }
                    cur = cur.getRightSib();
                }
            }
            id.setType(type);
            node.setTableEntry(new SymTabEntry(name, "variable", type));
        } else if(node.checkContent("localVarOrStat")){
            SyntaxTreeNode cur = node.getChild();

            // Skip non-localVar-declaration statements
            if(!cur.checkContent("localVarDecl")){
                return;
            }

            node.setTableEntry(new SymTabEntry(cur.getTableEntry()));
        } else if(node.checkContent("memberDecl")){
            // Get visibility
            SyntaxTreeNode cur = node.getChild();
            boolean visibility = cur.getTableEntry().isPublic();

            // get member declare
            cur = cur.getRightSib();
            node.setTableEntry(new SymTabEntry(cur.getTableEntry()));
            node.getTableEntry().setVisibility(visibility ? "public" : "private");
        } else if(node.checkContent("memFuncDecl")){
            SyntaxTreeNode cur = node.getChild();

            // Get ID
            String name = cur.getValue();

            // Get fParamsList
            cur = cur.getRightSib();
            SyntaxTreeNode fParams = cur.getChild();
            String paramTypes = "";
            SymbolTable table = new SymbolTable(name);
            while(fParams != null && !fParams.isEpsilon()){
                table.addEntry(fParams.getTableEntry());
                paramTypes += fParams.getTableEntry().getType() + ",";
                fParams = fParams.getRightSib();
            }

            // Get returnType
            cur = cur.getRightSib();
            String type = cur.getTableEntry().getType() + ":" + paramTypes;
            type = type.substring(0, type.length()-1); // remove redundant character

            node.setTableEntry(new SymTabEntry(name, "function", type, table));

        } else if(node.checkContent("memberVarDecl")){
            // Get ID
            SyntaxTreeNode cur = node.getChild();
            String name = cur.getValue();

            // Get type
            cur = cur.getRightSib();
            String type = cur.getTableEntry().getType();

            // Get array size list
            cur = cur.getRightSib().getChild();
            while(cur != null && !cur.isEpsilon()){
                if(cur.getValue() == null){
                    type += "[" + "]";
                } else {
                    type += "[" + cur.getValue() + "]";
                }
                cur = cur.getRightSib();
            }

            node.setTableEntry(new SymTabEntry(name, "variable", type));
        } else if(node.checkContent("prog")){
            if(!mainDeclared){
                OutputWriter.semanticErrWriting("ERROR: Main function was not declared!");
            }
        } else if(node.checkContent("returnType")){
            SyntaxTreeNode cur = node.getChild();
            String type = null;
            if(cur.checkContent("type")){
                type = cur.getTableEntry().getType();
            } else {
                type = "void";
            }

            node.setTableEntry(new SymTabEntry(null, "returnType", type));
        } else if(node.checkContent("type")){
            SyntaxTreeNode cur = node.getChild();
            String type = null;
            if(cur.checkContent("id")){
                type = cur.getValue();

                // Check if this class has been declared prior to this
                if(this.table.containsName(type) == null){
                    OutputWriter.semanticErrWriting("ERROR: Class " + type + " has not been declared before, line " + node.getLineCount());
                }
            } else {
                type = cur.toString();
            }

            node.setTableEntry(new SymTabEntry(null, "type", type));
        } else if(node.checkContent("visibility")){
            SyntaxTreeNode cur = node.getChild();
            node.setTableEntry(new SymTabEntry(null, "visibility", null));
            node.getTableEntry().setVisibility(cur.toString());
        }

    }

}
