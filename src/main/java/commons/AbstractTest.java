package commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.TimeUnit;

public class AbstractTest {
    WebDriver driver;

    @BeforeMethod
    public WebDriver openBrowser() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

            driver.get("https://www.gobear.com/ph?x_session_type=UAT");
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            return driver;
        }
        return driver;
    }

    @AfterTest(alwaysRun = true)
//    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }


}
