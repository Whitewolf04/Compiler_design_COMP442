package syntax_analyzer;

public abstract class GrammarToken {
    abstract public boolean compareToString(String another);
    abstract public String toString();
}
