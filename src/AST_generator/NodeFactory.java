package AST_generator;


public class NodeFactory extends Factory{
    String content;
    String type;

    public NodeFactory(String content){
        this.content = content;
        type = null;
    }

    public NodeFactory(String content, String type){
        this.content = content;
        this.type = type;
    }

    public void make(){
        SyntaxTreeNode newNode = new SyntaxTreeNode(content);
        if(type != null){
            newNode.setType(type);
        }
        Factory.nodeStack.push(newNode);
    }

    public String toString(){
        return "NodeFactory:"+content;
    }

    public boolean compareToString(String anotherString){
        return true;
    }
}
