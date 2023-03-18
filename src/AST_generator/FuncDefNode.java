package AST_generator;

import table_generator.Visitor;

public class FuncDefNode extends SyntaxTreeNode{
    public FuncDefNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
