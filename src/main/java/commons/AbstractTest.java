package commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class AbstractTest {
    private WebDriver driver;
    private long waitTime = 15;

    public WebDriver openBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://www.gobear.com/ph?x_session_type=UAT");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        return driver;
    }

    public void waitForVisibilityOf(WebElement element) {
        new WebDriverWait(driver, waitTime).until(visibilityOf(element));
    }

    public void waitForClickable(WebElement element) {
        new WebDriverWait(driver, waitTime).until(elementToBeClickable(element));
    }

    public void waitForAttributeToBe(WebElement element, String attribute, String value) {
        new WebDriverWait(driver, waitTime).until(attributeToBe(element, attribute, value));
    }

    public void clickOn(WebElement element) {
        waitForClickable(element);
        element.click();
    }

    public String getTexts(WebElement element) {
        waitForVisibilityOf(element);
        return element.getText();
    }
}