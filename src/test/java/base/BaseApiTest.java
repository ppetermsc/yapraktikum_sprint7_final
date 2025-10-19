package base;

import data.TestData;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import java.util.ArrayList;
import java.util.List;

public class BaseApiTest {

    protected static List<String> couriersToDelete = new ArrayList<>();
    protected static List<Integer> ordersToCancel = new ArrayList<>();  // Добавил для заказов

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @AfterClass
    public static void cleanUp() {
        // Очистка курьеров
        int totalCouriers = couriersToDelete.size();
        System.out.println("Начинается очистка тестовых данных... Найдено курьеров: " + totalCouriers);

        for (String courierLogin : couriersToDelete) {
            try {
                System.out.println("Удаляем курьера: " + courierLogin);
                // TODO: Добавить реальное удаление через API
            } catch (Exception e) {
                System.out.println("Ошибка при удалении курьера " + courierLogin + ": " + e.getMessage());
            }
        }

        // Очистка заказов
        int totalOrders = ordersToCancel.size();
        System.out.println("Найдено заказов для отмены: " + totalOrders);

        for (Integer track : ordersToCancel) {
            try {
                System.out.println("Отменяем заказ с track: " + track);
                // TODO: Добавить реальную отмену через API
            } catch (Exception e) {
                System.out.println("Ошибка при отмене заказа " + track + ": " + e.getMessage());
            }
        }

        couriersToDelete.clear();
        ordersToCancel.clear();
        System.out.println("Очистка завершена. Удалено курьеров: " + totalCouriers + ", отменено заказов: " + totalOrders);
    }

    public static void addCourierForCleanup(String courierLogin) {
        couriersToDelete.add(courierLogin);
        System.out.println("Добавлен курьер для очистки: " + courierLogin);
    }

    public static void addOrderForCleanup(int track) {
        ordersToCancel.add(track);
        System.out.println("Добавлен заказ для очистки с track: " + track);
    }
}