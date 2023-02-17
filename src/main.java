import lexical_analyzer.LexAnalyzer;
import syntax_analyzer.ProgramQueue;

public class main {
    public static void main(String[] args) {
        ProgramQueue.initProgramQueue();
        LexAnalyzer.analyze("example-bubblesort.src");

        System.out.println();
        System.out.println(ProgramQueue.printQueue());
    }
}
