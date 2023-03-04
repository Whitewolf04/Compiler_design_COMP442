package AST_generator;

import org.jsoup.nodes.Document.OutputSettings.Syntax;

public class SyntaxTreeNode {
    private SyntaxTreeNode parent;
    private SyntaxTreeNode leftmostSib;
    private SyntaxTreeNode rightSib;
    private SyntaxTreeNode child;
    private String type;
    private String content;

    public SyntaxTreeNode(String content){
        parent = null;
        leftmostSib = null;
        rightSib = null;
        child = null;
        type = "void";
        this.content = content;
    }

    public SyntaxTreeNode(SyntaxTreeNode parent, String content){
        this.parent = parent;
        leftmostSib = null;
        rightSib = null;
        child = null;
        type = "void";
        this.content = content;
    }

    public SyntaxTreeNode(SyntaxTreeNode parent, SyntaxTreeNode leftmostSib, String content){
        this.parent = parent;
        this.leftmostSib = leftmostSib;
        rightSib = null;
        child = null;
        type = "void";
        this.content = content;
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

    public String toString(){
        return this.content;
    }
}
