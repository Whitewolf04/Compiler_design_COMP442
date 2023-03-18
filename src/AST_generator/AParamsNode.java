package AST_generator;

import table_generator.Visitor;

public class AParamsNode extends SyntaxTreeNode{
    public AParamsNode(String content){
        super(content);
    }
    
    @Override
    public void accept(Visitor visitor){}
}
