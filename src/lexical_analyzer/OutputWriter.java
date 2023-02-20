package lexical_analyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import syntax_analyzer.GrammarStack;


public final class OutputWriter {
    private static BufferedWriter lexErrWriter;
    private static BufferedWriter lexOutWriter;
    private static BufferedWriter syntaxErrWriter;
    private static BufferedWriter syntaxOutWriter;
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

    public static void cmtWriting(String output){
        try{
            lexOutWriter.write(output + (lineCount - cmtLineCount) + "]\n");
            cmtLineCount = 0;
            lexOutWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing comment to file!");
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

}
