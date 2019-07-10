import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;
    String insurer = "Standard Insurance";

    @BeforeClass
    public void beforeClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://www.gobear.com/ph?x_session_type=UAT");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void verifyAtLeast3CardsDisplayed() {
        driver.findElement(By.cssSelector("li[data-gb-name='Insurance']")).click();
        driver.findElement(By.cssSelector("li[data-gb-name='Travel']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.name("product-form-submit"))).click();

        wait.until(ExpectedConditions.attributeToBe(By.cssSelector("div[data-gb-name='loading-status']"), "style", "display: none;"));
        String resultText = driver.findElement(By.cssSelector("div[data-gb-name='travel-nav-data']>h5")).getText();

        String arrResultText[] = resultText.split(" ");
        Assert.assertTrue(2 < Integer.parseInt(arrResultText[0]));
    }

    @Test
    public void verifyPromotionsFilterFunction() {
    }

    @Test
    public void verifyInsurersFilterFunction() throws InterruptedException {
        List<WebElement> options = driver.findElements(By.cssSelector(".checkbox.checkbox-primary"));
        for (WebElement option : options) {
            String value = option.getAttribute("data-filter-name");

            if (value.equals(insurer)) {
                option.click();
            }
        }
        Thread.sleep(3000);
        List<WebElement> cards = driver.findElements(By.cssSelector(".card-wrapper"));
        for (WebElement card : cards) {
            String valueCard = card.getAttribute("data-insuer-name");
            if (!valueCard.equals(insurer)) {
                System.out.println("Fail: " + valueCard);
            }
        }
    }

    @Test
    public void verifySortFunction() {
    }

    @Test
    public void verifyDetailsFunction() {
    }

    @AfterClass
    public void afterClass() {
        if (driver != null) {
            driver.quit();
        }
    }
}
