package AST_generator;

public class MemberFuncDeclNode extends SyntaxTreeNode{
    public MemberFuncDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
