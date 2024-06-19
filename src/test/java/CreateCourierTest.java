import data.*;
import feature.RandomLogin;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    RandomLogin randomLogin = new RandomLogin();
    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка корректного создания курьера. 201")
    public void createCourierSuccess() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier(unicLogin,
                "123Mimino_567","Sasha");

        //Проверка успешного создания курьера
        postCreateCourier(courier);

        FindCourier findCourier = new FindCourier(courier.login,courier.password);

        //Поиск id курьера и запись id в пересменную
        String courierId = postFindIdCourier(findCourier);

        //Удаление курьера
        deleteCourier(courierId);
    }

    @Test
    @DisplayName("Проверка корректного создания курьера без firstName. 201")
    public void createCourierSuccessWithoutFirstName() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourierWithoutFirstName courier = new CreateCourierWithoutFirstName(unicLogin,
                "123Mimino_567");

        //Проверка успешного создания курьера
        postCreateCourierWithoutFirstName(courier);

        FindCourier findCourier = new FindCourier(courier.login, courier.password);

        //Поиск id курьера и запись id в пересменную
        String courierId = postFindIdCourier(findCourier);

        //Удаление курьера
        deleteCourier(courierId);
    }

    @Test
    @DisplayName("Проверка создания 2ух одинаковых курьеров с одним логином. 409")
    public void createTwoSameCourier() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier( unicLogin,
                "123Mimino_567","Sasha");
        CreateCourier courierWithSameLogin = new CreateCourier( unicLogin,
                "123Mimino","Alexandr");
        String message = "Этот логин уже используется";
        postCreateCourier(courier);
        FindCourier findCourier = new FindCourier(courier.login, courier.password);

        postCreateCourierFailedUnic(courierWithSameLogin, message);

        //Поиск id курьера и запись id в пересменную
        String courierId = postFindIdCourier(findCourier);
        //Удаление курьера
        deleteCourier(courierId);
    }

    @Test
    @DisplayName("Проверка создания курьера без логина. 400")
    public void createCourierWithoutLogin() {
        CreateCourierWithoutLogin courier = new CreateCourierWithoutLogin(
                "123Mimino_567","Sasha");
        String message = "Недостаточно данных для создания учетной записи";
        postCreateCourierWithoutLogin(courier, message);
    }

    @Test
    @DisplayName("Проверка создания курьера без пароля. 400")
    public void createCourierWithoutPassword() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourierWithoutPassword courier = new CreateCourierWithoutPassword(
                unicLogin,"Sasha");
        String message = "Недостаточно данных для создания учетной записи";
        postCreateCourierWithoutPassword(courier, message);
    }

    @Step("Send success POST request to /api/v1/courier")
    @Description("Успешное создание курьера")
    public void postCreateCourier(CreateCourier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("ok",equalTo(true));
    }

    @Step("Send failed POST request to /api/v1/courier")
    @Description("Ошибка уникальности создания курьера")
    public void postCreateCourierFailedUnic(CreateCourier courier, String message) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(409)
                .and()
                .body("message",equalTo(message));
    }

    @Step("Send failed POST request to /api/v1/courier without login")
    @Description("Ошибка создания курьера без логина")
    public void postCreateCourierWithoutLogin(CreateCourierWithoutLogin courier, String message) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(400)
                .and()
                .body("message",equalTo(message));
    }

    @Step("Send failed POST request to /api/v1/courier without password")
    @Description("Ошибка создания курьера без пароля")
    public void postCreateCourierWithoutPassword(CreateCourierWithoutPassword courier, String message) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(400)
                .and()
                .body("message",equalTo(message));
    }

    @Step("Send success POST request to /api/v1/courier without firstName")
    @Description("Успешное создание курьера без имени")
    public void postCreateCourierWithoutFirstName(CreateCourierWithoutFirstName courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("ok",equalTo(true));
    }

    @Step("Send POST request to /api/v1/courier/login")
    @Description("поиск id курьера по логину / паролю и запись его в String переменную")
    public String postFindIdCourier(FindCourier findCourier) {
        String courierId =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(findCourier)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().extract().body().path("id").toString();
        return courierId;
    }

    @Step("Send DELETE request to /api/v1/courier/{id}")
    @Description("удаление данных курьера по id")
    public void deleteCourier(String courierId) {
        given()
                .pathParam("id", courierId)
                .when()
                .delete("/api/v1/courier/{id}")
                .then().assertThat().statusCode(200);
    }
}
