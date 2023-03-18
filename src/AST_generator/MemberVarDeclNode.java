package AST_generator;

import table_generator.Visitor;

public class MemberVarDeclNode extends SyntaxTreeNode{
    public MemberVarDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
