import base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.CourierSteps.*;

public class CourierDeleteTest extends BaseApiTest {

    private String testLogin;
    private String testPassword;
    private int courierId;

    @Before
    public void prepareTestData() {
        // Создаем тестового курьера перед каждым тестом
        testLogin = getLogin();
        testPassword = getPassword();
        String testFirstName = getFirstName();

        CourierModel courier = new CourierModel(testLogin, testPassword, testFirstName);
        createCourier(courier).then().statusCode(HTTP_CREATED);

        // Получаем ID курьера для использования в тестах
        courierId = getCourierId(testLogin, testPassword);
    }

    @After
    public void cleanupTestData() {
        // Основная очистка происходит в родительском классе BaseApiTest.cleanUp()
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    @Description("Проверка успешного удаления курьера по ID")
    public void testDeleteCourierSuccess() {
        deleteCourier(courierId)
                .then()
                .statusCode(HTTP_OK)
                .body("ok", equalTo(true));

        // Удаляем из списка cleanup, так как уже удалили
        couriersToDelete.remove(testLogin);
    }

    @Test
    @DisplayName("Удаление курьера без ID")
    @Description("Проверка ошибки при удалении курьера без указания ID")
    public void testDeleteCourierWithoutIdShouldFail() {
        // Отправляем запрос на некорректный эндпоинт без ID
        deleteCourier(0)
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test
    @DisplayName("Удаление несуществующего курьера")
    @Description("Проверка ошибки при удалении курьера с несуществующим ID")
    public void testDeleteNonExistentCourierShouldFail() {
        int nonExistentId = 999999;

        deleteCourier(nonExistentId)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Удаление уже удаленного курьера")
    @Description("Проверка ошибки при повторном удалении курьера")
    public void testDeleteAlreadyDeletedCourierShouldFail() {
        // Первое удаление - успешно
        deleteCourier(courierId)
                .then()
                .statusCode(HTTP_OK);

        // Удаляем из списка cleanup
        couriersToDelete.remove(testLogin);

        // Второе удаление - должно быть ошибка
        deleteCourier(courierId)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Удаление курьера с некорректным ID")
    @Description("Проверка ошибки при удалении курьера с отрицательным ID")
    public void testDeleteCourierWithInvalidIdShouldFail() {
        deleteCourier(-1)
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }
}