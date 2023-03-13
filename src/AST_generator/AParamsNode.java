package AST_generator;

public class AParamsNode extends SyntaxTreeNode{
    public AParamsNode(String content){
        super(content);
    }
    
    @Override
    public void accept(Visitor visitor){}
}
