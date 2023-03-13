package AST_generator;

public class ArraySizeListNode extends SyntaxTreeNode {
    public ArraySizeListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
