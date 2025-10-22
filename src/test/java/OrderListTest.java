import base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import steps.OrderApi;
import steps.OrderDataGenerator;

import java.util.Arrays;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderListTest extends BaseApiTest {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка что в тело ответа возвращается список заказов")
    public void testGetOrderList() {
        OrderApi.getOrderList()
                .statusCode(SC_OK)
                .body("orders", notNullValue()) // Cписок есть
                .body("orders.size()", greaterThan(0)); // Список не пустой
    }

    @Test
    @DisplayName("Полный цикл заказа: создание → получение по трекингу")
    public void testCreateAndGetOrderByTrack() {
        // Создаем заказ
        int track = OrderApi.createOrder(OrderDataGenerator.createDefaultOrder(Arrays.asList("BLACK")))
                .statusCode(SC_CREATED)
                .extract().path("track");

        // Добавляем в cleanup
        addOrderForCleanup(track);

        // Получаем заказ по трекингу
        OrderApi.getOrderByTrack(track)
                .statusCode(SC_OK)
                .body("order.id", notNullValue())
                .body("order.track", notNullValue());
    }
}