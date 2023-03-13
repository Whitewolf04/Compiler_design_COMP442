package AST_generator;

public class ExprNode extends SyntaxTreeNode{
    public ExprNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
