package AST_generator;

public class ReadStatNode extends SyntaxTreeNode{
    public ReadStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
