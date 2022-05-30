import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.*;

public class Memrise {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n0: Quit\n1: Get Words\n2: Speed Review\n");
            switch (scanner.nextInt()) {
                case 0 -> {
                    return;
                }
                case 1 -> getWords();
                case 2 -> {
                    if (speedReview() == 1) {
                        System.out.println("\nDictionary not found, get words first.");
                    }
                }
            }
        }
    }

    public static void getWords() {
        WebDriver driver = new ChromeDriver();
        PrintWriter writer;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("dictionary.txt")));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 1; i <= 37; i++) {
            driver.get("https://app.memrise.com/course/5546824/familia-romana-llpsi-eaglebrook-school/" + i + "/");
            String[] words = driver.findElement(By.cssSelector(".things.clearfix")).getText().split("\n");
            for (int j = 1; j < (words.length - 1); j += 2) {
                writer.println(words[j] + ": " + words[j + 1]);
            }
        }
        driver.close();
        driver.quit();
        writer.close();
    }

    public static int speedReview() {
        Map<String, String> dictionaryLn = new HashMap<>();
        Map<String, Set<String>> dictionaryEn = new HashMap<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("dictionary.txt"));
        } catch (FileNotFoundException e) {
            return 1;
        }
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] entry = line.split(": ");
                dictionaryLn.put(entry[0], entry[1]);
                if (dictionaryEn.get(entry[1]) != null) {
                    dictionaryEn.get(entry[1]).add(entry[0]);
                } else {
                    dictionaryEn.put(entry[1], new HashSet<>(Collections.singleton(entry[0])));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        }

        System.out.println("Target points:"); //TODO this block temp
        Scanner scanner = new Scanner(System.in);
        int stop = scanner.nextInt();
        scanner.close();

        WebDriver driver = new ChromeDriver();
        driver.get("https://app.memrise.com/course/5546824/familia-romana-llpsi-eaglebrook-school/");
        while (true) {
            if (driver.getCurrentUrl().equals("https://app.memrise.com/dashboard")) {
                driver.get("https://app.memrise.com/course/5546824/familia-romana-llpsi-eaglebrook-school/");
            }
            try {
                driver.findElement(By.cssSelector("#promotion-modal > div > button")).click();
            } catch (NoSuchElementException | ElementNotInteractableException ignored) {
            }
            try {
                driver.findElement(By.cssSelector("#content > div > div > div.course-progress-container > div.progress-box.progress-box-course > div.actions.actions-right > a:nth-child(2)")).click();
                break;
            } catch (NoSuchElementException | ElementClickInterceptedException ignored) {
            }
        }
        int totalCount = 0, totalPoints = 0;
        while (true) {
            try {
                String question = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[2]/div/div[1]/h2")).getText();
                Set<String> result1 = dictionaryEn.get(question);
                String result2 = dictionaryLn.get(question);
                for (WebElement answerButton : driver.findElements(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[2]/div/div[2]/*/button"))) {
                    if (answerButton.isEnabled()) {
                        String answer = answerButton.findElement(By.xpath(".//div[2]")).getText();
                        if (result1 != null && result1.contains(answer) || answer.equals(result2)) {
                            answerButton.click();
                            totalCount++;
                            int points = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div[2]/div/span/div/span")).getText());
                            System.out.println("total Count" + totalCount); //TODO
                            System.out.println("Total Points" + totalPoints + points + "\n"); //TODO
                            Thread.sleep(800);
                        }
                    }
                }
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                try {
                    driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div/div[2]/div[3]/div[2]/button"));
                    totalPoints += Integer.parseInt(driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div/div[1]/div[1]/div[2]/div/span/div/span")).getText());
                    System.out.println("total Count" + totalCount); //TODO
                    System.out.println("Total Points" + totalPoints + "\n"); //TODO
                    if (totalPoints >= stop) {
                        break;
                    }
                    driver.get("https://app.memrise.com/course/5546824/familia-romana-llpsi-eaglebrook-school/");
                    driver.findElement(By.cssSelector("#content > div > div > div.course-progress-container > div.progress-box.progress-box-course > div.actions.actions-right > a:nth-child(2)")).click();
                } catch (NoSuchElementException | ElementClickInterceptedException ignored) {
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        driver.close();
        driver.quit();
        return 0;
    }
}
