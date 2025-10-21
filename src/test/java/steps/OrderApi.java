package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import model.OrderModel;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private static final String ORDER_BASE_URL = "/api/v1/orders";

    @Step("Создать заказ")
    public static ValidatableResponse createOrder(OrderModel order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_BASE_URL)
                .then();
    }

    @Step("Получить список заказов")
    public static ValidatableResponse getOrderList() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_BASE_URL)
                .then();
    }

    @Step("Получить заказ по трекингу: {track}")
    public static ValidatableResponse getOrderByTrack(int track) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("t", track)
                .when()
                .get(ORDER_BASE_URL + "/track")
                .then();
    }

    @Step("Принять заказ {orderId} курьером {courierId}")
    public static ValidatableResponse acceptOrder(int orderId, int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("courierId", courierId)
                .when()
                .put(ORDER_BASE_URL + "/accept/" + orderId)
                .then();
    }

    @Step("Завершить заказ {orderId}")
    public static ValidatableResponse finishOrder(int orderId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .put(ORDER_BASE_URL + "/finish/" + orderId)
                .then();
    }

    @Step("Отменить заказ с трекингом: {track}")
    public static ValidatableResponse cancelOrder(int track) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("track", track)
                .when()
                .put(ORDER_BASE_URL + "/cancel")
                .then();
    }
}