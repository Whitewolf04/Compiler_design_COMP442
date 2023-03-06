package AST_generator;


public class SyntaxTreeNode {
    private SyntaxTreeNode parent;
    private SyntaxTreeNode leftmostSib;
    private SyntaxTreeNode rightSib;
    private SyntaxTreeNode child;
    private String type;
    private String content;
    private String value = null;
    private int id;
    private static int counter = 0;

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

    public String toString(){
        return this.content;
    }

    public String toTree(){
        if(value != null){
            return content+id+"_"+value;
        } else{
            return content+id;
        }
    }
}
