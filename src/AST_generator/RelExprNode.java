package AST_generator;

import table_generator.Visitor;

public class RelExprNode extends SyntaxTreeNode{
    public RelExprNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
