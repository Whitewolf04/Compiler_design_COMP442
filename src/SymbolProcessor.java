import java.util.regex.Pattern;

public class SymbolProcessor implements Processor{
    private String storage;
    private boolean stateFinal;
    private Sym symbol;
    private int state;
    public boolean inlineCmt;
    public boolean cmt;
    private final int[][] transitionTable = {{1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,0}, {3,5,4,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1},
    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, 
    {6,5,7,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, 
    {9,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, 
    {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,14,17,19,20,21,22,23,24,25,26,28,29,2,2,1}, 
    {14,14,14,14,14,15,14,14,14,14,14,14,14,14,14,14,14,14,14,0}, {14,14,14,14,14,15,16,14,14,14,14,14,14,14,14,14,14,14,14,0}, 
    {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,18,0}, 
    {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, 
    {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, 
    {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,27,28,29,2,2,1}, 
    {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}, {1,5,8,10,11,12,13,19,20,21,22,23,24,25,26,28,29,2,2,1}};

    enum Sym{
        EQUAL,
        LESS,
        MORE,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        OPENPAR,
        CLOSEPAR,
        OPENCUR,
        CLOSECUR,
        OPENSQ,
        CLOSESQ,
        SEMCOL,
        COL,
        COMMA,
        PERIOD,
        NEXTLINE,
        INVALID,
    }

    public SymbolProcessor(){
        /*
         * Constructor
         */
        storage = "";
        stateFinal = false;
        inlineCmt = false;
        cmt = false;
        symbol = Sym.INVALID;
        state = 0;
    }

    public void processToken(String token, Type type){
        if(type == Type.SYMBOL){
            symbol = identifier(token);
            if(!cmt && !inlineCmt)
                symbolChange(symbol);
            if(symbol == Sym.EQUAL){
                state = transitionTable[state][0];
            } else if(symbol == Sym.LESS){
                state = transitionTable[state][1];
            } else if(symbol == Sym.MORE){
                state = transitionTable[state][2];
            } else if(symbol == Sym.PLUS){
                state = transitionTable[state][3];
            } else if (symbol == Sym.MINUS){
                state = transitionTable[state][4];
            } else if(symbol == Sym.MULTIPLY){
                state = transitionTable[state][5];
            } else if(symbol == Sym.DIVIDE){
                state = transitionTable[state][6];
            } else if(symbol == Sym.OPENPAR){
                state = transitionTable[state][7];
            } else if(symbol == Sym.CLOSEPAR){
                state = transitionTable[state][8];
            } else if(symbol == Sym.OPENCUR){
                state = transitionTable[state][9];
            } else if(symbol == Sym.CLOSECUR){
                state = transitionTable[state][10];
            } else if(symbol == Sym.OPENSQ){
                state = transitionTable[state][11];
            } else if(symbol == Sym.CLOSESQ){
                state = transitionTable[state][12];
            } else if(symbol == Sym.SEMCOL){
                state = transitionTable[state][13];
            } else if(symbol == Sym.COL){
                state = transitionTable[state][14];
            } else if(symbol == Sym.COMMA){
                state = transitionTable[state][15];
            } else if(symbol == Sym.PERIOD){
                state = transitionTable[state][16];
            } else if (inlineCmt || cmt){
                state = transitionTable[state][17];
            } else {
                state = 2;
            }
        } else if(type == Type.SPACE){
            if(identifier(token) == Sym.NEXTLINE){
                if(cmt || inlineCmt){
                    OutputWriter.cmtLineCount++;
                }
                state = transitionTable[state][18];
            }
        } else {
            state = transitionTable[state][17];
        }

        if(state == 14 || state == 15){
            cmt = true;
        } else if(state == 17){
            inlineCmt = true;
        } else {
            cmt = false;
            inlineCmt = false;
        }

        stateFinal = (transitionTable[state][19] == 1) ? true : false;

        if(identifier(token) == Sym.NEXTLINE){
            storage += "\\n";
        } else {
            storage += token;
        }

    }

    private Sym identifier(String token){
        if(Pattern.compile("[\\=]").matcher(token).lookingAt()){
            return Sym.EQUAL;
        }
        if(Pattern.compile("[\\<]").matcher(token).lookingAt()){
            return Sym.LESS;
        }
        if(Pattern.compile("[\\>]").matcher(token).lookingAt()){
            return Sym.MORE;
        }
        if(Pattern.compile("[\\+]").matcher(token).lookingAt()){
            return Sym.PLUS;
        }
        if(Pattern.compile("[\\-]").matcher(token).lookingAt()){
            return Sym.MINUS;
        }
        if(Pattern.compile("[\\*]").matcher(token).lookingAt()){
            return Sym.MULTIPLY;
        }
        if(Pattern.compile("[\\/]").matcher(token).lookingAt()){
            return Sym.DIVIDE;
        }
        if(Pattern.compile("[\\(]").matcher(token).lookingAt()){
            return Sym.OPENPAR;
        }
        if(Pattern.compile("[\\)]").matcher(token).lookingAt()){
            return Sym.CLOSEPAR;
        }
        if(Pattern.compile("[\\{]").matcher(token).lookingAt()){
            return Sym.OPENCUR;
        }
        if(Pattern.compile("[\\}]").matcher(token).lookingAt()){
            return Sym.CLOSECUR;
        }
        if(Pattern.compile("[\\[]").matcher(token).lookingAt()){
            return Sym.OPENSQ;
        }
        if(Pattern.compile("[\\]]").matcher(token).lookingAt()){
            return Sym.CLOSESQ;
        }   
        if(Pattern.compile("[\\;]").matcher(token).lookingAt()){
            return Sym.SEMCOL;
        }
        if(Pattern.compile("[\\:]").matcher(token).lookingAt()){
            return Sym.COL;
        }
        if(Pattern.compile("[\\,]").matcher(token).lookingAt()){
            return Sym.COMMA;
        }
        if(Pattern.compile("[\\.]").matcher(token).lookingAt()){
            return Sym.PERIOD;
        }
        if(Pattern.compile("[\\n]").matcher(token).lookingAt()){
            return Sym.NEXTLINE;
        }

        return Sym.INVALID;
    }

    private String symbolToString(){
        switch(state){
            case 1:
                return "assign";
            case 3:
                return "equal";
            case 4:
                return "returntype";
            case 5:
                return "lt";
            case 6:
                return "leq";
            case 7:
                return "noteq";
            case 8:
                return "gt";
            case 9:
                return "geq";
            case 10:
                return "plus";
            case 11:
                return "minus";
            case 12:
                return "mult";
            case 13:
                return "div";
            case 16:
                return "blockcmt";
            case 18:
                return "inlinecmt";
            case 19:
                return "openpar";
            case 20:
                return "closepar";
            case 21:
                return "opencubr";
            case 22:
                return "closecubr";
            case 23:
                return "opensqbr";
            case 24:
                return "closesqbr";
            case 25:
                return "semicol";
            case 26:
                return "colon";
            case 27:
                return "scopeop";
            case 28:
                return "comma";
            case 29:
                return "dot";
            default:
                return "invalid";
        }
    }

    public boolean stateCheck(){
        boolean output = false;
        if(!stateFinal){
            OutputWriter.errWriting("Lexical error: Invalid symbol: " + this.storage);
            System.out.println("Invalid symbol: " + this.storage);
        } else{
            if(state == 16 || state == 18){
                OutputWriter.cmtWriting("[" + symbolToString() + ", " + this.storage + ", ");
            } else {
                OutputWriter.outWriting("[" + symbolToString() + ", " + this.storage + ", ");
            }
            System.out.println("Symbol processed: " + this.storage);
            output = true;
        }
        storage = "";
        state = 0;
        return output;
    }

    private void symbolChange(Sym nextSymbol){
        int oldState = state;
        // String oldStorage = storage;
        switch(state){
            case 0:
                break;
            case 1:
                if(nextSymbol == Sym.EQUAL || nextSymbol == Sym.MORE){
                    break;
                } 
            case 5:
                if(nextSymbol == Sym.MORE || nextSymbol == Sym.EQUAL){
                    break;
                }
            case 8:
                if(nextSymbol == Sym.EQUAL){
                    break;
                }
            case 13:
                if(nextSymbol == Sym.MULTIPLY || nextSymbol == Sym.DIVIDE){
                    return;
                }
            case 26:
                if(nextSymbol == Sym.COL){
                    break;
                }
            default:
                stateCheck();
                state = oldState;
        }
    }
}
