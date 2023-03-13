package AST_generator;

public class ReturnTypeNode extends SyntaxTreeNode{
    public ReturnTypeNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
