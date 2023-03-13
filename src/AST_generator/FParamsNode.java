package AST_generator;

public class FParamsNode extends SyntaxTreeNode{
    public FParamsNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
