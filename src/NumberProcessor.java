import java.util.regex.Pattern;

public class NumberProcessor implements Processor{
    private String storage;
    private boolean stateFinal;
    public NumType numType;
    private int state;
    private final int[][] transitionTable = {{1, 2, 2, 3, 6, 6, 6, 8, 2, 9}, {3, 2, 2, 3, 5, 5, 5, 9, 9, 9}, 
    {2, 4, 2, 4, 2, 2, 2, 2, 2, 2}, {2, 2, 2, 2, 2, 7, 2, 2, 2, 2}, {2, 2, 2, 2, 2, 2, 2, 8, 2, 2}, {0, 1, 0, 1, 0, 1, 0, 0, 0, 1}};

    public NumberProcessor(){
        /*
         * Constructor
         */
        storage = "";
        state = 0;
        stateFinal = false;
        numType = NumType.INVALID;
    }

    public void processToken(String token, Type type){
        if(type == Type.ZERO){
            state = transitionTable[0][state];
        } else if(type == Type.NONZERO){
            state = transitionTable[1][state];
        } else if(type == Type.ALPHA){
            // Need to check
            if(token.compareTo("e") == 0 && numType == NumType.FLOAT){
                state = transitionTable[3][state];
            } else {
                state = 2;
            }
        } else if(type == Type.SYMBOL){
            if(Pattern.compile("[\\.]").matcher(token).lookingAt()){
                state = transitionTable[2][state];
            } else if(Pattern.compile("[\\+\\-]").matcher(token).lookingAt()){
                state = transitionTable[4][state];
            } else{
                state = 2;
            }
        } else {
            state = 2;
        }
        setNumType();
        stateFinal = (transitionTable[5][state] == 1) ? true : false;
        storage += token;
        return;
    }

    public boolean stateCheck(){
        boolean output = false;

        if(!stateFinal){
            OutputWriter.errWriting("Lexical error: Invalid number: " + storage);
            System.out.println("Invalid number: " + this.storage);
        } else {
            OutputWriter.outWriting("[");
            if(numType == NumType.FLOAT){
                OutputWriter.outWriting("floatnum, ");
            } else {
                OutputWriter.outWriting("intnum, ");
            }
            OutputWriter.outWriting(this.storage + ", ");
            System.out.println("Number processed: " + this.storage);
            output = true;
        }
        storage = "";
        state = 0;
        return output;
    }

    private void setNumType(){
        switch(state){
            case 1:
                numType = NumType.ZERO;
                break;
            case 3:
                numType = NumType.INTEGER;
                break;
            case 5:
                numType = NumType.FLOAT;
                break;
            case 7:
                numType = NumType.FLOATE;
                break;
            case 9:
                numType = NumType.FLOAT;
                break;
            default:
                numType = NumType.INVALID;
                break;
        }
    }
}
