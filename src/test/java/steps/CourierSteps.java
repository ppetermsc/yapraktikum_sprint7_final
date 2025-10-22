package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierModel;

import static data.TestData.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    public static final String COURIER_CREATE_PATH = "/api/v1/courier";
    public static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";
    public static final String COURIER_DELETE_PATH = "/api/v1/courier/";

    //Создает курьера - только отправка запроса
    @Step("Создать курьера")
    public static Response createCourier(CourierModel courierModel) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courierModel)
                .when()
                .post(COURIER_CREATE_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Логин курьера")
    public static Response loginCourier(String login, String password) {
        CourierModel loginData = new CourierModel(login, password, null);

        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Получить ID курьера")
    public static int getCourierId(String login, String password) {
        Response response = loginCourier(login, password);
        return response.jsonPath().getInt("id");
    }

    @Step("Удалить курьера по ID: {courierId}")
    public static Response deleteCourier(int courierId) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(COURIER_DELETE_PATH + courierId)
                .then()
                .log().all()
                .extract().response();
    }
}