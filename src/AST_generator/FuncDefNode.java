package AST_generator;

public class FuncDefNode extends SyntaxTreeNode{
    public FuncDefNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
