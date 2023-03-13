package AST_generator;

public class FuncBodyNode extends SyntaxTreeNode{
    public FuncBodyNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
