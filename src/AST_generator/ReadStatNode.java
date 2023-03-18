package AST_generator;

import table_generator.Visitor;

public class ReadStatNode extends SyntaxTreeNode{
    public ReadStatNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
