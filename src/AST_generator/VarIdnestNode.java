package AST_generator;

import table_generator.Visitor;

public class VarIdnestNode extends SyntaxTreeNode{
    public VarIdnestNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
