package AST_generator;

import table_generator.Visitor;

public class Expr2Node extends SyntaxTreeNode{
    public Expr2Node(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
