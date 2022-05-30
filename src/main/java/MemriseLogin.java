import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MemriseLogin {
    public static void main(String[] args) throws InterruptedException {
        int i;
        Scanner scanner = new Scanner(System.in);
        WebDriver driver = new ChromeDriver();
        Map<String, String> dictionary = new HashMap<>();
        i = scanner.nextInt();
        scanner.close();
        driver.get("https://app.memrise.com/signin");
        driver.findElement(By.id("username")).sendKeys("");
        System.out.println("Typed username");
        driver.findElement(By.id("password")).sendKeys("");
        System.out.println("Typed password");
        Thread.sleep(10000);
        System.out.println("Signed in");
        driver.get("https://app.memrise.com/course/5546824/familia-romana-llpsi-eaglebrook-school/" + i + "/");
        String[] words = driver.findElement(By.cssSelector(".things.clearfix")).getText().split("\n");
        for (i = 1; i < (words.length - 1); i += 2) {
            dictionary.put(words[i], words[i + 1]);
        }
        System.out.println(dictionary.keySet());
        System.out.println(dictionary.values());
        driver.findElement(By.cssSelector(".btn.btn-light-green")).click();
        System.out.println("Started learning");
        while(true){
            driver.findElement(By.cssSelector(".next-button.btn.btn-inverse.clearfix.sel-next-button")).click();
            System.out.println("Next");
        }
//        driver.quit();
    }
}