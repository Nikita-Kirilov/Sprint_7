import data.CreateCourier;
import data.CreateOrders;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateOrdersTest {
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
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка успешного создания заказа")
    public void createOrdersSuccess() {
        CreateOrders order = new CreateOrders("Nick","Dubov","Karaevo, 35","5","9235672121",3,"2020-06-06","U dveri",color);
        postCreateOrders(order);
    }

    @Step("Send success POST request to /api/v1/orders")
    @Description("Успешное создание заказа")
    public void postCreateOrders(CreateOrders order) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
    }
}
