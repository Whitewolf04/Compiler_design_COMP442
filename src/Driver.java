import java.util.regex.Pattern;

public class Driver {
    public static void main(String[] args){
        String file = "Some random string 1234.123";


        Processor currentProcessor = null;
        AlphabetProcessor alphaProcessor = new AlphabetProcessor();
        NumberProcessor numberProcessor = new NumberProcessor();
        SymbolProcessor symbolProcessor =  new SymbolProcessor();
        State currentState = State.START;

        String[] inputArray = file.split("");
        Pattern lowAlpha = Pattern.compile("[a-z]");
        Pattern upperAlpha = Pattern.compile("[A-Z]");
        Pattern nonZero = Pattern.compile("[1-9]");
        Pattern zero = Pattern.compile("0");
        Pattern symbol = Pattern.compile("[\\Q+-*/=><(){}[].,;:\\E]");
        Pattern space = Pattern.compile("\s");

        for(String token : inputArray){
            switch(currentState){
                case START:
                    if(lowAlpha.matcher(token).lookingAt()){
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.LOWALPHA);
                    } else if(upperAlpha.matcher(token).lookingAt()){
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.UPPERALPHA);
                    } else if(nonZero.matcher(token).lookingAt()){
                        currentState = State.NUMBER;
                        currentProcessor = numberProcessor;
                        currentProcessor.processToken(token, Type.NONZERO);
                    } else if(zero.matcher(token).lookingAt()){
                        currentState = State.NUMBER;
                        currentProcessor = numberProcessor;
                        currentProcessor.processToken(token, Type.ZERO);
                    } else if(symbol.matcher(token).lookingAt()){
                        currentState = State.SYMBOL;
                        currentProcessor = symbolProcessor;
                        currentProcessor.processToken(token, Type.SYMBOL);
                    } else if(space.matcher(token).lookingAt()){
                        continue;
                    } else {
                        /*
                         * Invalid symbols
                         */
                        System.out.println("Invalid symbols!");
                    }
                    break;
                case ALPHABET:
                    if(lowAlpha.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.LOWALPHA);
                    } else if(upperAlpha.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.UPPERALPHA);
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
                        continue;
                    } else {
                        /*
                        * Invalid symbols
                        */
                        System.out.println("Invalid symbols!");
                    }
                    break;
                case NUMBER:
                    if(lowAlpha.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.LOWALPHA);
                    } else if(upperAlpha.matcher(token).lookingAt()){
                        /*
                         * Invalid symbol
                         */
                        System.out.println("Invalid symbol!");
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
                        continue;
                    } else {
                        /*
                        * Invalid symbols
                        */
                        System.out.println("Invalid symbols!");
                    }
                    break;
                case SYMBOL:
                    if(lowAlpha.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.LOWALPHA);
                    } else if(upperAlpha.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.UPPERALPHA);
                    } else if(nonZero.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.NUMBER;
                        currentProcessor = numberProcessor;
                        currentProcessor.processToken(token, Type.NONZERO);
                    } else if(zero.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.NUMBER;
                        currentProcessor = numberProcessor;
                        currentProcessor.processToken(token, Type.ZERO);
                    } else if(symbol.matcher(token).lookingAt()){
                        currentProcessor.processToken(token, Type.SYMBOL);
                    } else if(space.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.START;
                        continue;
                    } else {
                        /*
                        * Invalid symbols
                        */
                        System.out.println("Invalid symbols!");
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
