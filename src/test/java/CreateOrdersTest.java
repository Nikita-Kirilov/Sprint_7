import api.OrderApi;
import data.CreateOrders;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrdersTest {
    OrderApi orderApi;
    private final List<String> color;

    public CreateOrdersTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTextData() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK, GREY")},
                {List.of()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= UrlConstants.BASE_URI;
    }

    @Test
    @DisplayName("Проверка успешного создания заказа")
    public void createOrdersSuccess() {
        orderApi = new OrderApi();

        CreateOrders order = new CreateOrders("Nick","Dubov","Karaevo, 35","5","9235672121",3,"2020-06-06","U dveri",color);

        Response response = orderApi.postCreateOrders(order);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        Integer idActual = response.path("track");
        idActual.equals(notNullValue());
    }

}
