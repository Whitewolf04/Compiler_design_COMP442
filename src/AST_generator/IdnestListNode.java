package AST_generator;

public class IdnestListNode extends SyntaxTreeNode{
    public IdnestListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
