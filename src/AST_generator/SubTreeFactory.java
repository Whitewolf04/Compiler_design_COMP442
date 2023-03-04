package AST_generator;

public class SubTreeFactory extends Factory{
    String content;
    int numPop;
    String nodeToPop;
    String nodeToPop2;

    public SubTreeFactory(String name, int numPop){
        content = name;
        this.numPop = numPop;
        nodeToPop = null;
        nodeToPop2 = null;
    }

    public SubTreeFactory(String name, String nodeToPop){
        content = name;
        numPop = -1;
        this.nodeToPop = nodeToPop;
        nodeToPop2 = null;
    }

    public SubTreeFactory(String name, String nodeToPop, String secondNodeToPop){
        content = name;
        numPop = -1;
        this.nodeToPop = nodeToPop;
        this.nodeToPop2 = secondNodeToPop;
    }

    public void make(){
        SyntaxTreeNode newNode = new SyntaxTreeNode(content);
        if(numPop > -1 && nodeToPop == null && nodeToPop2 == null){
            newNode.setChild(stackTraverseWithNumPop(newNode, null, numPop));
        } else if(numPop == -1 && nodeToPop != null && nodeToPop2 == null) {
            newNode.setChild(stackTraverseWithNode(newNode, null, nodeToPop));
        } else if(numPop == -1 && nodeToPop != null && nodeToPop2 != null) {
            newNode.setChild(stackTraverseWithTwoNode(newNode, null, nodeToPop, nodeToPop2));
        } else {
            System.out.println("SubTreeFactory error! Cannot make!");
        }
        Factory.nodeStack.push(newNode);
    }

    private SyntaxTreeNode stackTraverseWithNumPop(SyntaxTreeNode parent, SyntaxTreeNode rightSib, int count){
        SyntaxTreeNode currentNode = Factory.nodeStack.pop();
        currentNode.setParent(parent);
        currentNode.setRightSib(rightSib);

        if(count > 1){
            SyntaxTreeNode leftmostSib = stackTraverseWithNumPop(parent, currentNode, --count);
            currentNode.setLeftmostSib(leftmostSib);
            return leftmostSib;
        } else {
            return currentNode;
        }
    }

    private SyntaxTreeNode stackTraverseWithNode(SyntaxTreeNode parent, SyntaxTreeNode rightSib, String nodeToPop){
        if(Factory.nodeStack.empty()){
            System.out.println("Node stack empty! Cannot find the ending node to make subtree!");
            System.exit(1);
        }

        SyntaxTreeNode currentNode = Factory.nodeStack.pop();
        currentNode.setParent(parent);
        currentNode.setRightSib(rightSib);

        if(!currentNode.toString().equals(nodeToPop)){
            SyntaxTreeNode leftmostSib = stackTraverseWithNode(parent, currentNode, nodeToPop);
            currentNode.setLeftmostSib(leftmostSib);
            return leftmostSib;
        } else {
            return currentNode;
        }
    }

    private SyntaxTreeNode stackTraverseWithTwoNode(SyntaxTreeNode parent, SyntaxTreeNode rightSib, String nodeToPop, String nodeToPop2){
        if(Factory.nodeStack.empty()){
            System.out.println("Node stack empty! Cannot find the ending node to make subtree!");
            System.exit(1);
        }

        SyntaxTreeNode currentNode = Factory.nodeStack.pop();
        currentNode.setParent(parent);
        currentNode.setRightSib(rightSib);

        if(!currentNode.toString().equals(nodeToPop) && !currentNode.toString().equals(nodeToPop2)){
            SyntaxTreeNode leftmostSib = stackTraverseWithTwoNode(parent, currentNode, nodeToPop, nodeToPop2);
            currentNode.setLeftmostSib(leftmostSib);
            return leftmostSib;
        } else {
            return currentNode;
        }
    }

    public String toString(){
        return "SubTreeFactory:"+content;
    }

    public boolean compareToString(String another){
        return true;
    }
}
