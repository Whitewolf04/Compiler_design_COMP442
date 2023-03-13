package AST_generator;

public class MemberVarDeclNode extends SyntaxTreeNode{
    public MemberVarDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
