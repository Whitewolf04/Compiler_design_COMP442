package lexical_analyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import AST_generator.Factory;
import syntax_analyzer.GrammarStack;


public final class OutputWriter {
    private static BufferedWriter lexErrWriter;
    private static BufferedWriter lexOutWriter;
    private static BufferedWriter syntaxErrWriter;
    private static BufferedWriter syntaxOutWriter;
    private static BufferedWriter treeWriter;
    private static BufferedWriter semanticOutWriter;
    private static BufferedWriter semanticErrWriter;
    private static BufferedWriter codeDeclWriter;
    public static int lineCount;
    private static int nextLineCount;
    public static int cmtLineCount;

    private OutputWriter(){
        lexErrWriter = null;
        lexOutWriter = null;
        lineCount = -1;
        nextLineCount = -1;
        cmtLineCount = -1;
    }

    public static void openLexWriteStream(){
        try{
            lexErrWriter = new BufferedWriter(new FileWriter("test.outlexerrors"));
            lexOutWriter = new BufferedWriter(new FileWriter("test.outlextokens"));
        } catch (IOException e){
            System.out.println("Error opening file to write!");
        }
        lineCount = 1;
        nextLineCount = 0;
        cmtLineCount = 0;
    }

    public static void openSyntaxWriteStream(){
        try{
            syntaxErrWriter = new BufferedWriter(new FileWriter("test.outsyntaxerrors"));
            syntaxOutWriter = new BufferedWriter(new FileWriter("test.outderivation"));
        } catch(IOException e){
            System.out.println("Error opening syntax file to write");
        }
    }

    public static void openTreeWriteStream(){
        try{
            treeWriter = new BufferedWriter(new FileWriter("tree.outast"));
        } catch(IOException e){
            System.out.println("Error opening graphviz file to write");
        }
    }

    public static void openSemanticOutWriting(){
        try{
            semanticOutWriter = new BufferedWriter(new FileWriter("test.outsymboltables"));
        } catch(IOException e){
            System.out.println("Error opening out symbol table file to write");
        }
    }

    public static void openSemanticErrWriting(){
        try{
            semanticErrWriter = new BufferedWriter(new FileWriter("test.outsemanticerrors"));
        } catch(IOException e){
            System.out.println("Error opening semantic error file to write");
        }
    }

    public static void openCodeDeclGen(){
        try{
            codeDeclWriter = new BufferedWriter(new FileWriter("decl.m"));
            codeDeclWriter.write("% Convention:\n");
            codeDeclWriter.write("%\tr14 is used for stack pointer\n");
            codeDeclWriter.write("%\tr13 is used for frame pointer");
            // codeDeclWriter.write("\n\n% Utils:\n");
            // codeDeclWriter.write("% Write an int to stdout.\n");
            // codeDeclWriter.write("% Entry: -4(r10) -> int/float argument\n");
            // codeDeclWriter.write("putint\tlw r1,-8(r10)\n");
            // codeDeclWriter.write("\t\taddi r2,r0,0\n");
            // codeDeclWriter.write("putint1\tlb r2,0(r1)\n");
            // codeDeclWriter.write("\t\tceqi r3,r2,0\n");
            // codeDeclWriter.write("\t\tbnz r3,putint2\n");
            // codeDeclWriter.write("\t\tputc r2\n");
            // codeDeclWriter.write("\t\taddi r1,r1,1\n");
            // codeDeclWriter.write("\t\tj putint1\n");
            // codeDeclWriter.write("putint2\tjr r15\n");
            // codeDeclWriter.write("\n% Read a string from stdin\n");
            // codeDeclWriter.write("% Entry: -4(r10) -> buffer\n");
            // codeDeclWriter.write("getint\tlw r1,-4(r10)\n");
            // codeDeclWriter.write("getint1\tgetc r2\n");
            // codeDeclWriter.write("\tceqi r3,r2,10\n");
            // codeDeclWriter.write("\tbnz r3,getint2\n");
            // codeDeclWriter.write("\tsb 0(r1),r2\n");
            // codeDeclWriter.write("\taddi r1,r1,1\n");
            // codeDeclWriter.write("\tj getint1\n");
            // codeDeclWriter.write("getint2\tsb 0(r1),r0\n");
            // codeDeclWriter.write("\tjr r15\n");
            // codeDeclWriter.write("\n% Convert string to integer. Skip leading blanks. Accept leading sign\n");
            // codeDeclWriter.write("% Entry: -4(r10) -> string\n");
            // codeDeclWriter.write("% Exit: result in r11\n");
            // codeDeclWriter.write("strint\taddi r11,r0,0\n");
            // codeDeclWriter.write("\taddi r4,r0,0\n");
            // codeDeclWriter.write("\tlw r1,-4(r10)\n");
            // codeDeclWriter.write("\taddi r2,r0,0\n");
            // codeDeclWriter.write("strint1\tlb r2,0(r1)\n");
            // codeDeclWriter.write("cnei\tr3,r2,32\n");
            // codeDeclWriter.write("\tbnz r3,strint2\n");
            // codeDeclWriter.write("\taddi r1,r1,1\n");
            // codeDeclWriter.write("\tj strint1\n");
            // codeDeclWriter.write("strint2\tcnei r3,r2,43\n");
            // codeDeclWriter.write("\tbnz r3,strint3\n");
            // codeDeclWriter.write("\tj strint4\n");
            // codeDeclWriter.write("strint3\tcnei r3,r2,45\n");
            // codeDeclWriter.write("\tbnz r3,strint5\n");
            // codeDeclWriter.write("\taddi r4,r4,1\n");
            // codeDeclWriter.write("strint4\taddi r1,r1,1\n");
            // codeDeclWriter.write("\tlb r2,0(r1)\n");
            // codeDeclWriter.write("strint5\tclti r3,r2,48\n");
            // codeDeclWriter.write("\tbnz r3,strint6\n");
            // codeDeclWriter.write("\tcgti r3,r2,57\n");
            // codeDeclWriter.write("\tbnzr3,strint6\n");
            // codeDeclWriter.write("\tsubi r2,r2,48\n");
            // codeDeclWriter.write("\tmuli r11,r11,10\n");
            // codeDeclWriter.write("\tadd r11, r11, r2\n");
            // codeDeclWriter.write("\tj strint4\n");
            // codeDeclWriter.write("strint6\tceqi r3,r4,0\n");
            // codeDeclWriter.write("\tbnz r3,strint7\n");
            // codeDeclWriter.write("\tsub r11,r0,r11\n");
            // codeDeclWriter.write("strint7\tjr r15\n");

        } catch(IOException e){
            System.out.println("Error opening semantic error file to write");
        }
    }

