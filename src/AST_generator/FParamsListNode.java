package AST_generator;

public class FParamsListNode extends SyntaxTreeNode{
    public FParamsListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
