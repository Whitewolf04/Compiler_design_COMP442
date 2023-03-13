package AST_generator;

public class FactorNode extends SyntaxTreeNode{
    public FactorNode(String content){
        super(content);
    }

    public void accept(Visitor visitor){}
}
