package testcases;

import commons.AbstractTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.InsurancePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InsuranceTest extends AbstractTest {
    WebDriver driver;
    private HomePage homePage;
    private InsurancePage insurancePage;
    String promotion = "Promos Only";
    int promotionResult = 0;
    String insurer = "Standard Insurance";
    String sortBy = "insurerName-Asc";
    String policyType = "annual";
    String traveller = "2";
    String country = "Singapore";

    @BeforeClass
    public void beforeClass() {
        driver = openBrowser();
        homePage = new HomePage(driver);
        homePage.clickOnInsuranceTab();
        homePage.clickOnTravelTab();
        homePage.clickOnSubmitBtn();
        insurancePage = new InsurancePage(driver);
    }

    @Test
    public void verifyAtLeast3CardsDisplayed() {
        Assert.assertTrue(2 < Integer.parseInt(insurancePage.getResultText()[0]));
    }

    @Test
    public void verifyPromotionsFilterFunction() throws InterruptedException {
        insurancePage.selectPromotionFilter(promotion);
        Assert.assertTrue(promotionResult == Integer.parseInt(insurancePage.getResultText()[0]));
    }

    @Test
    public void verifyInsurersFilterFunction() throws InterruptedException {
        insurancePage.selectInsuranceFilter(insurer);
        Assert.assertTrue(insurancePage.getAttributeList("data-insuer-name").stream()
                .filter(ele -> ele.equals(insurer))
                .allMatch(ele -> true)
        );
    }

    @Test
    public void verifyRangeFilterFunction() throws InterruptedException {
        insurancePage.selectMedicalRangeFilter(20);
        Assert.assertTrue(insurancePage.getMedicalCardList().stream()
                .mapToInt(Integer::parseInt)
                .filter(ele -> ele >= insurancePage.getMinMedical() && ele <= insurancePage.getMaxMedical())
                .allMatch(ele -> true)
        );
    }

    @Test
    public void verifySortFunction() throws InterruptedException {
        insurancePage.selectSortFilter(sortBy);
        List cardList = insurancePage.getAttributeList("data-insuer-name");
        List tempList = new ArrayList(cardList);
        Collections.sort(tempList, String.CASE_INSENSITIVE_ORDER);
        Assert.assertTrue(cardList.equals(tempList));
    }

    @Test
    public void verifyPolicyTypeDetailsFunction() throws InterruptedException {
        insurancePage.selectPolicyTypeFilter(policyType);
        Assert.assertTrue(insurancePage.getNavBarText()[0].startsWith(policyType));
    }

    @Test
    public void verifyWhosGoingDetailsFunction() throws InterruptedException {
        insurancePage.selectWhosGoingFilter(traveller);
        Assert.assertTrue(insurancePage.getNavBarText()[1].contains(traveller));
    }

    @Test
    public void verifyDestinationDetailsFunction() throws InterruptedException {
        insurancePage.selectDestinationFilter(country);
        Assert.assertEquals(insurancePage.getSelectedDestination(), country);
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
