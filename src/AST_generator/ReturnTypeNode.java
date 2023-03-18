package AST_generator;

import table_generator.Visitor;

public class ReturnTypeNode extends SyntaxTreeNode{
    public ReturnTypeNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
