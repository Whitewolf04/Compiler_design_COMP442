package AST_generator;

public class MemberListNode extends SyntaxTreeNode{
    public MemberListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
