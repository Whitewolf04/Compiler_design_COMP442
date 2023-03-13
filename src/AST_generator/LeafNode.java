package AST_generator;

public class LeafNode extends SyntaxTreeNode{
    public LeafNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
