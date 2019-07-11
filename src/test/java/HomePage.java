import commons.AbstractTest;
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
    String promotion = "Promos Only";
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
    @FindBy(css = "div.radio.radio-primary[data-gb-name='filter-option']")
    private List<WebElement> promotionFilterRadioList;
    @FindBy(css = ".checkbox.checkbox-primary")
    private List<WebElement> insurersFilterCbList;
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

    @FindBy(css = "div[data-gb-name='triptype'] .radio.radio-primary")
    private List<WebElement> tripTypeRadioList;
    @FindBy(css = "div[data-gb-name='travel-nav-data'] small")
    private WebElement navBar;

    @FindBy(css = "div[data-gb-name='traveller'] .radio.radio-primary>input")
    private List<WebElement> travellerRadioList;

    @FindBy(css = ".select-component")
    private WebElement destinationBox;
    @FindBy(css = ".dropdown-menu.open span")
    private List<WebElement> destinationList;
    @FindBy(css = "div[data-gb-name='destinations']>div>div")
    private WebElement selectedDestination;

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
        String arrResultText[] = getText(resultText).split(" ");
        Assert.assertTrue(2 < Integer.parseInt(arrResultText[0]));
    }

    @Test
    public void verifyPromotionsFilterFunction() throws InterruptedException {
        for (WebElement promotionFilterRadio : promotionFilterRadioList) {
            String value = getAttribute(promotionFilterRadio,"data-filter-name");

            if (value.equals(promotion)) {
                clickOn(promotionFilterRadio);
                break;
            }
        }
        Thread.sleep(2000);
        waitForLoadingGone(loadingIcon);

        String arrResultText[] = getText(resultText).split(" ");
        Assert.assertTrue(0 == Integer.parseInt(arrResultText[0]));
    }

    @Test
    public void verifyInsurersFilterFunction() throws InterruptedException {
        for (WebElement InsurersFilterCb : insurersFilterCbList) {
            String value = getAttribute(InsurersFilterCb,"data-filter-name");

            if (value.equals(insurer)) {
                clickOn(InsurersFilterCb);
                break;
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
                break;
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
        for (WebElement tripTypeRadio : tripTypeRadioList) {
            String value = getAttribute(tripTypeRadio,"data-gb-trip-types");
            if (value.equals(policyType)) {
                tripTypeRadio.click();
                break;
            }
        }
        Thread.sleep(2000);
        waitForLoadingGone(loadingIcon);

        String navBarText = getText(navBar);
        String navBarArray[] = navBarText.split("\\|");
        Assert.assertTrue(navBarArray[0].startsWith(policyType));
    }

    @Test
    public void verifyWhosGoingDetailsFunction() throws InterruptedException {
        for (WebElement travellerRadio : travellerRadioList) {
            String value = getAttribute(travellerRadio,"value");
            if (value.equals(traveller)) {
                clickParentByJS(travellerRadio);
                break;
            }
        }
        Thread.sleep(2000);
        waitForLoadingGone(loadingIcon);

        String navBarText = getText(navBar);
        String navBarArray[] = navBarText.split("\\|");
        Assert.assertTrue(navBarArray[1].contains(traveller));
    }

    @Test
    public void verifyDesnitationDetailsFunction() throws InterruptedException {
        clickOn(destinationBox);
        for (WebElement destination : destinationList) {
            String value = getText(destination);
            if (value.equals(country)) {
                clickParentByJS(destination);
                break;
            }
        }
        Thread.sleep(2000);
        waitForLoadingGone(loadingIcon);

        String selected = getAttribute(selectedDestination,"data-gb-destination");
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
