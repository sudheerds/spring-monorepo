import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
    public static void main(String[] args) {
        String path = "/api/orders/actuator/prometheus";
        String regex = "/api/orders/actuator/(?<segment>.*)";
        
        test(path, regex, "/api/actuator/$\\\\{segment}");
        test(path, regex, "/api/actuator/$\\{segment}");
        test(path, regex, "/api/actuator/${segment}");
        test(path, regex, "/api/actuator/\\\\${segment}");
    }
    
    private static void test(String path, String regex, String replacement) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(path);
            String result = matcher.replaceAll(replacement);
            System.out.println("Pattern: " + replacement + " -> Result: " + result);
        } catch (Exception e) {
            System.out.println("Pattern: " + replacement + " -> Error: " + e.toString());
        }
    }
}
