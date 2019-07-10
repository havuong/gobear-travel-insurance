import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;
    String insurer = "Standard Insurance";
    String sortBy = "insurerName-Asc";

    @BeforeClass
    public void beforeClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://www.gobear.com/ph?x_session_type=UAT");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);

        driver.findElement(By.cssSelector("li[data-gb-name='Insurance']")).click();
        driver.findElement(By.cssSelector("li[data-gb-name='Travel']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.name("product-form-submit"))).click();

        wait.until(ExpectedConditions.attributeToBe(By.cssSelector("div[data-gb-name='loading-status']"), "style", "display: none;"));
    }

    @Test
    public void verifyAtLeast3CardsDisplayed() {
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
            Assert.assertTrue(valueCard.equals(insurer));
        }
    }

    @Test
    public void verifyRangeFilterFunction() throws InterruptedException {
        driver.findElement(By.cssSelector("#collapseSeemoreBtn")).click();
        WebElement slider = driver.findElement(By.xpath("(//div[@class='slider-handle min-slider-handle round'])[2]"));
        int x = slider.getSize().width;
        Actions act = new Actions(driver);
        act.dragAndDropBy(slider, 20, 0).build().perform();

        String minText = driver.findElement(By.cssSelector("#gb-slider-2 + b")).getAttribute("data-min-value");
        String maxText = driver.findElement(By.cssSelector("#gb-slider-2 + b + b")).getAttribute("data-max-value");

        Thread.sleep(2000);

        List<WebElement> valuePAs = driver.findElements(By.xpath("//p[text()='Medical expenses while traveling']/following-sibling::p/descendant::span"));
        for (WebElement valuePA : valuePAs) {
            int convertValuePA = Integer.parseInt(valuePA.getText().replaceAll("[₱,]", ""));
            Assert.assertTrue(convertValuePA >= Integer.parseInt(minText) && convertValuePA <= Integer.parseInt(maxText));
        }
    }

    @Test
    public void verifySortFunction() throws InterruptedException {
        List<WebElement> options = driver.findElements(By.cssSelector("div[data-gb-name='sort-option']>input"));
        for (WebElement option : options) {
            String value = option.getAttribute("value");
            if (value.equals(sortBy)) {
                WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript(
                        "return arguments[0].parentNode;", option);
                parent.click();
            }
        }
        Thread.sleep(3000);
        List attributeList = new ArrayList();
        List<WebElement> cards = driver.findElements(By.cssSelector(".card-wrapper"));
        for (WebElement card : cards) {
            String valueCard = card.getAttribute("data-insuer-name");
            attributeList.add(valueCard);
        }
        ArrayList tempList = new ArrayList(attributeList);
        Collections.sort(tempList,String.CASE_INSENSITIVE_ORDER);
        Assert.assertTrue(attributeList.equals(tempList));
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
