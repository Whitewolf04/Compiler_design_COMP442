package lexical_analyzer;

public interface Processor {
    public void processToken(String token, Type type);
    public boolean stateCheck();
}
