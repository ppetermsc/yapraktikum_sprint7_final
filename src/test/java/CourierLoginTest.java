import base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.*;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.loginCourier;

public class CourierLoginTest extends BaseApiTest {

    private String testLogin;
    private String testPassword;
    private String testFirstName;

    @Before
    public void prepareTestData() {
        // Создаем тестового курьера перед каждым тестом
        testLogin = getLogin();
        testPassword = getPassword();
        testFirstName = getFirstName();

        CourierModel courier = new CourierModel(testLogin, testPassword, testFirstName);
        createCourier(courier).then().statusCode(HTTP_CREATED);

        // Добавляем в cleanup
        addCourierForCleanup(testLogin);
    }

    @After
    public void cleanupTestData() {
    } // Основная очистка происходит в родительском классе BaseApiTest.cleanUp()

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Проверка что курьер может авторизоваться с валидными данными")
    public void testCourierLoginSuccess() {
        loginCourier(testLogin, testPassword)
                .then()
                .statusCode(HTTP_OK)
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class));
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка ошибки при авторизации без логина")
    public void testCourierLoginWithoutLoginShouldFail() {
        loginCourier(null, testPassword)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка ошибки при авторизации без пароля")
    public void testCourierLoginWithoutPasswordShouldFail() {
        loginCourier(testLogin, null)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с пустым логином")
    @Description("Проверка ошибки при авторизации с пустым логином")
    public void testCourierLoginWithEmptyLoginShouldFail() {
        loginCourier("", testPassword)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с пустым паролем")
    @Description("Проверка ошибки при авторизации с пустым паролем")
    public void testCourierLoginWithEmptyPasswordShouldFail() {
        loginCourier(testLogin, "")
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверка ошибки при авторизации с неверным логином")
    public void testCourierLoginWithWrongLoginShouldFail() {
        loginCourier("wrong_" + testLogin, testPassword)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка ошибки при авторизации с неверным паролем")
    public void testCourierLoginWithWrongPasswordShouldFail() {
        loginCourier(testLogin, "wrong_password")
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
    @DisplayName("Авторизация без данных")
    @Description("Проверка ошибки при авторизации без логина и пароля")
    public void testCourierLoginWithoutAnyDataShouldFail() {
        loginCourier(null, null)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}