import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

public class Didascalica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

//        ChromeOptions options = new ChromeOptions();
//        String chromeProfilePath = "";
//        options.addArguments("user-data-dir=" + chromeProfilePath, "--profile-directory=Default");
//        WebDriver driver = new ChromeDriver(options);
        WebDriver driver = new ChromeDriver();

        driver.get(scanner.next());
        scanner.close();
        int questions;
        for (questions = 0; ; questions++) {
            try {
                driver.findElement(By.id("Gap" + questions));
            } catch (NoSuchElementException e) {
                break;
            }
        }
        String[] answers = new String[questions];
        int count = 0;
        for (int i = 0; count < questions; i++) {
            for (int j = 0; j < questions; j++) {
                if(answers[j]!=null){
                    continue;
                }
                try {
                    Select select = new Select(driver.findElement(By.id("Gap" + j)));
                    select.selectByIndex(i);
                } catch (NoSuchElementException e) {
                    answers[j] = driver.findElement(By.id("GapSpan" + j)).getText();
                    count++;
                }
            }
            driver.findElement(By.id("CheckButton2")).click();
        }
        driver.navigate().refresh();
        for (int i = 0; i < questions; i++) {
            Select select = new Select(driver.findElement(By.id("Gap" + i)));
            select.selectByValue(answers[i]);
        }
        driver.findElement(By.id("CheckButton2")).click();
    }
}