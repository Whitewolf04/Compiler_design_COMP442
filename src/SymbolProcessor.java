public class SymbolProcessor implements Processor{
    private String storage;
    private boolean stateFinal;
    private boolean err;
    private final String[] SYMBOLS = {"+", "-", "*", "/", "=", ">", "<", ";", ":", ",", ".", "(", ")", "{", "}", "[", "]"};

    public SymbolProcessor(){
        /*
         * Constructor
         */
        storage = "";
        stateFinal = false;
        err = false;
    }

    public void processToken(String token, Type type){
        
    }

    public boolean stateCheck(){
        return true;
    }
}
