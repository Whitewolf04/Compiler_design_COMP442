package AST_generator;

public class StatementListNode extends SyntaxTreeNode{
    public StatementListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
