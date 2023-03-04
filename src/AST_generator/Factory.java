package AST_generator;

import java.util.Stack;

import syntax_analyzer.GrammarToken;

public abstract class Factory extends GrammarToken{
    static Stack<SyntaxTreeNode> nodeStack = new Stack<SyntaxTreeNode>();

    abstract public void make();

    static public String printNodeStack(){
        Stack<SyntaxTreeNode> temp = (Stack<SyntaxTreeNode>) nodeStack.clone();
        String output = "";

        while(!temp.empty()){
            output += temp.pop().toString() + " ";
        }
        
        return output;
    }
}
