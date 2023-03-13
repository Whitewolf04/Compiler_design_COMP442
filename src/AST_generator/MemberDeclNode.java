package AST_generator;


public class MemberDeclNode extends SyntaxTreeNode{
    public MemberDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
