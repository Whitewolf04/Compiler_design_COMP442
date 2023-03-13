package AST_generator;

public class FuncHeadNode extends SyntaxTreeNode{
    public FuncHeadNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
