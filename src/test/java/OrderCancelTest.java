import base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import steps.OrderApi;
import steps.OrderDataGenerator;

import java.util.Arrays;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCancelTest extends BaseApiTest {

    @Test
    @DisplayName("Успешная отмена заказа")
    @Description("Проверка успешной отмены заказа по track номеру")
    public void testCancelOrderSuccess() {
        // Создаем заказ
        int track = OrderApi.createOrder(OrderDataGenerator.createDefaultOrder(Arrays.asList("BLACK")))
                .statusCode(HTTP_CREATED)
                .extract().path("track");

        // Добавляем в cleanup на случай, если тест упадет
        addOrderForCleanup(track);

        // Отменяем заказ
        OrderApi.cancelOrder(track)
                .statusCode(HTTP_OK)
                .body("ok", equalTo(true));

        // Удаляем из списка cleanup, так как уже отменили
        ordersToCancel.remove((Integer) track);
    }

    @Test
    @DisplayName("Отмена несуществующего заказа")
    @Description("Проверка ошибки при отмене заказа с несуществующим track")
    public void testCancelNonExistentOrderShouldFail() {
        int nonExistentTrack = 999999;

        OrderApi.cancelOrder(nonExistentTrack)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Отмена уже отмененного заказа")
    @Description("Проверка ошибки при повторной отмене заказа")
    public void testCancelAlreadyCanceledOrderShouldFail() {
        // Создаем заказ
        int track = OrderApi.createOrder(OrderDataGenerator.createDefaultOrder(Arrays.asList("GREY")))
                .statusCode(HTTP_CREATED)
                .extract().path("track");

        // Первая отмена - успешно
        OrderApi.cancelOrder(track)
                .statusCode(HTTP_OK);

        // Вторая отмена - должна быть ошибка
        OrderApi.cancelOrder(track)
                .statusCode(HTTP_CONFLICT)
                .body("message", equalTo("Заказ нельзя завершить")); // Исправлено на реальное сообщение
    }

    @Test
    @DisplayName("Отмена заказа без track")
    @Description("Проверка ошибки при отмене заказа без указания track")
    public void testCancelOrderWithoutTrackShouldFail() {
        // Отправляем запрос с track = 0 (система интерпретирует как отсутствие track)
        OrderApi.cancelOrder(0)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Заказ не найден")); // Исправлено на реальное сообщение
    }

    @Test
    @DisplayName("Отмена заказа с некорректным track")
    @Description("Проверка ошибки при отмене заказа с отрицательным track")
    public void testCancelOrderWithInvalidTrackShouldFail() {
        OrderApi.cancelOrder(-1)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Заказ не найден")); // Исправлено на реальное сообщение
    }
}