package AST_generator;

import table_generator.SymTabEntry;
import table_generator.Visitor;

public class SyntaxTreeNode {
    private SyntaxTreeNode parent;
    private SyntaxTreeNode leftmostSib;
    private SyntaxTreeNode rightSib;
    private SyntaxTreeNode child;
    private String type;
    private String content;
    private String value = null;
    private String offset = "99999999";
    private int id;
    private static int counter = 0;
    private SymTabEntry tableEntry = null;

    public SyntaxTreeNode(String content){
        parent = null;
        leftmostSib = null;
        rightSib = null;
        child = null;
        type = "void";
        this.content = content;
        id = counter++;
    }

    public SyntaxTreeNode(SyntaxTreeNode parent, String content){
        this.parent = parent;
        leftmostSib = null;
        rightSib = null;
        child = null;
        type = "void";
        this.content = content;
        id = counter++;
    }

    public SyntaxTreeNode(SyntaxTreeNode parent, SyntaxTreeNode leftmostSib, String content){
        this.parent = parent;
        this.leftmostSib = leftmostSib;
        rightSib = null;
        child = null;
        type = "void";
        this.content = content;
        id = counter++;
    }

    public void setParent(SyntaxTreeNode parent){
        this.parent = parent;
    }

    public void setLeftmostSib(SyntaxTreeNode leftmostSib){
        this.leftmostSib = leftmostSib;
    }

    public void setRightSib(SyntaxTreeNode rightsib){
        rightSib = rightsib;
    }
    
    public void setChild(SyntaxTreeNode childNode){
        child = childNode;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setValue(String s){
        value = s;
    }

    public void setOffset(String offset){
        this.offset = offset;
    }

    public void setTableEntry(SymTabEntry entry){
        this.tableEntry = entry;
    }

    public SyntaxTreeNode getParent(){
        return parent;
    }

    public SyntaxTreeNode getLeftmostSib(){
        return leftmostSib;
    }

    public SyntaxTreeNode getRightSib(){
        return rightSib;
    }

    public String getType(){
        return this.type;
    }

    public SyntaxTreeNode getChild(){
        return child;
    }

    public SymTabEntry getTableEntry(){
        return this.tableEntry;
    }

    public String toString(){
        return this.content;
    }

    public String getValue(){
        return value;
    }

    public String getOffset(){
        return this.offset;
    }

    public String toTree(){
        if(value != null){
            return content+id+"_"+value;
        } else{
            return content+id;
        }
    }

    public void accept(Visitor visitor){}

    public boolean isEpsilon(){
        if(this.content.compareTo("EPSILON") == 0){
            return true;
        }
        return false;
    }

    public int getChildNum(){
        if(this.child == null){
            return 0;
        }

        SyntaxTreeNode cur = this.child;
        int count = 1;
        while(cur.rightSib != null){
            count++;
            cur = cur.rightSib;
        }
        return count;
    }

    public boolean checkContent(String str){
        if(this.content.compareTo(str) == 0){
            return true;
        }
        return false;
    }
}
