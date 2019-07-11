import commons.AbstractTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePage extends AbstractTest {
    WebDriver driver;
    String insurer = "Standard Insurance";
    String sortBy = "insurerName-Asc";
    String policyType = "annual";
    String traveller = "2";
    String country = "Singapore";

    @FindBy(css = "li[data-gb-name='Insurance']")
    private WebElement insuranceTab;
    @FindBy(css = "li[data-gb-name='Travel']")
    private WebElement travelTab;
    @FindBy(name = "product-form-submit")
    private WebElement submitBtn;

    @FindBy(css = "div[data-gb-name='loading-status']")
    private WebElement loadingIcon;
    @FindBy(css = "div[data-gb-name='travel-nav-data']>h5")
    private WebElement resultText;
    @FindBy(css = ".checkbox.checkbox-primary")
    private List<WebElement> InsurersFilterCbs;
    @FindBy(css = ".card-wrapper")
    private List<WebElement> cardList;

    @FindBy(css = "#collapseSeemoreBtn")
    private WebElement seeMoreBtn;
    @FindBy(xpath = "(//div[@class='slider-handle min-slider-handle round'])[2]")
    private WebElement minMedicalSlider;
    @FindBy(css = "#gb-slider-2 + b")
    private WebElement minMedicalText;
    @FindBy(css = "#gb-slider-2 + b + b")
    private WebElement maxMedicalText;
    @FindBy(xpath = "//p[text()='Medical expenses while traveling']/following-sibling::p/descendant::span")
    private List<WebElement> valueMedicalCards;

    @FindBy(css = "div[data-gb-name='sort-option']>input")
    private List<WebElement> sortRadioList;

    @BeforeClass
    public void beforeClass() {
        driver = openBrowser();
        PageFactory.initElements(driver, this);

        clickOn(insuranceTab);
        clickOn(travelTab);

        waitForClickable(submitBtn);
        clickOn(submitBtn);
        waitForLoadingGone(loadingIcon);
    }

    @Test
    public void verifyAtLeast3CardsDisplayed() {
        String arrResultText[] = getTexts(resultText).split(" ");
        Assert.assertTrue(2 < Integer.parseInt(arrResultText[0]));
    }

    @Test
    public void verifyPromotionsFilterFunction() {
    }

    @Test
    public void verifyInsurersFilterFunction() throws InterruptedException {
        for (WebElement InsurersFilterCb : InsurersFilterCbs) {
            String value = getAttribute(InsurersFilterCb,"data-filter-name");

            if (value.equals(insurer)) {
                clickOn(InsurersFilterCb);
            }
        }
        Thread.sleep(2000);
        waitForLoadingGone(loadingIcon);
        for (WebElement card : cardList) {
            String valueCard = card.getAttribute("data-insuer-name");
            Assert.assertTrue(valueCard.equals(insurer));
        }
    }

    @Test
    public void verifyRangeFilterFunction() throws InterruptedException {
        clickOn(seeMoreBtn);
        int x = minMedicalSlider.getSize().width;
        Actions act = new Actions(driver);
        act.dragAndDropBy(minMedicalSlider, 20, 0).build().perform();

        String minText = getAttribute(minMedicalText,"data-min-value");
        String maxText = getAttribute(maxMedicalText,"data-max-value");

        Thread.sleep(2000);
        waitForLoadingGone(loadingIcon);

        for (WebElement valueMedicalCard : valueMedicalCards) {
            int convertedValueMedical = Integer.parseInt(valueMedicalCard.getText().replaceAll("[₱,]", ""));
            Assert.assertTrue(convertedValueMedical >= Integer.parseInt(minText) && convertedValueMedical <= Integer.parseInt(maxText));
        }
    }

    @Test
    public void verifySortFunction() throws InterruptedException {
        for (WebElement sortRadio : sortRadioList) {
            String value = getAttribute(sortRadio,"value");
            if (value.equals(sortBy)) {
                clickParentByJS(sortRadio);
            }
        }
        Thread.sleep(2000);
        waitForLoadingGone(loadingIcon);
        List attributeList = new ArrayList();
        for (WebElement card : cardList) {
            String valueCard =  getAttribute(card,"data-insuer-name");
            attributeList.add(valueCard);
        }
        ArrayList tempList = new ArrayList(attributeList);
        Collections.sort(tempList, String.CASE_INSENSITIVE_ORDER);
        Assert.assertTrue(attributeList.equals(tempList));
    }

    @Test
    public void verifyPolicyTypeDetailsFunction() throws InterruptedException {
        List<WebElement> options = driver.findElements(By.cssSelector("div[data-gb-name='triptype'] .radio.radio-primary"));
        for (WebElement option : options) {
            String value = option.getAttribute("data-gb-trip-types");
            if (value.equals(policyType)) {
                option.click();
            }
        }
        Thread.sleep(3000);

        String navData = driver.findElement(By.cssSelector("div[data-gb-name='travel-nav-data'] small")).getText();
        String navArray[] = navData.split("\\|");
        Assert.assertTrue(navArray[0].startsWith(policyType));
    }

    @Test
    public void verifyWhosGoingDetailsFunction() throws InterruptedException {
        List<WebElement> options = driver.findElements(By.cssSelector("div[data-gb-name='traveller'] .radio.radio-primary>input"));
        for (WebElement option : options) {
            String value = option.getAttribute("value");
            if (value.equals(traveller)) {
                WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript(
                        "return arguments[0].parentNode;", option);
                parent.click();
            }
        }
        Thread.sleep(3000);

        String navData = driver.findElement(By.cssSelector("div[data-gb-name='travel-nav-data'] small")).getText();
        String navArray[] = navData.split("\\|");
        Assert.assertTrue(navArray[1].contains(traveller));
    }

    @Test
    public void verifyDesnitationDetailsFunction() throws InterruptedException {
        driver.findElement(By.cssSelector(".select-component")).click();
        List<WebElement> options = driver.findElements(By.cssSelector(".dropdown-menu.open span"));
        for (WebElement option : options) {
            String value = option.getText();
            if (value.equals(country)) {
                WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript(
                        "return arguments[0].parentNode;", option);
                parent.click();
            }
        }
        Thread.sleep(3000);
        String selected = driver.findElement(By.cssSelector("div[data-gb-name='destinations']>div>div")).getAttribute("data-gb-destination");
        Assert.assertEquals(selected, country);
    }

    @Test
    public void verifyTravelDateDetailsFunction() {
    }

    @AfterClass
    public void afterClass() {
        if (driver != null) {
            driver.quit();
        }
    }
}
