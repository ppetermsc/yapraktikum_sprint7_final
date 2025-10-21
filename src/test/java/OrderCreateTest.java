import base.BaseApiTest;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderApi;
import steps.OrderDataGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static data.TestData.getOrderColorData;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseApiTest {

    private final List<String> colors;
    private final String testName;

    public OrderCreateTest(List<String> colors, String testName) {
        this.colors = colors;
        this.testName = testName;
    }

    @Parameterized.Parameters(name = "Тест {index}: {1}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(getOrderColorData());
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void testCreateOrderWithDifferentColors() {
        int track = OrderApi.createOrder(OrderDataGenerator.createDefaultOrder(colors))
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract().path("track");

        // Добавляем заказ в cleanup
        addOrderForCleanup(track);
    }
}
