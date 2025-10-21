package steps;

import model.OrderModel;

import java.util.List;

import static data.TestData.*;

public class OrderDataGenerator {

    public static OrderModel createDefaultOrder(List<String> colors) {
        return new OrderModel(
                ORDER_FIRST_NAME,
                ORDER_LAST_NAME,
                ORDER_ADDRESS,
                ORDER_METRO_STATION,
                ORDER_PHONE,
                ORDER_RENT_TIME,
                ORDER_DELIVERY_DATE,
                ORDER_COMMENT,
                colors
        );
    }
}
