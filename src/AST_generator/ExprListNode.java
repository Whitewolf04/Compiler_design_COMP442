package AST_generator;

import table_generator.Visitor;

public class ExprListNode extends SyntaxTreeNode{
    public ExprListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
