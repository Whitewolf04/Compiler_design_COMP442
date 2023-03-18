package AST_generator;

import table_generator.Visitor;

public class IndiceNode extends SyntaxTreeNode{
    public IndiceNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
