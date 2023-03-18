package AST_generator;

import table_generator.Visitor;

public class LeafNode extends SyntaxTreeNode{
    public LeafNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
