package AST_generator;

public class RelExprNode extends SyntaxTreeNode{
    public RelExprNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
