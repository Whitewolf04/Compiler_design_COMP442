package lexical_analyzer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import syntax_analyzer.ProgramQueue;
import syntax_analyzer.Terminal;


public class LexAnalyzer {
    public static void analyze(String filename){
        String input = "";
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(filename));

            String currentLine;
            while((currentLine = reader.readLine()) != null){
                input += currentLine;
                input += "\n";
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
        } catch (IOException e){
            System.out.println("Error reading input file!");
        }

        OutputWriter.openLexWriteStream();

        Processor currentProcessor = null;
        AlphabetProcessor alphaProcessor = new AlphabetProcessor();
        NumberProcessor numberProcessor = new NumberProcessor();
        SymbolProcessor symbolProcessor =  new SymbolProcessor();
        State currentState = State.START;

        String[] inputArray = input.split("");
        Pattern alpha = Pattern.compile("[a-zA-Z]");
        Pattern underscore = Pattern.compile("[\\_]");
        Pattern nonZero = Pattern.compile("[1-9]");
        Pattern zero = Pattern.compile("0");
        Pattern symbol = Pattern.compile("[\\Q+-*/=><(){}[].,;:\\E]");
        Pattern space = Pattern.compile("[\\s\\n]");

        for(String token : inputArray){
            switch(currentState){
                case START:
                    if(alpha.matcher(token).lookingAt()){
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.ALPHA);
                    } else if(underscore.matcher(token).lookingAt()){
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.UNDERSCORE);
                    } else if(nonZero.matcher(token).lookingAt()){
                        currentState = State.NUMBER;
                        currentProcessor = numberProcessor;
                        currentProcessor.processToken(token, Type.NONZERO);
                    } else if(zero.matcher(token).lookingAt()){
                        currentState = State.NUMBER;
                        currentProcessor = numberProcessor;
                        currentProcessor.processToken(token, Type.ZERO);
                    } else if(space.matcher(token).lookingAt()){
                        break;
                    } else {
                        currentState = State.SYMBOL;
                        currentProcessor = symbolProcessor;
                        currentProcessor.processToken(token, Type.SYMBOL);
                    }
                    break;
                case ALPHABET:
                    if(alpha.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.ALPHA);
                    } else if(underscore.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.UNDERSCORE);
                    } else if(nonZero.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.NONZERO);
                    } else if(zero.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.ZERO);
                    } else if(symbol.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.SYMBOL;
                        currentProcessor = symbolProcessor;
                        currentProcessor.processToken(token, Type.SYMBOL);
                    } else if(space.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.START;
                        break;
                    } else {
                        /*
                        * Invalid symbols
                        */
                        currentProcessor.processToken(token, Type.SYMBOL);
                    }
                    break;
                case NUMBER:
                    if(alpha.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.ALPHA);
                    } else if(underscore.matcher(token).lookingAt()){
                        /*
                         * Invalid symbol
                         */
                        currentProcessor.processToken(token, Type.SYMBOL);
                    } else if(nonZero.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.NONZERO);
                    } else if(zero.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.ZERO);
                    } else if(space.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.START;
                        break;
                    } else if (symbol.matcher(token).lookingAt()){
                        if(token.compareTo(".") == 0){
                            currentProcessor.processToken(token, Type.SYMBOL);
                        } else if (numberProcessor.numType == NumType.FLOATE && Pattern.compile("[\\+\\-]").matcher(token).lookingAt()){
                            currentProcessor.processToken(token, Type.SYMBOL);
                        } else {
                            currentProcessor.stateCheck();
                            currentState = State.SYMBOL;
                            currentProcessor = symbolProcessor;
                            currentProcessor.processToken(token, Type.SYMBOL);
                        }
                    } else {
                        currentProcessor.processToken(token, Type.SYMBOL);
                    }
                    break;
                case SYMBOL:
                    if(alpha.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.ALPHA);
                        } else{
                            currentProcessor.stateCheck();
                            currentState = State.ALPHABET;
                            currentProcessor = alphaProcessor;
                            currentProcessor.processToken(token, Type.ALPHA);
                        }
                    } else if(underscore.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.UNDERSCORE);
                        } else {
                            currentProcessor.stateCheck();
                            currentState = State.ALPHABET;
                            currentProcessor = alphaProcessor;
                            currentProcessor.processToken(token, Type.UNDERSCORE);
                        }
                    } else if(nonZero.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.NONZERO);
                        } else {
                            currentProcessor.stateCheck();
                            currentState = State.NUMBER;
                            currentProcessor = numberProcessor;
                            currentProcessor.processToken(token, Type.NONZERO);
                        }
                    } else if(zero.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.ZERO);
                        } else {
                            currentProcessor.stateCheck();
                            currentState = State.NUMBER;
                            currentProcessor = numberProcessor;
                            currentProcessor.processToken(token, Type.ZERO);
                        }
                    } else if(space.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.SPACE);
                        } else {
                            currentProcessor.stateCheck();
                            currentState = State.START;
                            break;
                        }
                    } else {
                        currentProcessor.processToken(token, Type.SYMBOL);
                    }
                    break;
            }
            if(token.compareTo("\n") == 0){
                OutputWriter.lineCount++;
                ProgramQueue.recordLine();
                OutputWriter.lexOutWriting("\n");
            }
        }
        ProgramQueue.add(Terminal.START);
        OutputWriter.closeLexWriteStream();

    }


}
