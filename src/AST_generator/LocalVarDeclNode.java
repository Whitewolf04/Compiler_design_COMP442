package AST_generator;

public class LocalVarDeclNode extends SyntaxTreeNode{
    public LocalVarDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
