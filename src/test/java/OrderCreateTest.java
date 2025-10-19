import base.BaseApiTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK"), "создание заказа с BLACK цветом"},
                {Arrays.asList("GREY"), "создание заказа с GREY цветом"},
                {Arrays.asList("BLACK", "GREY"), "создание заказа с обоими цветами"},
                {null, "создание заказа без указания цвета"},
                {Arrays.asList(), "создание заказа с пустым списком цветов"}
        });
    }

    @Test
    public void testCreateOrderWithDifferentColors() {
        OrderSteps.createOrder(colors)
                .statusCode(201)
                .body("track", notNullValue());
    }
}