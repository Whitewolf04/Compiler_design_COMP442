package AST_generator;

import table_generator.Visitor;

public class VariableNode extends SyntaxTreeNode{
    public VariableNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
