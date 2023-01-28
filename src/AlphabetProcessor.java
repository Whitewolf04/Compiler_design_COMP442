
public class AlphabetProcessor implements Processor{
    public String storage;
    private boolean stateFinal;
    private boolean err;
    final String[] RESERVED = {"or", "and", "not", "integer", "float", "void", "class", "self", "isa", "while", "if", "then", "else", "read", "write", "return", "localvar", "constructor", "attribute", "function", "public", "private"};

    public AlphabetProcessor(){
        /*
         * Constructor
         */
        super();
        storage = "";
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

        if(this.storage == ""){
            if(type == Type.LOWALPHA || type == Type.UPPERALPHA){
                stateFinal = true;
                storage += token;
                return;
            } else{
                err = true;
                stateFinal = false;
                return;
            }
        } else {
            if(token == "_"){
                this.storage += token;
                stateFinal = true;
                return;
            } else if (type == Type.NONZERO || type == Type.ZERO){
                this.storage += token;
                stateFinal = true;
                return;
            } else if (type == Type.UPPERALPHA || type == Type.LOWALPHA){
                this.storage += token;
                stateFinal = true;
                return;
            } else {
                err = true;
                stateFinal = false;
                return;
            }
        }
    }

    /*
     * stateCheck() is only called before changing state in order to confirm 
     * if the most recent tokens processed are valid or not
     */
    public boolean stateCheck(){
        System.out.println("String processed: " + this.storage);
        this.storage = "";
        if(err || !stateFinal){
            return false;
        } else if(isReservedWord(storage)){
            return false;
        } else{
            stateFinal = false;
            return true;
        }
    }

    private boolean isReservedWord(String storage){
        for(int i = 0; i < RESERVED.length; i++){
            if(storage == RESERVED[i])
                return true;
        }
        return false;
    }
}
