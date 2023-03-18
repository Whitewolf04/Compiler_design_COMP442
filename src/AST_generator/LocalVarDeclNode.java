package AST_generator;

import table_generator.Visitor;

public class LocalVarDeclNode extends SyntaxTreeNode{
    public LocalVarDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
