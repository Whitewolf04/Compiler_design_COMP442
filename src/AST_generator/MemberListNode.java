package AST_generator;

import table_generator.Visitor;

public class MemberListNode extends SyntaxTreeNode{
    public MemberListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
