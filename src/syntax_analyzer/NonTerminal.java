package syntax_analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class NonTerminal extends GrammarToken{
    ArrayList<Terminal> first;
    ArrayList<Terminal> follow;
    HashMap<String, Stack<GrammarToken>> tableEntry;

    public NonTerminal(ArrayList<Terminal> firstSet, ArrayList<Terminal> followSet){
        first = firstSet;
        follow = followSet;
        tableEntry = new HashMap<String, Stack<GrammarToken>>();
    }

    public boolean inFirst(Terminal token){
        return first.contains(token);
    }

    public boolean inFollow(Terminal token){
        return follow.contains(token);
    }
}
