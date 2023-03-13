package AST_generator;

public class WriteStatNode extends SyntaxTreeNode{
    public WriteStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
