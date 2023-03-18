package AST_generator;

import table_generator.Visitor;

public class FParamsNode extends SyntaxTreeNode{
    public FParamsNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
