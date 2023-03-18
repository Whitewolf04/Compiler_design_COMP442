package AST_generator;

import table_generator.Visitor;

public class WhileStatNode extends SyntaxTreeNode{
    public WhileStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
