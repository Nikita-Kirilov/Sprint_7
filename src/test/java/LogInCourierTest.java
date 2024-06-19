import data.CreateCourier;
import data.FindCourier;
import data.FindCourierWithoutLogin;
import data.FindCourierWithoutPassword;
import feature.RandomLogin;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LogInCourierTest {

    RandomLogin randomLogin = new RandomLogin();
    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка корректной авторизации курьера. 200")
    public void checkCourierLogIn() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier(unicLogin,
                "123Mimino_567","Sasha");

        //Создание курьера
        postCreateCourier(courier);


        FindCourier findCourier = new FindCourier(courier.login,courier.password);

        //Проверка авторизации курьера в системе и запись id в переменную
        String courierId = postFindIdCourier(findCourier);

        //Удаление курьера
        deleteCourier(courierId);
    }

    @Test
    @DisplayName("Проверка авторизации курьера с несуществующими логином/паролем. 404")
    public void checkCourierLogInNotExist() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        String unicPassword = "Mimino" + randomLogin.randomText(6);
        String message = "Учетная запись не найдена";

        FindCourier findCourier = new FindCourier(unicLogin,unicPassword);

        postFindIdCourierNotExist(findCourier, message);
    }

    @Test
    @DisplayName("Проверка авторизации курьера без передачи логина. 400")
    public void checkCourierLogInWithoutLogin() {
        String unicPassword = "Mimino" + randomLogin.randomText(6);
        String message = "Недостаточно данных для входа";

        FindCourierWithoutLogin findCourier = new FindCourierWithoutLogin(unicPassword);

        postFindIdCourierWithoutLogin(findCourier, message);
    }

    @Test
    @DisplayName("Проверка авторизации курьера без передачи пароля. 400")
    public void checkCourierLogInWithoutPassword() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        String message = "Недостаточно данных для входа";

        FindCourierWithoutPassword findCourier = new FindCourierWithoutPassword(unicLogin);

        postFindIdCourierWithoutPassword(findCourier, message);
    }

    @Test
    @DisplayName("Проверка авторизации курьера с несуществующим логином. 404")
    public void checkCourierLogInNotExistLogin() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier(unicLogin,
                "123Mimino_567","Sasha");
        String message = "Учетная запись не найдена";

        //Создание курьера
        postCreateCourier(courier);

        FindCourier findCourier = new FindCourier("NotExistLogin",courier.password);
        FindCourier findCourierForCleanTest = new FindCourier(courier.login,courier.password);

        postFindIdCourierNotExist(findCourier, message);
        // Поиск Id курьера для дальнейшего удаления
        String courierId = postFindIdCourier(findCourierForCleanTest);
        //Удаление курьера
        deleteCourier(courierId);

    }

    @Test
    @DisplayName("Проверка авторизации курьера с несуществующим паролем. 404")
    public void checkCourierLogInNotExistPassword() {
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier(unicLogin,
                "123Mimino_567","Sasha");
        String message = "Учетная запись не найдена";

        //Создание курьера
        postCreateCourier(courier);

        FindCourier findCourier = new FindCourier(courier.login,"NotExistPassword");
        FindCourier findCourierForCleanTest = new FindCourier(courier.login,courier.password);

        postFindIdCourierNotExist(findCourier, message);
        // Поиск Id курьера для дальнейшего удаления
        String courierId = postFindIdCourier(findCourierForCleanTest);
        //Удаление курьера
        deleteCourier(courierId);
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

    @Step("Send success POST request to /api/v1/courier/login")
    @Description("поиск id курьера по логину / паролю и запись его в String переменную")
    public String postFindIdCourier(FindCourier findCourier) {
        String courierId =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(findCourier)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().assertThat()
                        .statusCode(200)
                        .and()
                        .body("id", notNullValue())
                        .extract().body().path("id").toString();
        return courierId;
    }

    @Step("Send failed POST request to /api/v1/courier/login with login/password not exist")
    @Description("поиск id курьера по логину / паролю, которых не существует")
    public void postFindIdCourierNotExist(FindCourier findCourier, String message) {
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(findCourier)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().assertThat()
                        .statusCode(404)
                        .and()
                        .body("message",equalTo(message));
    }

    @Step("Send failed POST request to /api/v1/courier/login without login")
    @Description("поиск id курьера по логину / паролю, без передачи логина")
    public void postFindIdCourierWithoutLogin(FindCourierWithoutLogin findCourier, String message) {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(findCourier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat()
                .statusCode(400)
                .and()
                .body("message",equalTo(message));
    }

    @Step("Send failed POST request to /api/v1/courier/login without password")
    @Description("поиск id курьера по логину / паролю, без передачи пароля")
    public void postFindIdCourierWithoutPassword(FindCourierWithoutPassword findCourier, String message) {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(findCourier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat()
                .statusCode(400)
                .and()
                .body("message",equalTo(message));
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
