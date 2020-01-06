import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestsFrom2_2 {

    @BeforeAll
    public static void clearCookies() {
        open("http://localhost:9999");
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    //проверка на прошлое
    @Test
    void shouldWarningIfDateInPast() {
        String tenDaysAgo = LocalDate.now().minusDays(10).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Красноярск");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(tenDaysAgo);
        form.$("[data-test-id=name] input").setValue("Иван Петров");
        form.$("[data-test-id=phone] input").setValue("+79012345681");
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    //проверка на сегодняшнюю дату
    @Test
    void shouldWarningIfCurrentDate() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Красноярск");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(today);
        form.$("[data-test-id=name] input").setValue("Иван Петров");
        form.$("[data-test-id=phone] input").setValue("+79012345681");
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    // проверка на сегодняшнюю дату + 1 день
    @Test
    void shouldWarningIfCurrentDatePlusOneDay() {
        String currentDatePlusOneDay = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Красноярск");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(String.valueOf(currentDatePlusOneDay));
        form.$("[data-test-id=name] input").setValue("Иван Петров");
        form.$("[data-test-id=phone] input").setValue("+79012345681");
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }


    // Проверка города
    @ParameterizedTest
    @CsvFileSource(resources = "/VariantsOfCity.csv", numLinesToSkip = 1)
    void shouldWarningIfWrongTypeOfCity(String variantsOfCity) {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(variantsOfCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(date);
        form.$("[data-test-id=name] input").setValue("Иван Петров");
        form.$("[data-test-id=phone] input").setValue("+79012345681");
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=city]").shouldHave(Condition.cssClass("input_invalid"));
    }


    // Проверка имени
    @ParameterizedTest
    @CsvFileSource(resources = "/VariantsOfName.csv", numLinesToSkip = 1)
    void shouldWarningIfWrongTypeOfLettersInName(String variantsOfName) {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Красноярск");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(date);
        form.$("[data-test-id=name] input").setValue(variantsOfName);
        form.$("[data-test-id=phone] input").setValue("+79012345681");
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=name]").shouldHave(Condition.cssClass("input_invalid"));
    }

    // Проверка телефона
    @ParameterizedTest
    @CsvFileSource(resources = "/VariantsOfPhone.csv", numLinesToSkip = 1)
    void shouldWarningIfNotNumbersInPhone(String variantsOfPhone) {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Красноярск");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(date);
        form.$("[data-test-id=name] input").setValue("Иван Петров");
        form.$("[data-test-id=phone] input").setValue(variantsOfPhone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=phone]").shouldHave(Condition.cssClass("input_invalid"));
    }

    // Проверка нажатой галочки в чекбоксе
    @Test
    void shouldWarningIfDontClickCheckbox() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Красноярск");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(date);
        form.$("[data-test-id=name] input").setValue("Иван Петров");
        form.$("[data-test-id=phone] input").setValue("+79012345681");
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=agreement]").shouldHave(Condition.cssClass("input_invalid"));
    }
}