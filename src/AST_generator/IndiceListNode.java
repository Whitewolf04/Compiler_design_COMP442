package AST_generator;

import table_generator.Visitor;

public class IndiceListNode extends SyntaxTreeNode{
    public IndiceListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
