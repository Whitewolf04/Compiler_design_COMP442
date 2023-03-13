package AST_generator;

public class VariableNode extends SyntaxTreeNode{
    public VariableNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
