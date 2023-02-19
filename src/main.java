import lexical_analyzer.LexAnalyzer;
import syntax_analyzer.ProgramQueue;
import syntax_analyzer.SyntaxAnalyzer;

public class main {
    public static void main(String[] args) {
        ProgramQueue.initProgramQueue();
        LexAnalyzer.analyze("example-bubblesort.src");

        SyntaxAnalyzer.analyze();
    }
}
