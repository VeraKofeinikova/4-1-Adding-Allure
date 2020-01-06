import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class Tests2_3 {
    @BeforeAll
    public static void clearCookies() {
        open("http://localhost:9999");
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    public static String datePresentAndFuture(int x) {
        String datePresentAndFuture = LocalDate.now().plusDays(x).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return datePresentAndFuture;
    }

    public static String dateInPast(int y) {
        String dateInPast = LocalDate.now().minusDays(y).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return dateInPast;
    }

    private Faker faker;
    @BeforeEach
    void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }

    public static final String[] strings = {
            "Элиста",
            "Красноярск",
            "Ижевск",
            "Москва",
            "Краснодар",
            "Салехард",
            "Тверь",
            "Ульяновск"
    };
    Random random = new Random();
    String randomCity = strings[random.nextInt(strings.length)];

    @Test
    @DisplayName("Дата в прошлом не является валидной для даты встречи")
    void shouldWarningIfDateInPast() {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(dateInPast(5));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @Test
    @DisplayName("Сегодняшнее число не является валидным для даты встречи")
    void shouldWarningIfCurrentDate() {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(0));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @Test
    @DisplayName("Завтра не является валидной датой для даты встречи")
    void shouldWarningIfCurrentDatePlusOneDay() {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(1));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля города на невалидные значения")
    @CsvFileSource(resources = "/VariantsOfCity.csv", numLinesToSkip = 1)
    void shouldWarningIfWrongTypeOfCity(String variantsOfCity) {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(variantsOfCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=city]").shouldHave(Condition.cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля имени на невалидные значения")
    @CsvFileSource(resources = "/VariantsOfName.csv", numLinesToSkip = 1)
    void shouldWarningIfWrongTypeOfLettersInName(String variantsOfName) {
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue(variantsOfName);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=name]").shouldHave(Condition.cssClass("input_invalid"));
    }

    //имя захардкоржено намеренно
    @Test
    @DisplayName("Happy Path с буквой Ё в имени - должно заканчиваться успешным планированием и перепланированием встречи")
    void shouldBookTimeSuccessfully() {
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue("Семён Петров");
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);
    }

    @Test
    @DisplayName("Проверка НЕнажатой галочки в чекбоксе")
    void shouldWarningIfDontClickCheckbox() {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=agreement]").shouldHave(Condition.cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля телефона - реформатирование неправильного введенного ПОЛНОГО номера и успешное бронирование времени")
    @CsvFileSource(resources = "/variantsOfPhoneReformatAndPositiveEnd.csv", numLinesToSkip = 1)
    void shouldReformatWrongButFullPhone(String variantsOfPhone) {
        String name = faker.name().fullName();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(variantsOfPhone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);
    }

    // метод для получения рандомной даты переназначения встречи
    // получаем случайное число в диапазоне от 4 до 30 и
    // прибавляем его к нынешней дате
    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    @Test
    @DisplayName("Happy Path. Перепланирование встречи")
    void shouldBookAndReBookTimeSuccessfully() {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);

        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(rnd(4,30)));
        form.$(By.className("button_theme_alfa-on-white")).click();

        $("[data-test-id=replan-notification]").waitUntil(visible,1000);
        $(byText("Перепланировать")).click();

        $("[data-test-id=success-notification]").waitUntil(visible, 15000);
    }

    @Test
    @DisplayName("Перепланирование останавливается, если дата перепланирования назначена на прошлое")
    void shouldBookSuccessfullyAndWarnIfSecondDateInPast() {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        $("[data-test-id=success-notification]").waitUntil(visible, 15000);

        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(dateInPast(5));
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$(By.className("calendar-input__custom-control")).shouldHave(cssClass("input_invalid"));
    }

    @ParameterizedTest
    @DisplayName("Проверка поля телефона - должен выдавать предупреждение о неправильно введенном номере, если номер введен не до конца")
    @CsvFileSource(resources = "/variantsOfPhoneShouldFail.csv", numLinesToSkip = 1)
    void shouldWarningIfPhoneNotFullyEntered(String variantsOfPhone) {
        String name = faker.name().fullName();

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(randomCity);
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[data-test-id=date] input").setValue(datePresentAndFuture(3));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(variantsOfPhone);
        form.$("[data-test-id=agreement]").click();
        form.$(By.className("button_theme_alfa-on-white")).click();
        form.$("[data-test-id=phone]").shouldHave(Condition.cssClass("input_invalid"));
    }
}