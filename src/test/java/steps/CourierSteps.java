package steps;

import base.BaseApiTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierModel;

import static data.TestData.*;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;

public class CourierSteps {

    public static final String COURIER_CREATE_PATH = "/api/v1/courier";
    public static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";

    /**
     * Создает курьера для тестов
     */
    public static Response createCourier(CourierModel courierModel) {
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courierModel)
                .when()
                .post(COURIER_CREATE_PATH)
                .then()
                .log().all()
                .extract().response();

        // Добавляем курьера в список для очистки
        if (response.statusCode() == 201) {
            BaseApiTest.addCourierForCleanup(courierModel.getLogin());
        }

        return response;
    }

    /**
     * Создает тестового курьера и возвращает его логин
     * Используется для подготовки данных в тестах
     */
    public static String createTestCourier() {
        String login = getLogin();
        String password = getPassword();
        String firstName = getFirstName();

        CourierModel courier = new CourierModel(login, password, firstName);
        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED);

        return login;  // Возвращаем логин для использования в тестах
    }

    /**
     * Создает тестового курьера с указанными данными
     */
    public static String createTestCourier(String login, String password, String firstName) {
        CourierModel courier = new CourierModel(login, password, firstName);
        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED);

        return login;
    }

    /**
     * Создает тестового курьера и возвращает все данные
     * Используется когда нужен и логин и пароль
     */
    public static CourierCredentials createTestCourierWithCredentials() {
        String login = getLogin();
        String password = getPassword();
        String firstName = getFirstName();

        CourierModel courier = new CourierModel(login, password, firstName);
        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED);

        return new CourierCredentials(login, password);
    }

    /**
     * Логин курьера
     */
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

    /**
     * Вспомогательный класс для возврата credentials
     */
    public static class CourierCredentials {
        private final String login;
        private final String password;

        public CourierCredentials(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() { return login; }
        public String getPassword() { return password; }
    }
}
