package AST_generator;

public class IndiceNode extends SyntaxTreeNode{
    public IndiceNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
