package AST_generator;

import table_generator.Visitor;

public class MemberDeclNode extends SyntaxTreeNode{
    public MemberDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
