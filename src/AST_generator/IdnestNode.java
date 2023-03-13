package AST_generator;

public class IdnestNode extends SyntaxTreeNode{
    public IdnestNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
