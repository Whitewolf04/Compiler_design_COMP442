package AST_generator;

import table_generator.Visitor;

public class LocalVarOrStatNode extends SyntaxTreeNode{
    public LocalVarOrStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
