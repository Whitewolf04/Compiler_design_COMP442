package AST_generator;

public class StatementNode extends SyntaxTreeNode{
    public StatementNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
