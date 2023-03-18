package AST_generator;

import table_generator.Visitor;

public class ExprNode extends SyntaxTreeNode{
    public ExprNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
