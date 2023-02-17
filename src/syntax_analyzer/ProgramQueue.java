package syntax_analyzer;

import java.util.Queue;
import java.util.LinkedList;

public class ProgramQueue {
    static Queue<Terminal> program;

    private ProgramQueue(){
        program = null;
    }

    public static void initProgramQueue(){
        program = new LinkedList<Terminal>();
    }

    public static boolean add(Terminal token){
        return program.offer(token);
    }

    public static Terminal nextToken(){
        program.poll();
        return program.peek();
    }

    public static String printQueue(){
        String output = "";

        for(Terminal t : program){
            output += t + "\n";
        }

        return output;
    }
}
