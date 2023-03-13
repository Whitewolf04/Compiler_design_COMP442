package AST_generator;

public class ExprListNode extends SyntaxTreeNode{
    public ExprListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
