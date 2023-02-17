package syntax_analyzer;

import java.util.Stack;

public final class GrammarStack {
    private static Stack<GrammarToken> stack;

    private GrammarStack() {
        stack = null;
    }

    public static void initStack(){
        stack = new Stack<GrammarToken>();
    }

    public static void push(GrammarToken token){
        stack.push(token);
    }

    public static GrammarToken pop(){
        return stack.pop();
    }

}
