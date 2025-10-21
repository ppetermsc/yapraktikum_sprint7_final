import base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CourierModel;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.CourierSteps.createCourier;

public class CreateCourierTest extends BaseApiTest {

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверка успешного создания курьера с валидными данными")
    public void testCreateCourierSuccess() {
        CourierModel courier = new CourierModel(getLogin(), getPassword(), getFirstName());

        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверка, что система не позволяет создать дубликат курьера")
    public void testCreateDuplicateCourierShouldFail() {
        String login = getLogin();
        String password = getPassword();
        String firstName = getFirstName();

        CourierModel courier = new CourierModel(login, password, firstName);

        // Первое создание - должно быть успешно
        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED);

        // Второе создание с теми же данными - должно быть ошибка
        createCourier(courier)
                .then()
                .statusCode(HTTP_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка ошибки при создании курьера без обязательного поля login")
    public void testCreateCourierWithoutLoginShouldFail() {
        // Создаем курьера без логина (null)
        CourierModel courier = new CourierModel(null, getPassword(), getFirstName());

        createCourier(courier)
                .then()
                .log().all()  // Добавим лог для отладки
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка ошибки при создании курьера без обязательного поля password")
    public void testCreateCourierWithoutPasswordShouldFail() {
        // Создаем курьера без пароля (null)
        CourierModel courier = new CourierModel(getLogin(), null, getFirstName());

        createCourier(courier)
                .then()
                .log().all()  // Добавим лог для отладки
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с пустым логином")
    @Description("Проверка ошибки при создании курьера с пустым логином")
    public void testCreateCourierWithEmptyLoginShouldFail() {
        // Создаем курьера с пустым логином
        CourierModel courier = new CourierModel("", getPassword(), getFirstName());

        createCourier(courier)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с пустым паролем")
    @Description("Проверка ошибки при создании курьера с пустым паролем")
    public void testCreateCourierWithEmptyPasswordShouldFail() {
        // Создаем курьера с пустым паролем
        CourierModel courier = new CourierModel(getLogin(), "", getFirstName());

        createCourier(courier)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера только с логином")
    @Description("Проверка ошибки при создании курьера только с логином")
    public void testCreateCourierWithLoginOnlyShouldFail() {
        CourierModel courier = new CourierModel(getLogin(), null, null);

        createCourier(courier)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    @Description("Проверка ошибки при создании курьера с уже занятым логином")
    public void testCreateCourierWithExistingLoginShouldFail() {
        String existingLogin = getLogin();

        // Сначала создаем курьера
        CourierModel firstCourier = new CourierModel(existingLogin, getPassword(), getFirstName());
        createCourier(firstCourier).then().statusCode(HTTP_CREATED);

        // Пытаемся создать другого курьера с таким же логином
        CourierModel secondCourier = new CourierModel(existingLogin, getPassword(), getFirstName());

        createCourier(secondCourier)
                .then()
                .statusCode(HTTP_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Проверка, что курьера можно создать без необязательного поля firstName")
    public void testCreateCourierWithoutFirstNameShouldSuccess() {
        CourierModel courier = new CourierModel(getLogin(), getPassword(), null);

        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
    }
}
