package AST_generator;

public class Expr2Node extends SyntaxTreeNode{
    public Expr2Node(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
