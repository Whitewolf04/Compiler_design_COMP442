import java.util.Scanner;

import AST_generator.Factory;
import code_generator.CodeGenerator;
import lexical_analyzer.LexAnalyzer;
import lexical_analyzer.OutputWriter;
import syntax_analyzer.ProgramQueue;
import syntax_analyzer.SyntaxAnalyzer;
import table_generator.SymbolTableGenerator;

public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter input file name: ");
        String fileName = sc.next();
        System.out.print("\n\n");
        sc.close();

        ProgramQueue.initProgramQueue();
        LexAnalyzer.analyze(fileName);

        SyntaxAnalyzer.analyze();
        OutputWriter.openTreeWriteStream();
        Factory.treeToGraph();
        OutputWriter.closeTreeWriteStream();

        OutputWriter.openSemanticErrWriting();
        OutputWriter.openSemanticOutWriting();
        SymbolTableGenerator.visitTree();
        OutputWriter.closeSemanticOutStream();
        OutputWriter.closeSemanticOutStream();

        CodeGenerator.generate("out.m");
    }
}
