import api.OrderApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class GetOrdersTest {

    OrderApi orderApi;
    @Before
    public void setUp() {
        RestAssured.baseURI= UrlConstants.BASE_URI;
    }

    @Test
    @DisplayName("Проверка успешного получения списка заказов с лимитом заказа = 1")
    public void getOrdersListLimitOneSuccess() {
        orderApi = new OrderApi();

        Response response = orderApi.getOrdersListWithLimitOne();
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        Integer idActual = response.path("orders[0].id");
        assertNotNull(idActual);
    }

}
