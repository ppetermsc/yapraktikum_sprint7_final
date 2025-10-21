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

    // Данные для параметризованных тестов заказов
    public static Object[][] getOrderColorData() {
        return new Object[][]{
                {Arrays.asList("BLACK"), "создание заказа с черным цветом"},
                {Arrays.asList("GREY"), "создание заказа с серым цветом"},
                {Arrays.asList("BLACK", "GREY"), "создание заказа с обоими цветами"},
                {null, "создание заказа без указания цвета"},
                {Arrays.asList(), "создание заказа с пустым списком цветов"}
        };
    }

    // Дефолтные значения для заказов
    public static final String ORDER_FIRST_NAME = "Иван";
    public static final String ORDER_LAST_NAME = "Петров";
    public static final String ORDER_ADDRESS = "Москва, ул. Ленина, 1";
    public static final int ORDER_METRO_STATION = 5;
    public static final String ORDER_PHONE = "+79991234567";
    public static final String ORDER_RENT_TIME = "3";
    public static final String ORDER_DELIVERY_DATE = "2024-01-15";
    public static final String ORDER_COMMENT = "Тестовый заказ";
}
