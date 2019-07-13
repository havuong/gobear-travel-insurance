package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;

public class DatePicker {
    private static final String DATE_FORMAT = "dd MMMM yyyy";
    private static final String DAY_FIRST = "01";
    private static final String SPACE = " ";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @FindBy(css = ".day")
    private List<WebElement> dates;
    @FindBy(css = ".datepicker-days .prev")
    private WebElement prev;
    @FindBy(css = ".datepicker-days .datepicker-switch")
    private WebElement curDate;
    @FindBy(css = ".datepicker-days .prev")
    private WebElement next;

    public DatePicker(final WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void setDate(String date) {

        long diff = this.getDateDifferenceInMonths(date);
        int day = this.getDay(date);

        WebElement arrow = (diff >= 0 ? next : prev);
        diff = Math.abs(diff);

        //click the arrows
        LongStream.range(0, diff)
                .forEach(i -> arrow.click());

        //select the date
        dates.stream()
                .filter(ele -> Integer.parseInt(ele.getText()) == day)
                .findFirst()
                .ifPresent(ele -> ele.click());
    }

    private int getDay(String date) {
        LocalDate dpToDate = LocalDate.parse(date, DTF);
        return dpToDate.getDayOfMonth();
    }

    private long getDateDifferenceInMonths(String date) {
        LocalDate dpCurDate = LocalDate.parse(DAY_FIRST + SPACE + this.getCurrentMonthFromDatePicker(), DTF);
        LocalDate dpToDate = LocalDate.parse(date, DTF);
        return YearMonth.from(dpCurDate).until(dpToDate, ChronoUnit.MONTHS);
    }

    private String getCurrentMonthFromDatePicker() {
        return this.curDate.getText();
    }
}
