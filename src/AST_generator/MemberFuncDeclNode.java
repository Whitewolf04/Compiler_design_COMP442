package AST_generator;

import table_generator.Visitor;

public class MemberFuncDeclNode extends SyntaxTreeNode{
    public MemberFuncDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
