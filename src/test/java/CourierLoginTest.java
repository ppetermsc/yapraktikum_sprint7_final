import base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CourierModel;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.*;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.loginCourier;

public class CourierLoginTest extends BaseApiTest {

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Проверка что курьер может авторизоваться с валидными данными")
    public void testCourierLoginSuccess() {
        // Создаем курьера для теста
        String login = getLogin();
        String password = getPassword();
        CourierModel courier = new CourierModel(login, password, getFirstName());
        createCourier(courier).then().statusCode(HTTP_CREATED);

        // Логинимся и проверяем успешную авторизацию
        loginCourier(login, password)
                .then()
                .statusCode(HTTP_OK)
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class));
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка ошибки при авторизации без логина")
    public void testCourierLoginWithoutLoginShouldFail() {
        loginCourier(null, getPassword())
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля") // TODO: Добавить в баг-репорт. БАГ!
    @Description("Проверка ошибки при авторизации без пароля")
    public void testCourierLoginWithoutPasswordShouldFail() {
        loginCourier(getLogin(), null)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с пустым логином")
    @Description("Проверка ошибки при авторизации с пустым логином")
    public void testCourierLoginWithEmptyLoginShouldFail() {
        loginCourier("", getPassword())
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с пустым паролем")
    @Description("Проверка ошибки при авторизации с пустым паролем")
    public void testCourierLoginWithEmptyPasswordShouldFail() {
        loginCourier(getLogin(), "")
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверка ошибки при авторизации с неверным логином")
    public void testCourierLoginWithWrongLoginShouldFail() {
        // Создаем курьера
        String correctPassword = getPassword();
        CourierModel courier = new CourierModel(getLogin(), correctPassword, getFirstName());
        createCourier(courier).then().statusCode(HTTP_CREATED);

        // Пытаемся авторизоваться с неверным логином
        loginCourier("wrong_" + getLogin(), correctPassword)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка ошибки при авторизации с неверным паролем")
    public void testCourierLoginWithWrongPasswordShouldFail() {
        // Создаем курьера
        String correctLogin = getLogin();
        CourierModel courier = new CourierModel(correctLogin, getPassword(), getFirstName());
        createCourier(courier).then().statusCode(HTTP_CREATED);

        // Пытаемся авторизоваться с неверным паролем
        loginCourier(correctLogin, "wrong_password")
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    @Description("Проверка ошибки при авторизации под несуществующим пользователем")
    public void testCourierLoginNonExistentShouldFail() {
        loginCourier("non_existent_login", "any_password")
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация только с логином") // TODO: Добавить в баг-репорт. БАГ!
    @Description("Проверка ошибки при авторизации только с логином (без пароля)")
    public void testCourierLoginWithLoginOnlyShouldFail() {
        loginCourier(getLogin(), null)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация только с паролем")
    @Description("Проверка ошибки при авторизации только с паролем (без логина)")
    public void testCourierLoginWithPasswordOnlyShouldFail() {
        loginCourier(null, getPassword())
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без данных") // TODO: Добавить в баг-репорт. БАГ!
    @Description("Проверка ошибки при авторизации без логина и пароля")
    public void testCourierLoginWithoutAnyDataShouldFail() {
        loginCourier(null, null)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}