package AST_generator;

import table_generator.Visitor;

public class FuncHeadNode extends SyntaxTreeNode{
    public FuncHeadNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
