package syntax_analyzer;

import java.util.Stack;

public final class GrammarStack {
    private static Stack<GrammarToken> stack;
    private static Stack<String> outputStack;

    private GrammarStack() {
        stack = null;
        outputStack = null;
    }

    public static void initStack(){
        stack = new Stack<GrammarToken>();
        outputStack = new Stack<String>();
    }

    public static void push(GrammarToken token){
        stack.push(token);
        outputStack.push(token.toString());
    }

    public static GrammarToken pop(){
        outputStack.pop();
        return stack.pop();
    }

    public static GrammarToken peek(){
        return stack.peek();
    }

    public static void printStack(){
        Stack<String> temp = (Stack<String>) outputStack.clone();

        while(!temp.empty()){
            System.out.print(temp.pop() + " ");
        }
        System.out.println();
    }

}
