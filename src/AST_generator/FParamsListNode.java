package AST_generator;

import table_generator.Visitor;

public class FParamsListNode extends SyntaxTreeNode{
    public FParamsListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
