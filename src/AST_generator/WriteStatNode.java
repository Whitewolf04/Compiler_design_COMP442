package AST_generator;

import table_generator.Visitor;

public class WriteStatNode extends SyntaxTreeNode{
    public WriteStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
