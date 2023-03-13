package AST_generator;

public class VarIdnestNode extends SyntaxTreeNode{
    public VarIdnestNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
