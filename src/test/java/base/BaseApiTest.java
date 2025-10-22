package base;

import data.TestData;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import steps.CourierSteps;
import steps.OrderApi;

import java.util.ArrayList;
import java.util.List;

public class BaseApiTest {

    protected static List<String> couriersToDelete = new ArrayList<>();
    protected static List<Integer> ordersToCancel = new ArrayList<>();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @AfterClass
    public static void cleanUp() {
        // Очистка курьеров
        int totalCouriers = couriersToDelete.size();
        System.out.println("Очистка тестовых данных... Найдено курьеров: " + totalCouriers);

        for (String courierLogin : couriersToDelete) {
            try {
                System.out.println("Удаляем курьера: " + courierLogin);
                deleteCourierByLogin(courierLogin);
            } catch (Exception e) {
                System.out.println("Ошибка при удалении курьера " + courierLogin + ": " + e.getMessage());
            }
        }

        // Очистка заказов
        int totalOrders = ordersToCancel.size();
        System.out.println("Найдено заказов для отмены: " + totalOrders);

        for (Integer track : ordersToCancel) {
            try {
                System.out.println("Отменяем заказ с трекингом: " + track);
                OrderApi.cancelOrder(track);
            } catch (Exception e) {
                System.out.println("Ошибка при отмене заказа " + track + ": " + e.getMessage());
            }
        }

        couriersToDelete.clear();
        ordersToCancel.clear();
        System.out.println("Очистка завершена. Удалено курьеров: " + totalCouriers + ", отменено заказов: " + totalOrders);
    }

    // Удаление курьера по id
    private static void deleteCourierByLogin(String login) {
        try {
            String defaultPassword = "1234";
            int courierId = CourierSteps.getCourierId(login, defaultPassword);

            if (courierId > 0) {
                CourierSteps.deleteCourier(courierId);
                System.out.println("Курьер " + login + " (ID: " + courierId + ") успешно удален");
            }
        } catch (Exception e) {
            System.out.println("Не удалось удалить курьера " + login + ": " + e.getMessage());
        }
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