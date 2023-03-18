package AST_generator;

import table_generator.Visitor;

public class FactorNode extends SyntaxTreeNode{
    public FactorNode(String content){
        super(content);
    }

    public void accept(Visitor visitor){}
}
