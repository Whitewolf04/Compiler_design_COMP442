package AST_generator;

import table_generator.Visitor;

public class FuncBodyNode extends SyntaxTreeNode{
    public FuncBodyNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
