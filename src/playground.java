import java.util.regex.Pattern;

public class playground {
    public static void main(String[] args) {
        Pattern symbol = Pattern.compile("[\\Q+-*/=><(){}[].,;:\\E]");
        System.out.println(symbol.matcher("=").lookingAt());
    }
}
