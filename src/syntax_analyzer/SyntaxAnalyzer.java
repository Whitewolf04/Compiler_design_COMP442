package syntax_analyzer;

import java.util.Stack;

import lexical_analyzer.OutputWriter;

public class SyntaxAnalyzer {
    public static void analyze(){
        ParsingTable.loadTable();
        System.out.println("Table loaded!");

        OutputWriter.openSyntaxWriteStream();

        GrammarStack.initStack();
        GrammarStack.push(Terminal.START);
        GrammarStack.push(ParsingTable.get("START"));
        
        Terminal token = ProgramQueue.nextToken();
        GrammarToken stackTop = GrammarStack.peek();
        boolean error = false;
        Stack<GrammarToken> temp = null;
        Stack<GrammarToken> lookup = null;

        while(token != null){
            // Check if top of the stack is a terminal character
            stackTop = GrammarStack.peek();
            if(stackTop.getClass() == token.getClass()){
                if(stackTop.equals(Terminal.EPSILON)){
                    GrammarStack.pop();
                } else if(stackTop.equals(token)){
                    GrammarStack.pop();
                    token = ProgramQueue.nextToken();
                } else{
                    skipErrors(token, stackTop);
                    error = true;
                }
            } else{
                lookup = ParsingTable.get(stackTop.toString()).tableEntry.get(token.toString());
                if(lookup != null){
                    GrammarStack.pop();

                    // Check before typecast
                    if(lookup instanceof java.util.Stack){
                        temp = (Stack<GrammarToken>) lookup.clone();
                    }

                    // Push everything onto the the grammar stack
                    while(!temp.empty()){
                      GrammarStack.push(temp.pop());  
                    }

                } else {
                    skipErrors(token, stackTop);
                    error = true;
                }
            }

            System.out.println(GrammarStack.printStack());
            OutputWriter.syntaxOutWriting();
        }

        if(!stackTop.compareToString("$") || error){
            System.out.println("Program is syntactically invalid!");
        } else{
            System.out.println("Program is syntactically correct!");
        }

        OutputWriter.closeSyntaxWriteStream();
    }

    public static void skipErrors(Terminal token, GrammarToken stackTop){
        String error = "Syntax error at line " + ProgramQueue.getLineCount() + ", character " + token.toString() + " with " + stackTop.toString() + " at the top of the stack";
        System.out.println(error);
        OutputWriter.syntaxErrWriting(error);
        NonTerminal temp = null;
        while(stackTop.getClass() == token.getClass()){
            GrammarStack.pop();
            stackTop = GrammarStack.peek();
        }

        // Downcast to NonTerminal class for easier manipulation
        // if(stackTop instanceof NonTerminal){
        temp = (NonTerminal) stackTop;
        // }

        if(token.equals(Terminal.START) || temp.inFollow(token)){
            GrammarStack.pop();
        } else {
            while(!temp.inFirst(token) || (temp.inFirst(Terminal.EPSILON) && !temp.inFollow(token))){
                token = ProgramQueue.nextToken();
                if(token == null){
                    System.out.println("End of program!");
                    OutputWriter.syntaxErrWriting("End of program!");
                    OutputWriter.closeSyntaxWriteStream();
                    System.exit(1);
                }
            }
        }
    }
}
