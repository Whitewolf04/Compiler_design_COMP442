import AST_generator.Factory;
import lexical_analyzer.LexAnalyzer;
import lexical_analyzer.OutputWriter;
import syntax_analyzer.ProgramQueue;
import syntax_analyzer.SyntaxAnalyzer;
import table_generator.Generator;

public class main {
    public static void main(String[] args) {
        ProgramQueue.initProgramQueue();
        LexAnalyzer.analyze("testIdnest.src");

        SyntaxAnalyzer.analyze();
        OutputWriter.openTreeWriteStream();
        Factory.treeToGraph();
        OutputWriter.closeTreeWriteStream();

        OutputWriter.openSemanticErrWriting();
        OutputWriter.openSemanticOutWriting();
        Generator.visitTree();
        OutputWriter.closeSemanticOutStream();
        OutputWriter.closeSemanticOutStream();
    }
}
