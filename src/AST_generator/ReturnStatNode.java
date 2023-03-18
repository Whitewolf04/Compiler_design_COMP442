package AST_generator;

import table_generator.Visitor;

public class ReturnStatNode extends SyntaxTreeNode{
    public ReturnStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