    public static void lexErrWriting(String output){
        try{
            lexErrWriter.write(output + ": line " + lineCount + ".\n");
            lexErrWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing error tokens to file!");
        }
    }

    public static void lexOutWriting(String output){
        try{
            if(output.compareTo("\n") == 0){
                if(nextLineCount == 0){
                    lexOutWriter.write("\n");
                    nextLineCount++;
                }
            } else{
                lexOutWriter.write(output + lineCount + "] ");
                nextLineCount = 0;
            }
            lexOutWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing out tokens to file!");
        }
    }

    public static void syntaxOutWriting(){
        try{
            syntaxOutWriter.write(GrammarStack.printStack() + "\n");
            syntaxOutWriter.write(Factory.printNodeStack() + "\n\n");
            syntaxOutWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing Grammar stack to file!");
        }
    }

    public static void syntaxErrWriting(String error){
        try{
            syntaxErrWriter.write(error + "\n");
            syntaxErrWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing Grammar error to file!");
        }
    }

    public static void treeWriting(String output){
        try{
            treeWriter.write(output);
            treeWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing out trees to file!");
        }
    }

    public static void cmtWriting(String output){
        try{
            lexOutWriter.write(output + (lineCount - cmtLineCount) + "]\n");
            cmtLineCount = 0;
            lexOutWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing comment to file!");
        }
    }

    public static void semanticOutWriting(String output){
        try{
            semanticOutWriter.write(output + "\n");
            semanticOutWriter.flush();
        } catch (IOException e){
            System.out.println("Error writing symbol tables out to file!");
        }
    }

    public static void semanticErrWriting(String output){
        try{
            semanticErrWriter.write(output + "\n");
            semanticErrWriter.flush();
        } catch (IOException e){
            System.out.println("Error writing symbol tables out to file!");
        }
    }

    public static void codeDeclGen(String output){
        try{
            codeDeclWriter.write(output + "\n");
            codeDeclWriter.flush();
        } catch (IOException e){
            System.out.println("Error writing symbol tables out to file!");
        }
    }

    public static void closeLexWriteStream(){
        try{
            lexErrWriter.close();
            lexOutWriter.close();
        } catch(IOException e){
            System.out.println("Error closing writer stream!");
        }
    }

    public static void closeSyntaxWriteStream(){
        try{
            syntaxErrWriter.close();
            syntaxOutWriter.close();
        } catch(IOException e){
            System.out.println("Error closing syntax writer stream!");
        }
    }

    public static void closeTreeWriteStream(){
        try{
            treeWriter.close();
        } catch(IOException e){
            System.out.println("Error closing tree writer stream!");
        }
    }

    public static void closeSemanticOutStream(){
        try{
            semanticOutWriter.close();
        } catch(IOException e){
            System.out.println("Error closing semantic out writer stream!");
        }
    }

    public static void closeSemanticErrStream(){
        try{
            semanticErrWriter.close();
        } catch(IOException e){
            System.out.println("Error closing semantic err writer stream!");
        }
    }

    public static void closeCodeDeclGenStream(){
        try{
            codeDeclWriter.close();
        } catch(IOException e){
            System.out.println("Error closing semantic err writer stream!");
        }
    }
}
