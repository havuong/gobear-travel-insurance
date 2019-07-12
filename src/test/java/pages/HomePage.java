package pages;

import commons.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends AbstractPage {
    WebDriver driver;
    @FindBy(css = "li[data-gb-name='Insurance']")
    private WebElement insuranceTab;
    @FindBy(css = "li[data-gb-name='Travel']")
    private WebElement travelTab;
    @FindBy(name = "product-form-submit")
    private WebElement submitBtn;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickOnInsuranceTab() {
        waitForClickable(driver, insuranceTab);
        clickOn(driver, insuranceTab);
    }

    public void clickOnTravelTab() {
        waitForClickable(driver, travelTab);
        clickOn(driver, travelTab);
    }

    public void clickOnSubmitBtn() {
        waitForClickable(driver, submitBtn);
        clickOn(driver, submitBtn);
    }
}
