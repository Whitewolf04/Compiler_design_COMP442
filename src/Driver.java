import java.util.regex.Pattern;


public class Driver {
    public static void main(String[] args){
        String file = "0.1230";


        Processor currentProcessor = null;
        AlphabetProcessor alphaProcessor = new AlphabetProcessor();
        NumberProcessor numberProcessor = new NumberProcessor();
        SymbolProcessor symbolProcessor =  new SymbolProcessor();
        State currentState = State.START;

        String[] inputArray = file.split("");
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
                        if(token.compareTo(".") == 0 && (numberProcessor.numType == NumType.INTEGER || numberProcessor.numType == NumType.ZERO)){
                            currentProcessor.processToken(token, Type.SYMBOL);
                        } else if (numberProcessor.numType == NumType.FLOATE && Pattern.compile("[\\+\\-]").matcher(token).lookingAt()){
                            currentProcessor.processToken(token, Type.SYMBOL);
                        } else {
                            currentProcessor.stateCheck();
                            currentState = State.SYMBOL;
                            currentProcessor = symbolProcessor;
                            currentProcessor.processToken(token, Type.SYMBOL);
                        }
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
                    if(alpha.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.ALPHA);
                    } else if(underscore.matcher(token).lookingAt()){
                        currentProcessor.stateCheck();
                        currentState = State.ALPHABET;
                        currentProcessor = alphaProcessor;
                        currentProcessor.processToken(token, Type.UNDERSCORE);
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
