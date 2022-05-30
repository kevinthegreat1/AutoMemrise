import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class MemriseDuplicatedWords {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        Map<String, List<String>> dictionary = new HashMap<>();
        driver.get("https://app.memrise.com/course/5546824/familia-romana-llpsi-eaglebrook-school/37/");
        for (int i = 1; i <= 37; i++) {
            driver.get("https://app.memrise.com/course/5546824/familia-romana-llpsi-eaglebrook-school/" + i + "/");
            String[] words = driver.findElement(By.cssSelector(".things.clearfix")).getText().split("\n");
            for (int j = 1; j < (words.length - 1); j += 2) {
                if (dictionary.get(words[j + 1]) != null) {
                    dictionary.get(words[j + 1]).add(i + ": " + words[j]);
                } else {
                    dictionary.put(words[j + 1], new ArrayList<>(Collections.singleton(i + ": " + words[j])));
                }
            }
        }
        driver.close();
        driver.quit();
        for (String key : dictionary.keySet()) {
            List<String> values = dictionary.get(key);
            if (values.size() > 1) {
                System.out.print(key + ": ");
                for (int i = 0; i < values.size(); i++) {
                    System.out.print(values.get(i));
                    if (i < values.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }
}