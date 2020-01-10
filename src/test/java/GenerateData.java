import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class GenerateData {

    public static String getRandomName() {
        Faker faker;
        faker = new Faker(new Locale("ru"));
        String name = faker.name().fullName();
        return name;
    }

    public static String getRandomPhone() {
        Faker faker;
        faker = new Faker(new Locale("ru"));
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static String getRandomCity() {
        final String[] strings = {
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

        return randomCity;
    }

    public static String dateInPresentOrInFuturePlusDays(int x) {
        String dateInPresentOrInFuturePlusDays = LocalDate.now().plusDays(x).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return dateInPresentOrInFuturePlusDays;
    }

    public static String dateInPastMinusDays(int y) {
        String dateInPastMinusDays = LocalDate.now().minusDays(y).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return dateInPastMinusDays;
    }

    //захардкорженное имя с буквой Ё
    public static String nameWithE() {
        String nameWithE = "Семён Петров";
        return nameWithE;
    }

    // метод для получения рандомной даты переназначения встречи
    // получаем случайное число в диапазоне от 4 до 30 и
    // прибавляем его к нынешней дате
    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
