package syntax_analyzer;

import java.util.HashMap;
import java.util.Stack;

public class NonTerminal extends GrammarToken{
    Terminal[] first;
    Terminal[] follow;
    String name;
    HashMap<String, Stack<GrammarToken>> tableEntry;

    public NonTerminal(String name, Terminal[] firstSet, Terminal[] followSet){
        first = firstSet;
        follow = followSet;
        this.name = name;
        tableEntry = new HashMap<String, Stack<GrammarToken>>();
        System.out.println("NonTerminal added");
    }

    public boolean inFirst(Terminal token){
        for(int i = 0; i < first.length; i++){
            if(token.equals(first[i])){
                return true;
            }
        }
        return false;
    }

    public boolean inFollow(Terminal token){
        for(int i = 0; i < follow.length; i++){
            if(token.equals(follow[i])){
                return true;
            }
        }
        return false;
    }

    public boolean compareToString(String another){
        return another.equals(name);
    }

    public String toString(){
        return name;
    }
}
