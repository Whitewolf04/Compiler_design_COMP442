package AST_generator;

import table_generator.Visitor;

public class StatementNode extends SyntaxTreeNode{
    public StatementNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
