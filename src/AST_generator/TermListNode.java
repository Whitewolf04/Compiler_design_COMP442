package AST_generator;

import table_generator.Visitor;

public class TermListNode extends SyntaxTreeNode{
    public TermListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
