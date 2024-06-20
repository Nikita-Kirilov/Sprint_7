import api.CourierApi;
import data.*;
import feature.RandomLogin;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CreateCourierTest {
    private RandomLogin randomLogin = new RandomLogin();
    private CourierApi courierApi;

    private static final String message409Expected = "Этот логин уже используется";
    private static final String message400Expected = "Недостаточно данных для создания учетной записи";

    private static final boolean keyOkExpected = true;

    private String courierId;
    @Before
    public void setUp() {
        RestAssured.baseURI= UrlConstants.BASE_URI;
    }
    @After
    public void teardown() {
        //Удаление курьера
        courierApi.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Проверка корректного создания курьера. 201")
    public void createCourierSuccess() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier()
                .withLogin(unicLogin)
                .withPassword("123Mimino_567")
                .withFirstName("Sasha");

        //Проверка успешного создания курьера
        Response response = courierApi.postCreateCourier(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        boolean okActual = response.path("ok");
        assertEquals("Некорректый ответ в Body", keyOkExpected, okActual);

        FindCourier findCourier = new FindCourier()
                .withLogin(courier.getLogin())
                .withPassword(courier.getPassword());

        //Поиск id курьера и запись id в переменную courierId для дальнейшего удаления курьера
        Response loginResponse = courierApi.postFindIdCourier(findCourier);
        courierId = (loginResponse.path("id")).toString();
    }

    @Test
    @DisplayName("Проверка корректного создания курьера без firstName. 201")
    public void createCourierSuccessWithoutFirstName() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier()
                .withLogin(unicLogin)
                .withPassword("123Mimino_567");

        //Проверка успешного создания курьера
        Response response = courierApi.postCreateCourier(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        boolean okActual = response.path("ok");
        assertEquals("Некорректый ответ в Body", keyOkExpected,okActual);

        FindCourier findCourier = new FindCourier()
                .withLogin(courier.getLogin())
                .withPassword(courier.getPassword());

        //Поиск id курьера и запись id в переменную courierId для дальнейшего удаления курьера
        Response loginResponse = courierApi.postFindIdCourier(findCourier);
        courierId = (loginResponse.path("id")).toString();
    }

    @Test
    @DisplayName("Проверка создания 2ух одинаковых курьеров с одним логином. 409")
    public void createTwoSameCourier() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);

        CreateCourier courier = new CreateCourier()
                .withLogin(unicLogin)
                .withPassword("123Mimino_567")
                .withFirstName("Sasha");

        CreateCourier courierWithSameLogin = new CreateCourier()
                .withLogin(unicLogin)
                .withPassword("123Mimino")
                .withFirstName("Alexandr");

        FindCourier findCourier = new FindCourier()
                .withLogin(courier.getLogin())
                .withPassword(courier.getPassword());

        courierApi.postCreateCourier(courier);

        Response response = courierApi.postCreateCourier(courierWithSameLogin);
        assertEquals("Неверный статус код", SC_CONFLICT, response.statusCode());
        String messageActual = response.path("message");
        assertEquals("Некорректый ответ в Body", message409Expected, messageActual);

        //Поиск id курьера и запись id в переменную courierId для дальнейшего удаления курьера
        Response loginResponse = courierApi.postFindIdCourier(findCourier);
        courierId = (loginResponse.path("id")).toString();
    }

    @Test
    @DisplayName("Проверка создания курьера без логина. 400")
    public void createCourierWithoutLogin() {
        courierApi = new CourierApi();
        CreateCourier courier = new CreateCourier()
                .withPassword("123Mimino")
                .withFirstName("Alexandr");

        Response response = courierApi.postCreateCourier(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        String messageActual = response.path("message");
        assertEquals("Некорректый ответ в Body", message400Expected, messageActual);
    }

    @Test
    @DisplayName("Проверка создания курьера без пароля. 400")
    public void createCourierWithoutPassword() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier()
                .withLogin(unicLogin)
                .withFirstName("Sasha");

        Response response = courierApi.postCreateCourier(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        String messageActual = response.path("message");
        assertEquals("Некорректый ответ в Body", message400Expected, messageActual);
    }

}
