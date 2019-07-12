package pages;

import commons.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class InsurancePage extends AbstractPage {
    WebDriver driver;
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

    @FindBy(name = "dates-startdate")
    private WebElement startDatePicker;
    @FindBy(name = "dates-enddate")
    private WebElement endDatePicker;
    @FindBy(css = ".day")
    private List<WebElement> dayList;
    @FindBy(css = ".datepicker-days .datepicker-switch")
    private WebElement currentMonthYear;
    @FindBy(css = ".active.selected.range-start.day")
    private WebElement activeStartDay;
    @FindBy(css = ".active.selected.range-end.day")
    private WebElement activeEndDay;
    @FindBy(css = ".datepicker-days .today")
    private WebElement todayPicker;

    public InsurancePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        waitForLoadingGone(driver, loadingIcon);
    }

    public String[] getResultText() {
        String[] arrResultText = getText(driver, resultText).split(" ");
        return arrResultText;
    }

    public void selectPromotionFilter(String promotion) throws InterruptedException {
        loopAndClickByAttribute(driver, promotionFilterRadioList, promotion, "data-filter-name");
        Thread.sleep(2000);
        waitForLoadingGone(driver, loadingIcon);
    }

    public void selectInsuranceFilter(String insurer) throws InterruptedException {
        loopAndClickByAttribute(driver, insurersFilterCbList, insurer, "data-filter-name");
        Thread.sleep(2000);
        waitForLoadingGone(driver, loadingIcon);
    }

    public void selectMedicalRangeFilter(int xOffset) throws InterruptedException {
        clickOn(driver, seeMoreBtn);
        int x = minMedicalSlider.getSize().width;
        Actions act = new Actions(driver);
        act.dragAndDropBy(minMedicalSlider, xOffset, 0).build().perform();
        Thread.sleep(2000);
        waitForLoadingGone(driver, loadingIcon);
    }

    public int getMinMedical() {
        String minText = getAttribute(driver, minMedicalText, "data-min-value");
        return Integer.parseInt(minText);
    }

    public int getMaxMedical() {
        String maxText = getAttribute(driver, maxMedicalText, "data-max-value");
        return Integer.parseInt(maxText);
    }

    public void selectSortFilter(String sortBy) throws InterruptedException {
        loopAndClickByAttribute(driver, sortRadioList, sortBy, "value");
        Thread.sleep(2000);
        waitForLoadingGone(driver, loadingIcon);
    }

    public void selectPolicyTypeFilter(String policyType) throws InterruptedException {
        loopAndClickByAttribute(driver, tripTypeRadioList, policyType, "data-gb-trip-types");
        Thread.sleep(2000);
        waitForLoadingGone(driver, loadingIcon);
    }

    public void selectWhosGoingFilter(String traveller) throws InterruptedException {
        loopAndClickByAttribute(driver, travellerRadioList, traveller, "value");
        Thread.sleep(2000);
        waitForLoadingGone(driver, loadingIcon);
    }

    public void selectDestinationFilter(String country) throws InterruptedException {
        clickOn(driver, destinationBox);
        loopAndClickByText(driver, destinationList, country);
        Thread.sleep(2000);
        waitForLoadingGone(driver, loadingIcon);
    }

    public List<String> getAttributeList(String attribute) {
        List<String> attributeList = new ArrayList();
        for (WebElement card : cardList) {
            String valueCard = card.getAttribute(attribute);
            attributeList.add(valueCard);
        }
        return attributeList;
    }

    public List<String> getMedicalCardList() {
        List<String> convertedValueMedicalList = new ArrayList();
        for (WebElement valueMedicalCard : valueMedicalCards) {
            String convertedValueMedical = valueMedicalCard.getText().replaceAll("[₱,]", "");
            convertedValueMedicalList.add(convertedValueMedical);
        }
        return convertedValueMedicalList;
    }

    public String[] getNavBarText() {
        String navBarText = getText(driver, navBar);
        String[] navBarArray = navBarText.split("\\|");
        return navBarArray;

    }

    public String getSelectedDestination() {
        String selected = getAttribute(driver, selectedDestination, "data-gb-destination");
        return selected;
    }
}
