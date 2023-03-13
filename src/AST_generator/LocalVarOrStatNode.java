package AST_generator;

public class LocalVarOrStatNode extends SyntaxTreeNode{
    public LocalVarOrStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
