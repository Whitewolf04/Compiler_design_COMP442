package AST_generator;

import table_generator.Visitor;

public class ArraySizeListNode extends SyntaxTreeNode {
    public ArraySizeListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
