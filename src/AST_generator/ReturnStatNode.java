package AST_generator;

public class ReturnStatNode extends SyntaxTreeNode{
    public ReturnStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
