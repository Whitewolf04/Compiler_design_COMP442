package AST_generator;

public class WhileStatNode extends SyntaxTreeNode{
    public WhileStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
