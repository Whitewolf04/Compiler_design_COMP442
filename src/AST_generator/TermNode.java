package AST_generator;

public class TermNode extends SyntaxTreeNode{
    public TermNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
