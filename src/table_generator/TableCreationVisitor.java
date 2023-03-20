package table_generator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_generator.SyntaxTreeNode;

public class TableCreationVisitor extends Visitor{
    SymbolTable table;

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

            // Get inherit list
            cur = cur.getRightSib();
            SyntaxTreeNode parent = cur.getChild().getRightSib(); // skip through isa operator
            String type = name + ":";
            while(parent != null && !parent.isEpsilon()){
                // Check if this class has been declared
                SymTabEntry parentEntry = this.table.accessFromGlobal(parent.getValue());
                if(parentEntry == null){
                    System.out.println("ERROR: Class " + parent.getValue() + " has not been declared and cannot be inherited!");
                    parent = parent.getRightSib();
                    continue;
                }

                // Add to inheritance list 
                type += parent.getValue() + ",";
                SymbolTable parentTable = parentEntry.getLink();
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
                    SymTabEntry duplicate = classTable.contains(cur.getTableEntry().getName(), cur.getTableEntry().getType());
                    if(duplicate != null){
                        duplicate.setLink(cur.getTableEntry().getLink());
                        System.out.println("WARNING: Override for function " + cur.getTableEntry().getName() + " in class " + name);
                    } else {
                        classTable.addEntry(cur.getTableEntry());
                    }
                } else if(cur.getTableEntry().getKind().compareTo("variable")==0){
                    SymTabEntry duplicate = classTable.accessFromGlobal(cur.getTableEntry().getName());
                    if(duplicate != null){
                        duplicate.setType(cur.getTableEntry().getType());
                        System.out.println("WARNING: Override for attribute " + cur.getTableEntry().getName() + " in class " + name);
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
            SyntaxTreeNode funcHead = node.getChild();
            Pattern pattern = Pattern.compile("\\A(.*?)::(.*?)\\Z");
            Matcher matcher = pattern.matcher(funcHead.getTableEntry().getName());
            String funcName = null;
            if(matcher.find()){
                // Member function definition
                String ownerName = matcher.group(1);
                funcName = matcher.group(2);
                SymTabEntry ownerEntry = this.table.accessFromGlobal(ownerName);
                if(ownerEntry == null){
                    System.out.println("ERROR: Class " + ownerName + " has not been declared, so its function " + funcName + " cannot be defined!");
                    return;
                }
                SymbolTable ownerTable = ownerEntry.getLink();
                SymTabEntry funcEntry = ownerTable.contains(funcName, funcHead.getTableEntry().getType());
                if(funcEntry == null){
                    System.out.println("ERROR: Function " + funcName + " has not been declared in class " + ownerName + ", so it cannot be defined!");
                    return;
                }
                table = funcEntry.getLink();
                table.name = funcHead.getTableEntry().getName();
            } else {
                // Global function definition
                funcName = funcHead.getTableEntry().getName();
                table = new SymbolTable(funcName);
                SymbolTable paramList = funcHead.getTableEntry().getLink();
                paramList.pushToTable(table);
                funcHead.getTableEntry().setLink(null);
                node.setTableEntry(new SymTabEntry(funcName, "function", funcHead.getTableEntry().getType(), table));
                this.table.addEntry(node.getTableEntry());
            }
            
            // Traverse funcBody node
            SyntaxTreeNode cur = funcHead.getRightSib().getChild();
            while(cur != null && !cur.isEpsilon()){
                // Skip through statement
                if(cur.getTableEntry() == null){
                    cur = cur.getRightSib();
                    continue;
                }
                table.addEntry(cur.getTableEntry());
                cur = cur.getRightSib();
            }
        } else if(node.checkContent("funcHead")){
            // Get ID and attach to class if this is member fund def
            SyntaxTreeNode cur = node.getChild();
            String name = cur.getValue();
            SymbolTable table = null;
            if(node.getChildNum() == 3){
                // Global function
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
                table.addEntry(cur.getTableEntry());
                paramTypes += fParams.getTableEntry().getType() + ",";
                fParams = fParams.getRightSib();
            }

            // Swith to returnType
            String type = null;
            if(node.getChildNum() == 4){
                type = "void:" + paramTypes;
                type = type.substring(0, type.length()-1);
            } else {
                cur = cur.getRightSib();
                type = cur.getTableEntry().getType() + ":" + paramTypes;
                type = type.substring(0, type.length()-1);
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
                SymTabEntry ownerEntry = this.table.accessFromGlobal(cur.getChild().getValue());
                // Error handling
                if(ownerEntry == null){
                    System.out.println("ERROR: This class/function hasn't been declared, so variable " + name + " cannot be created!");
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
                        System.out.println("ERROR: " + type + " is not an object!");
                        node.setTableEntry(new SymTabEntry(name, "variable", "ERR@!"));
                        return;
                    }
                }
            } else {
                type = cur.getChild().toString();
                // Get arraySizeList if available
                cur = cur.getRightSib().getChild();
                if(!cur.checkContent("arraySizeList")){
                    System.out.println("ERROR: " + type + " is not an object!");
                    node.setTableEntry(new SymTabEntry(name, "variable", "ERR@!"));
                    return;
                } else {
                    cur = cur.getChild();
                    while(cur != null && !cur.isEpsilon()){
                        if(cur.getValue() == null){
                            type += "[" + "]";
                        } else {
                            type += "[" + cur.getValue() + "]";
                        }
                    }
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
            boolean visibility = cur.getTableEntry().getVisibility();

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
