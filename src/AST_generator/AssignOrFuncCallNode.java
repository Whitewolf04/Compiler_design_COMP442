package AST_generator;

public class AssignOrFuncCallNode extends SyntaxTreeNode{
    public AssignOrFuncCallNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
