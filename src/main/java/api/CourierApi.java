package api;

import data.CreateCourier;
import data.FindCourier;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {
    private static final String CREATE_ENDPOINT = "/api/v1/courier";
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";
    private static final String DELETE_ENDPOINT = "/api/v1/courier/{id}";

    @Step("Send POST request to /api/v1/courier")
    @Description("Запрос на создание курьера")
    public Response postCreateCourier(CreateCourier courier) {
        return given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(courier)
                    .when()
                    .post(CREATE_ENDPOINT);
    }

    @Step("Send POST request to /api/v1/courier/login")
    @Description("Запрос на поиск id курьера по логину / паролю")
    public Response postFindIdCourier(FindCourier findCourier) {
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(findCourier)
                        .when()
                        .post(LOGIN_ENDPOINT);
    }

    @Step("Send DELETE request to /api/v1/courier/{id}")
    @Description("Запрос на удаление данных курьера по id")
    public void deleteCourier(String courierId) {
        if(courierId != null) {
            given()
                    .pathParam("id", courierId)
                    .when()
                    .delete(DELETE_ENDPOINT);
        }
    }
}
