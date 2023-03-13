package AST_generator;

public class ClassDeclNode extends SyntaxTreeNode{
    public ClassDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
