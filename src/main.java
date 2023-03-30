import AST_generator.Factory;
import code_generator.CodeGenerator;
import lexical_analyzer.LexAnalyzer;
import lexical_analyzer.OutputWriter;
import syntax_analyzer.ProgramQueue;
import syntax_analyzer.SyntaxAnalyzer;
import table_generator.SymbolTableGenerator;

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
        SymbolTableGenerator.visitTree();
        OutputWriter.closeSemanticOutStream();
        OutputWriter.closeSemanticOutStream();

        CodeGenerator.createTable();
    }
}
