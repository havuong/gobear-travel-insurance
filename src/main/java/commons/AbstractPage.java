package commons;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class AbstractPage {
    private WebDriver driver;
    private long waitTime = 15;

    public void waitForVisibilityOf(WebDriver driver, WebElement element) {
        new WebDriverWait(driver, waitTime).until(visibilityOf(element));
    }

    public void waitForClickable(WebDriver driver, WebElement element) {
        new WebDriverWait(driver, waitTime).until(elementToBeClickable(element));
    }

    public void waitForLoadingGone(WebDriver driver, WebElement element) {
        new WebDriverWait(driver, waitTime).until(attributeToBe(element, "style", "display: none;"));
    }

    public void waitForAttributeToBe(WebDriver driver, WebElement element, String attribute, String value) {
        new WebDriverWait(driver, waitTime).until(attributeToBe(element, attribute, value));
    }

    public void clickOn(WebDriver driver, WebElement element) {
        try {
            element.click();
        } catch (TimeoutException | ElementClickInterceptedException e) {
            WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].parentNode;", element);
            parent.click();
        }
    }

    public String getText(WebDriver driver, WebElement element) {
        return element.getText();
    }

    public String getAttribute(WebDriver driver, WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    public void loopAndClickByAttribute(WebDriver driver, List<WebElement> list, String expect, String attribute) {
        for (WebElement element : list) {
            String value = getAttribute(driver, element, attribute);

            if (value.equals(expect)) {
                clickOn(driver, element);
                break;
            }
        }
    }

    public void loopAndClickByText(WebDriver driver, List<WebElement> list, String text) {
        for (WebElement element : list) {
            String value = getText(driver, element);

            if (value.equals(text)) {
                clickOn(driver, element);
                break;
            }
        }
    }
}
