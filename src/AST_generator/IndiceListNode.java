package AST_generator;

public class IndiceListNode extends SyntaxTreeNode{
    public IndiceListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
