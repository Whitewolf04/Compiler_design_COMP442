package AST_generator;

public class TermListNode extends SyntaxTreeNode{
    public TermListNode(String content){
        super(content);
    }

    @Override
    public void accept(Visitor visitor){}
}
