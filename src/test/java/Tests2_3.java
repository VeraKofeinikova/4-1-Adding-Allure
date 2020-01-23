import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class Tests2_3 {
    @BeforeEach
    public void clearCookies() {
        open("http://localhost:9999");
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Дата в прошлом не является валидной для даты встречи")
    void shouldWarningIfDateInPast() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPastMinusDays(5));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @Test
    @DisplayName("Сегодняшнее число не является валидным для даты встречи")
    void shouldWarningIfCurrentDate() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(0));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @Test
    @DisplayName("Завтра не является валидной датой для даты встречи")
    void shouldWarningIfCurrentDatePlusOneDay() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(1));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля города на невалидные значения")
    @CsvFileSource(resources = "/VariantsOfCity.csv", numLinesToSkip = 1)
    void shouldWarningIfWrongTypeOfCity(String variantsOfCity) {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(variantsOfCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=city]").shouldHave(Condition.cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля имени на невалидные значения")
    @CsvFileSource(resources = "/VariantsOfName.csv", numLinesToSkip = 1)
    void shouldWarningIfWrongTypeOfLettersInName(String variantsOfName) {


        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(variantsOfName);
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=name]").shouldHave(Condition.cssClass("input_invalid"));
    }

    @Test
    @DisplayName("Happy Path с буквой Ё в имени - должно заканчиваться успешным планированием и перепланированием встречи")
    void shouldBookTimeSuccessfully() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(GenerateData.nameWithE());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);
    }

    @Test
    @DisplayName("Проверка НЕнажатой галочки в чекбоксе")
    void shouldWarningIfDontClickCheckbox() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=agreement]").shouldHave(Condition.cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля телефона - реформатирование неправильного введенного ПОЛНОГО номера и успешное бронирование времени")
    @CsvFileSource(resources = "/variantsOfPhoneReformatAndPositiveEnd.csv", numLinesToSkip = 1)
    void shouldReformatWrongButFullPhone(String variantsOfPhone) {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(variantsOfPhone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);
    }

    @Test
    @DisplayName("Happy Path. Перепланирование встречи")
    void shouldBookAndReBookTimeSuccessfully() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);

        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(GenerateData.rnd(4,30)));
        form.$(By.className("button_theme_alfa-on-white")).click();

        $("[data-test-id=replan-notification]").waitUntil(visible,1000);
        $(byText("Перепланировать")).click();

        $("[data-test-id=success-notification]").waitUntil(visible, 15000);
    }

    @Test
    @DisplayName("Перепланирование останавливается, если дата перепланирования назначена на прошлое")
    void shouldBookSuccessfullyAndWarnIfSecondDateInPast() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(GenerateData.getRandomPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);

        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPastMinusDays(5));
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля телефона - должен выдавать предупреждение о неправильно введенном номере, если номер введен не до конца")
    @CsvFileSource(resources = "/variantsOfPhoneShouldFail.csv", numLinesToSkip = 1)
    void shouldWarningIfPhoneNotFullyEntered(String variantsOfPhone) {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(GenerateData.getRandomCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(GenerateData.dateInPresentOrInFuturePlusDays(3));
        form.$("[data-test-id=name] input").setValue(GenerateData.getRandomName());
        form.$("[data-test-id=phone] input").setValue(variantsOfPhone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=phone]").shouldHave(Condition.cssClass("input_invalid"));
    }
}