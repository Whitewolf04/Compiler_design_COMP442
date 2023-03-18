package AST_generator;

import table_generator.Visitor;

public class StatementListNode extends SyntaxTreeNode{
    public StatementListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
