package AST_generator;

public class IfStatNode extends SyntaxTreeNode{
    public IfStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
