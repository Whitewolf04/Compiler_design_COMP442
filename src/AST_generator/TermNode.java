package AST_generator;

import table_generator.Visitor;

public class TermNode extends SyntaxTreeNode{
    public TermNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
