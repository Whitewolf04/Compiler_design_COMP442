import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class UnitTest {
    Processor tempProcessor = null;
    AlphabetProcessor alphaProcessor = new AlphabetProcessor();
    
    @Test
    void processTokenTest(){
        String testString = "Some random string";
        String[] stringArray = testString.split("");
        tempProcessor = alphaProcessor;

        for(String str : stringArray){
            System.out.println("Token: " + str);
            if(str == " "){
                tempProcessor.stateCheck();
            } else{
                tempProcessor.processToken(str, Type.LOWALPHA);
            }
            
        }
    }
}
