package steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import model.OrderModel;
import java.util.List;
import static data.TestData.*;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    private static final String ORDER_BASE_URL = "/api/v1/orders";

    // Основной метод создания заказа с OrderModel
    public static ValidatableResponse createOrder(OrderModel order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)  // ← Теперь передаем объект, а не Map!
                .when()
                .post(ORDER_BASE_URL)
                .then();
    }

     //Перегруженный метод с дефолтными значениями
    public static ValidatableResponse createOrder(List<String> colors) {
        OrderModel order = new OrderModel(
                OrderDefaults.FIRST_NAME,
                OrderDefaults.LAST_NAME,
                OrderDefaults.ADDRESS,
                OrderDefaults.METRO_STATION,
                OrderDefaults.PHONE,
                OrderDefaults.RENT_TIME,
                OrderDefaults.DELIVERY_DATE,
                OrderDefaults.COMMENT,
                colors
        );
        return createOrder(order);
    }

    // Метод для создания заказа со случайными данными
    public static ValidatableResponse createRandomOrder(List<String> colors) {
        OrderModel order = new OrderModel(
                getOrderFirstName(),
                getOrderLastName(),
                getOrderAddress(),
                getOrderMetroStation(),
                getOrderPhone(),
                getOrderRentTime(),
                getOrderDeliveryDate(),
                getOrderComment(),
                colors
        );
        return createOrder(order);
    }

    // Метод для создания заказа с кастомными данными
    public static ValidatableResponse createOrder(List<String> colors, String firstName, String lastName,
                                                  String address, int metroStation, String phone,
                                                  String rentTime, String deliveryDate, String comment) {
        OrderModel order = new OrderModel(
                firstName, lastName, address, metroStation, phone,
                rentTime, deliveryDate, comment, colors
        );
        return createOrder(order);
    }

    // Остальные методы остаются без изменений
    public static ValidatableResponse getOrderList() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_BASE_URL)
                .then();
    }

    public static ValidatableResponse getOrderByTrack(int track) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("t", track)
                .when()
                .get(ORDER_BASE_URL + "/track")
                .then();
    }

    public static ValidatableResponse acceptOrder(int orderId, int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("courierId", courierId)
                .when()
                .put(ORDER_BASE_URL + "/accept/" + orderId)
                .then();
    }

    public static ValidatableResponse finishOrder(int orderId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .put(ORDER_BASE_URL + "/finish/" + orderId)
                .then();
    }

    public static ValidatableResponse cancelOrder(int track) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("track", track)
                .when()
                .put(ORDER_BASE_URL + "/cancel")
                .then();
    }

    public static int getTrackFromResponse(ValidatableResponse response) {
        return response.extract().path("track");
    }

    public static int getOrderIdFromTrackResponse(ValidatableResponse response) {
        return response.extract().path("order.id");
    }
}