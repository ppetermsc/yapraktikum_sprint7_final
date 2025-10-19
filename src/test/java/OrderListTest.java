import base.BaseApiTest;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import steps.OrderSteps;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderListTest extends BaseApiTest {

    @Test
    @DisplayName("Получение списка заказов - проверка наличия и типа данных")
    public void testGetOrderList() {
        OrderSteps.getOrderList()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(List.class))
                .body("orders.size()", greaterThan(0));
    }

    @Test
    @DisplayName("Проверка структуры данных в списке заказов")
    public void testGetOrderListDetailedStructure() {
        OrderSteps.getOrderList()
                .statusCode(200)
                .body("orders[0].id", notNullValue())
                .body("orders[0].firstName", notNullValue())
                .body("orders[0].lastName", notNullValue())
                .body("orders[0].address", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @Test
    @DisplayName("Полный цикл заказа: создание → получение по track")
    public void testCreateAndGetOrderByTrack() {
        // Создаем заказ
        int track = OrderSteps.createOrder(Arrays.asList("BLACK"))
                .statusCode(201)
                .extract().path("track");

        // Получаем заказ по track
        OrderSteps.getOrderByTrack(track)
                .statusCode(200)
                .body("order.id", notNullValue())
                .body("order.track", notNullValue());
    }
}