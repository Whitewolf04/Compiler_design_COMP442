package AST_generator;

import table_generator.Visitor;

public class IdnestListNode extends SyntaxTreeNode{
    public IdnestListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
