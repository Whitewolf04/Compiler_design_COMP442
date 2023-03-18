package AST_generator;

import table_generator.Visitor;

public class AssignOrFuncCallNode extends SyntaxTreeNode{
    public AssignOrFuncCallNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
