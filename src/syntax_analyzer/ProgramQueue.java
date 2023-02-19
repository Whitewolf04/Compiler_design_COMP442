package syntax_analyzer;

import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class ProgramQueue {
    private static Queue<Terminal> program;
    private static ArrayList<Integer> lineRecord;
    private static int charCount;

    private ProgramQueue(){
        program = null;
    }

    public static void initProgramQueue(){
        program = new LinkedList<Terminal>();
        lineRecord = new ArrayList<Integer>();
        charCount = 0;
        program.offer(Terminal.START);
    }

    public static boolean add(Terminal token){
        return program.offer(token);
    }

    public static Terminal nextToken(){
        program.poll();
        charCount++;
        return program.peek();
    }

    public static String printQueue(){
        String output = "";

        for(Terminal t : program){
            output += t + "\n";
        }

        return output;
    }

    public static void recordLine(){
        lineRecord.add(program.size());
    }

    public static int getLineCount(){
        int lineCount = 1;
        for(int i : lineRecord){
            if(i > charCount){
                break;
            } else{
                lineCount++;
            }
        }
        return lineCount;
    }
}
