package AST_generator;

import table_generator.Visitor;

public class ArrayOrObjectNode extends SyntaxTreeNode{
    public ArrayOrObjectNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
