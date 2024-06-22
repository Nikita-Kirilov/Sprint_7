package api;

import data.CreateOrders;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderApi {

    private static final String GET_OR_CREATED_ORDER_ENDPOINT = "/api/v1/orders/";

    @Step("Send GET request to /api/v1/orders with params limit=1")
    @Description("Успешное получение списка заказов с лимитом заказа = 1")
    public Response getOrdersListWithLimitOne() {
        return given()
                    .queryParam("limit", 1)
                    .when()
                    .get(GET_OR_CREATED_ORDER_ENDPOINT);
    }

    @Step("Send POST request to /api/v1/orders")
    @Description("Успешное создание заказа")
    public Response postCreateOrders(CreateOrders order) {
        return given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(order)
                    .when()
                    .post(GET_OR_CREATED_ORDER_ENDPOINT);
    }
}
