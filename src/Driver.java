import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;


public class Driver {
    public static void main(String[] args){
        String input = "";
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("lexpositivegrading.src"));

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

        OutputWriter.openWriteStream();


        Processor currentProcessor = null;
        AlphabetProcessor alphaProcessor = new AlphabetProcessor();
        NumberProcessor numberProcessor = new NumberProcessor();
        SymbolProcessor symbolProcessor =  new SymbolProcessor();
        State currentState = State.START;
        int lineCount = 1;

        String[] inputArray = input.split("");
        Pattern alpha = Pattern.compile("[a-zA-Z]");
        Pattern underscore = Pattern.compile("[\\_]");
        Pattern nonZero = Pattern.compile("[1-9]");
        Pattern zero = Pattern.compile("0");
        Pattern symbol = Pattern.compile("[\\Q+-*/=><(){}[].,;:\\E]");
        Pattern space = Pattern.compile("[\\s\\n]");

        for(String token : inputArray){
            if(token.compareTo("\n") == 0){
                lineCount++;
                OutputWriter.outWriting("\n");
            }
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
                        continue;
                    } else {
                        /*
                         * Invalid symbols
                         */
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
                        if(currentProcessor.stateCheck()){
                            OutputWriter.outWriting(lineCount + "] ");
                        } else {
                            OutputWriter.errWriting(": line " + lineCount + ".\n");
                        }
                        currentState = State.SYMBOL;
                        currentProcessor = symbolProcessor;
                        currentProcessor.processToken(token, Type.SYMBOL);
                    } else if(space.matcher(token).lookingAt()){
                        if(currentProcessor.stateCheck()){
                            OutputWriter.outWriting(lineCount + "] ");
                        } else {
                            OutputWriter.errWriting(": line " + lineCount + ".\n");
                        }
                        currentState = State.START;
                        continue;
                    } else {
                        /*
                        * Invalid symbols
                        */
                        System.out.println("Invalid symbols!");
                    }
                    break;
                case NUMBER:
                    if(alpha.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.ALPHA);
                    } else if(underscore.matcher(token).lookingAt()){
                        /*
                         * Invalid symbol
                         */
                        System.out.println("Invalid symbol!");
                    } else if(nonZero.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.NONZERO);
                    } else if(zero.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.ZERO);
                    } else if(symbol.matcher(token).lookingAt()){
                        if(token.compareTo(".") == 0){
                            currentProcessor.processToken(token, Type.SYMBOL);
                        } else if (numberProcessor.numType == NumType.FLOATE && Pattern.compile("[\\+\\-]").matcher(token).lookingAt()){
                            currentProcessor.processToken(token, Type.SYMBOL);
                        } else {
                            if(currentProcessor.stateCheck()){
                                OutputWriter.outWriting(lineCount + "] ");
                            } else {
                                OutputWriter.errWriting(": line " + lineCount + ".\n");
                            }
                            currentState = State.SYMBOL;
                            currentProcessor = symbolProcessor;
                            currentProcessor.processToken(token, Type.SYMBOL);
                        }
                    } else if(space.matcher(token).lookingAt()){
                        if(currentProcessor.stateCheck()){
                            OutputWriter.outWriting(lineCount + "] ");
                        } else {
                            OutputWriter.errWriting(": line " + lineCount + ".\n");
                        }
                        currentState = State.START;
                        continue;
                    } else {
                        /*
                        * Invalid symbols
                        */
                        System.out.println("Invalid symbols!");
                    }
                    break;
                case SYMBOL:
                    if(alpha.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.ALPHA);
                        } else{
                            if(currentProcessor.stateCheck()){
                                OutputWriter.outWriting(lineCount + "] ");
                            } else {
                                OutputWriter.errWriting(": line " + lineCount + ".\n");
                            }
                            currentState = State.ALPHABET;
                            currentProcessor = alphaProcessor;
                            currentProcessor.processToken(token, Type.ALPHA);
                        }
                    } else if(underscore.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.UNDERSCORE);
                        } else {
                            if(currentProcessor.stateCheck()){
                                OutputWriter.outWriting(lineCount + "] ");
                            } else {
                                OutputWriter.errWriting(": line " + lineCount + ".\n");
                            }
                            currentState = State.ALPHABET;
                            currentProcessor = alphaProcessor;
                            currentProcessor.processToken(token, Type.UNDERSCORE);
                        }
                    } else if(nonZero.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.NONZERO);
                        } else {
                            if(currentProcessor.stateCheck()){
                                OutputWriter.outWriting(lineCount + "] ");
                            } else {
                                OutputWriter.errWriting(": line " + lineCount + ".\n");
                            }
                            currentState = State.NUMBER;
                            currentProcessor = numberProcessor;
                            currentProcessor.processToken(token, Type.NONZERO);
                        }
                    } else if(zero.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.ZERO);
                        } else {
                            if(currentProcessor.stateCheck()){
                                OutputWriter.outWriting(lineCount + "] ");
                            } else {
                                OutputWriter.errWriting(": line " + lineCount + ".\n");
                            }
                            currentState = State.NUMBER;
                            currentProcessor = numberProcessor;
                            currentProcessor.processToken(token, Type.ZERO);
                        }
                    } else if(space.matcher(token).lookingAt()){
                        if(symbolProcessor.inlineCmt || symbolProcessor.cmt){
                            currentProcessor.processToken(token, Type.SPACE);
                        } else {
                            if(currentProcessor.stateCheck()){
                                OutputWriter.outWriting(lineCount + "] ");
                            } else {
                                OutputWriter.errWriting(": line " + lineCount + ".\n");
                            }
                            currentState = State.START;
                            continue;
                        }
                    } else {
                        /*
                        * Invalid symbols
                        */
                        currentProcessor.processToken(token, Type.SYMBOL);
                    }
                    break;
            }
        }

        if(currentProcessor.stateCheck()){
            /*
             * Implement state change procedure
             */
            System.out.println("Token valid!");
        } else{
            /* 
             * Implement state change procedure
             */
            System.out.println("Token invalid!");
        }
        currentState = State.START;
        OutputWriter.closeWriteStream();

    }
    
    public static boolean changeState(State current, State next){
        if(current == State.START){
            return true;
        } else if(current == next) {
            return false;
        } else{
            return true;
        }
    }

}
