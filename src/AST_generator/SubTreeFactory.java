package AST_generator;

import java.util.ListIterator;

public class SubTreeFactory extends Factory{
    String content;
    int numPop;
    String nodeToPop = null;
    String nodeToPop2 = null;
    String startingNode = null;
    boolean exclude = false;

    public SubTreeFactory(String name, int numPop){
        content = name;
        this.numPop = numPop;
    }

    public SubTreeFactory(String name, String start, String nodeToPop){
        content = name;
        numPop = -1;
        startingNode = start;
        this.nodeToPop = nodeToPop;
    }

    public SubTreeFactory(String name, String start, boolean exclude, String nodeToPop, String nodeToPop2){
        content = name;
        numPop = -1;
        startingNode = start;
        this.exclude = exclude;
        this.nodeToPop = nodeToPop;
        this.nodeToPop2 = nodeToPop2;
    }

    public void make(String value){
        SyntaxTreeNode newNode = new SyntaxTreeNode(content);
        if(numPop > -1 && startingNode == null){
            newNode.setChild(stackTraverseWithNumPop(newNode, null, numPop));
        } else if(numPop == -1 && startingNode != null && nodeToPop != null && nodeToPop2 == null) {
            stackTraverseWithNode(newNode);
        } else if(numPop == -1 && startingNode != null && nodeToPop != null && nodeToPop2 != null && exclude) {
            stackTraverseExcludeStart(newNode);
        } else {
            System.out.println("SubTreeFactory error! Cannot make!");
        }
        Factory.nodeStack.add(newNode);
    }

    private SyntaxTreeNode stackTraverseWithNumPop(SyntaxTreeNode parent, SyntaxTreeNode rightSib, int count){
        SyntaxTreeNode currentNode = Factory.nodeStack.pollLast();
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

    private void stackTraverseWithNode(SyntaxTreeNode parent){
        if(Factory.nodeStack.peek() == null){
            System.out.println("Node stack empty! Cannot find the ending node to make subtree!");
            System.exit(1);
        }

        SyntaxTreeNode leftmost = null;
        SyntaxTreeNode right = null;
        SyntaxTreeNode currentNode = null;
        ListIterator<SyntaxTreeNode> i = Factory.nodeStack.listIterator();

        // Iterate until the starting point
        while(i.hasNext()){
            currentNode = i.next();
            if(currentNode.toString().equals(startingNode)){
                break;
            } else if(currentNode.toString().equals(nodeToPop)){
                parent.setChild(currentNode);
                i.remove();
                return;
            }
        }

        // Check if current node is starting point
        if(!currentNode.toString().equals(startingNode)){
            System.out.println("Starting point could not be found! Cannot make subtree:"+content+"!");
            System.exit(1);
        }

        parent.setChild(currentNode);
        leftmost = currentNode;
        currentNode.setParent(parent);
        i.remove();

        // Iterate from the starting point to ending node
        while(i.hasNext()){
            right = i.next();
            currentNode.setRightSib(right);
            right.setParent(parent);
            right.setLeftmostSib(leftmost);
            currentNode = right;
            i.remove();
            if(currentNode.toString().equals(nodeToPop)){
                currentNode.setRightSib(null);
                if(i.hasNext()){
                    System.out.println("Warning: nodeToPop is not at the top of the stack!");
                    break;
                }
            }
        }
    }

    private void stackTraverseExcludeStart(SyntaxTreeNode parent){
        if(Factory.nodeStack.peek() == null){
            System.out.println("Node stack empty! Cannot find the ending node to make subtree!");
            System.exit(1);
        }

        SyntaxTreeNode leftmost = null;
        SyntaxTreeNode right = null;
        SyntaxTreeNode currentNode = null;
        ListIterator<SyntaxTreeNode> i = Factory.nodeStack.listIterator();

        // Iterate until the starting point
        while(i.hasNext()){
            currentNode = i.next();
            if(currentNode.toString().equals(startingNode)){
                break;
            } else if(currentNode.toString().equals(nodeToPop) || currentNode.toString().equals(nodeToPop2)){
                parent.setChild(currentNode);
                return;
            }
        }

        // Check if current node is starting point
        if(!currentNode.toString().equals(startingNode)){
            System.out.println("Starting point could not be found! Cannot make subtree:"+content+"!");
            System.exit(1);
        }

        currentNode = i.next();
        parent.setChild(currentNode);
        leftmost = currentNode;
        currentNode.setParent(parent);
        i.remove();

        // Iterate from the starting point to ending node
        while(i.hasNext()){
            right = i.next();
            currentNode.setRightSib(right);
            right.setParent(parent);
            right.setLeftmostSib(leftmost);
            currentNode = right;
            i.remove();
            if(currentNode.toString().equals(nodeToPop) || currentNode.toString().equals(nodeToPop2)){
                currentNode.setRightSib(null);
                if(i.hasNext()){
                    System.out.println("Warning: nodeToPop is not at the top of the stack!");
                    break;
                }
            }
        }
    }

    // private SyntaxTreeNode stackTraverseWithTwoNode(SyntaxTreeNode parent, SyntaxTreeNode rightSib, String nodeToPop, String nodeToPop2){
    //     if(Factory.nodeStack.peek() == null){
    //         System.out.println("Node stack empty! Cannot find the ending node to make subtree!");
    //         System.exit(1);
    //     }

    //     SyntaxTreeNode currentNode = Factory.nodeStack.pop();
    //     currentNode.setParent(parent);
    //     currentNode.setRightSib(rightSib);

    //     if(!currentNode.toString().equals(nodeToPop) && !currentNode.toString().equals(nodeToPop2)){
    //         SyntaxTreeNode leftmostSib = stackTraverseWithTwoNode(parent, currentNode, nodeToPop, nodeToPop2);
    //         currentNode.setLeftmostSib(leftmostSib);
    //         return leftmostSib;
    //     } else {
    //         return currentNode;
    //     }
    // }

    public String toString(){
        return "SubTreeFactory:"+content;
    }

    public boolean compareToString(String another){
        return true;
    }
}
