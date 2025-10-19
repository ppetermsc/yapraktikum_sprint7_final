package data;

import java.util.Arrays;
import java.util.List;
import com.github.javafaker.Faker;

public class TestData {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final Faker user = new Faker();

    // Для курьеров
    public static String getLogin() { return user.name().lastName() + user.regexify("[0-9]{4}"); }
    public static String getPassword() { return user.regexify("[0-9]{4}"); }
    public static String getFirstName() { return user.name().firstName(); }

    // Для заказов
    public static String getOrderFirstName() { return user.name().firstName(); }
    public static String getOrderLastName() { return user.name().lastName(); }
    public static String getOrderAddress() { return user.address().fullAddress(); }
    public static int getOrderMetroStation() { return user.number().numberBetween(1, 20); }
    public static String getOrderPhone() { return "+7" + user.regexify("[0-9]{10}"); }
    public static String getOrderRentTime() { return String.valueOf(user.number().numberBetween(1, 10)); }
    public static String getOrderDeliveryDate() {
        return "2024-" + String.format("%02d", user.number().numberBetween(1, 12)) + "-" +
                String.format("%02d", user.number().numberBetween(1, 28));
    }
    public static String getOrderComment() { return user.lorem().sentence(3, 5); }

    // Цвета для параметризации
    public static List<List<String>> getColorCombinations() {
        return Arrays.asList(
                Arrays.asList("BLACK"),
                Arrays.asList("GREY"),
                Arrays.asList("BLACK", "GREY"),
                null,
                Arrays.asList()
        );
    }

    // Метод для параметризованных тестов (преобразуем в Object[][])
    public static Object[][] getOrderColorData() {
        List<List<String>> colorCombinations = getColorCombinations();
        Object[][] data = new Object[colorCombinations.size()][1];
        for (int i = 0; i < colorCombinations.size(); i++) {
            data[i][0] = colorCombinations.get(i);
        }
        return data;
    }

    // Дефолтные значения для заказов
    public static class OrderDefaults {
        public static final String FIRST_NAME = "Иван";
        public static final String LAST_NAME = "Петров";
        public static final String ADDRESS = "Москва, ул. Ленина, 1";
        public static final int METRO_STATION = 5;
        public static final String PHONE = "+79991234567";
        public static final String RENT_TIME = "3";
        public static final String DELIVERY_DATE = "2024-01-15";
        public static final String COMMENT = "Тестовый заказ";
    }
}
