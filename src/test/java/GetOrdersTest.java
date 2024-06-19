import data.CreateOrders;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersTest {
    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка успешного получения списка заказов с лимитом заказа = 1")
    public void getOrdersListLimitOneSuccess() {
        getOrdersListWithLimitOne();
    }

    @Step("Send success GET request to /api/v1/orders with params limit=1")
    @Description("Успешное получение списка заказов с лимитом заказа = 1")
    public void getOrdersListWithLimitOne() {
        Response response =
                given()
                        .queryParam("limit", 1)
                        .when()
                        .get("/api/v1/orders/");
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("orders[0].id", notNullValue());
    }
}
