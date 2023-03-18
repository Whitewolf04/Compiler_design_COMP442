package AST_generator;

import table_generator.Visitor;

public class IfStatNode extends SyntaxTreeNode{
    public IfStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
