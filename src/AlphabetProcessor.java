
public class AlphabetProcessor implements Processor{
    public String storage;
    private boolean stateFinal;
    private int state;
    private boolean err;
    final String[] RESERVED = {"or", "and", "not", "integer", "float", "void", "class", "self", "isa", "while", "if", "then", "else", "read", "write", "return", "localvar", "constructor", "attribute", "function", "public", "private"};
    private final int[][] transitionTable = {{1, 1, 2}, {2, 1, 2}, {2, 1, 2}, {0, 1, 0}};

    public AlphabetProcessor(){
        /*
         * Constructor
         */
        super();
        storage = "";
        state = 0;
        stateFinal = false;
        err = false;
    }
    
    /*
     * processToken() is called to process each token inputted
     */
    public void processToken(String token, Type type){
        if(err == true){
            return;
        }

        if(type == Type.ALPHA){
            state = transitionTable[0][state];
        } else if(type == Type.UNDERSCORE){
            state = transitionTable[2][state];
        } else if(type == Type.NONZERO || type == Type.ZERO){
            state = transitionTable[1][state];
        } else {
            state = 2;
        }

        stateFinal = (transitionTable[3][state] == 1) ? true : false;
        storage += token;
        return;
    }

    /*
     * stateCheck() is only called before changing state in order to confirm 
     * if the most recent tokens processed are valid or not
     */
    public boolean stateCheck(){
        boolean output = false;

        if(err || !stateFinal){
            OutputWriter.errWriting("Lexical error: Invalid string: " + this.storage);
            System.out.println("Invalid string: " + this.storage);
        } else if(isReservedWord(this.storage)){
            OutputWriter.outWriting("[" + this.storage + ", " + this.storage + ", ");
            System.out.println("Reserved word: " + this.storage);
            output = true;
        } else{
            OutputWriter.outWriting("[id, " + this.storage + ", ");
            System.out.println("String processed: " + this.storage);
            output = true;
        }

        this.storage = "";
        state = 0;
        return output;
    }

    private boolean isReservedWord(String storage){
        for(int i = 0; i < RESERVED.length; i++){
            if(storage.compareTo(RESERVED[i]) == 0)
                return true;
        }
        return false;
    }
}
