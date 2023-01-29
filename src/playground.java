import java.util.regex.Pattern;

public class playground {
    public static void main(String[] args) {
        String myString = """
                Some random string\n
                Some random string
                Some random string
                """;;
        String[] myArray = myString.split("");
        for(String s : myArray){
            if(Pattern.compile("[\\n]").matcher(s).lookingAt()){
                System.out.println("New line");
            }
        }
    }
}
