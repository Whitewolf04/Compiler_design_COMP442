package AST_generator;

import java.util.Iterator;
import java.util.LinkedList;

import lexical_analyzer.OutputWriter;
import syntax_analyzer.GrammarToken;

public abstract class Factory extends GrammarToken{
    public static LinkedList<SyntaxTreeNode> nodeStack = new LinkedList<SyntaxTreeNode>();

    abstract public void make(String value);

    static public String printNodeStack(){
        Iterator<SyntaxTreeNode> i = nodeStack.descendingIterator();
        String output = "";

        while(i.hasNext()){
            output += i.next().toString() + " ";
        }
        
        return output;
    }

    static public void treeToGraph(){
        OutputWriter.treeWriting("graph G {\n");

        SyntaxTreeNode current = nodeStack.peek();
        treeTraversal(current);
        OutputWriter.treeWriting("}");

    }

    static private void treeTraversal(SyntaxTreeNode current){
        if(current.getChild() != null){
            OutputWriter.treeWriting("\t"+current.toTree() + " -- " + current.getChild().toTree() + ";\n");
            treeTraversal(current.getChild());
        }
        if(current.getRightSib() != null){
            OutputWriter.treeWriting("{rank=same;"+current.toTree()+";"+current.getRightSib().toTree()+";}");
            OutputWriter.treeWriting("\t"+current.getRightSib().toTree() + " -- " + current.getParent().toTree() + ";\n");
            treeTraversal(current.getRightSib());
        }
    }
}
