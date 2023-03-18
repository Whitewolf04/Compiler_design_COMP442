package AST_generator;

import table_generator.Visitor;

public class StatBlockNode extends SyntaxTreeNode{
    public StatBlockNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
