package AST_generator;

public class ArrayOrObjectNode extends SyntaxTreeNode{
    public ArrayOrObjectNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
