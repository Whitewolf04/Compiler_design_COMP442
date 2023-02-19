package lexical_analyzer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public final class OutputWriter {
    private static BufferedWriter errWriter;
    private static BufferedWriter outWriter;
    public static int lineCount;
    private static int nextLineCount;
    public static int cmtLineCount;

    private OutputWriter(){
        errWriter = null;
        outWriter = null;
        lineCount = -1;
        nextLineCount = -1;
        cmtLineCount = -1;
    }

    public static void openWriteStream(){
        try{
            errWriter = new BufferedWriter(new FileWriter("test.outlexerrors"));
            outWriter = new BufferedWriter(new FileWriter("test.outlextokens"));
        } catch (IOException e){
            System.out.println("Error opening file to write!");
        }
        lineCount = 1;
        nextLineCount = 0;
        cmtLineCount = 0;
    }

    public static void errWriting(String output){
        try{
            errWriter.write(output + ": line " + lineCount + ".\n");
            errWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing error tokens to file!");
        }
    }

    public static void outWriting(String output){
        try{
            if(output.compareTo("\n") == 0){
                if(nextLineCount == 0){
                    outWriter.write("\n");
                    nextLineCount++;
                }
            } else{
                outWriter.write(output + lineCount + "] ");
                nextLineCount = 0;
            }
            outWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing out tokens to file!");
        }
    }

    public static void cmtWriting(String output){
        try{
            outWriter.write(output + (lineCount - cmtLineCount) + "]\n");
            cmtLineCount = 0;
            outWriter.flush();
        } catch(IOException e){
            System.out.println("Error writing comment to file!");
        }
    }

    public static void closeWriteStream(){
        try{
            errWriter.close();
            outWriter.close();
        } catch(IOException e){
            System.out.println("Error closing writer stream!");
        }
    }

}
