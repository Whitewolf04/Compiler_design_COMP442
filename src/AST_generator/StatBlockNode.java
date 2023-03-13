package AST_generator;

public class StatBlockNode extends SyntaxTreeNode{
    public StatBlockNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
