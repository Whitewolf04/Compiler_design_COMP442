package AST_generator;

import table_generator.Visitor;

public class ClassDeclNode extends SyntaxTreeNode{
    public ClassDeclNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
