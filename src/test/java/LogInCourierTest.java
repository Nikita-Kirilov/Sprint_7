import api.CourierApi;
import data.CreateCourier;
import data.FindCourier;
import feature.RandomLogin;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LogInCourierTest {
    private CourierApi courierApi;
    private RandomLogin randomLogin = new RandomLogin();
    private String courierId;

    private static final String message404Expected = "Учетная запись не найдена";
    private static final String message400Expected = "Недостаточно данных для входа";

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
    @DisplayName("Проверка корректной авторизации курьера. 200")
    public void checkCourierLogIn() {
        courierApi = new CourierApi();

        String unicLogin = "Sasha" + randomLogin.randomText(6);

        CreateCourier courier = new CreateCourier()
                .withLogin(unicLogin)
                .withPassword("123Mimino_567")
                .withFirstName("Sasha");

        //Создание курьера
        courierApi.postCreateCourier(courier);


        FindCourier findCourier = new FindCourier()
                .withLogin(courier.getLogin())
                .withPassword(courier.getPassword());

        //Проверка авторизации курьера в системе и запись id в переменную
        Response response = courierApi.postFindIdCourier(findCourier);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        Integer idActual = response.path("id");
        assertNotNull(idActual);

        courierId = idActual.toString();


    }

    @Test
    @DisplayName("Проверка авторизации курьера с несуществующими логином/паролем. 404")
    public void checkCourierLogInNotExist() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        String unicPassword = "Mimino" + randomLogin.randomText(6);

        FindCourier findCourier = new FindCourier()
                .withLogin(unicLogin)
                .withPassword(unicPassword);

        Response response = courierApi.postFindIdCourier(findCourier);
        String messageActual = response.path("message");
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        assertEquals("Некорректый ответ в Body", message404Expected, messageActual);
    }

    @Test
    @DisplayName("Проверка авторизации курьера без передачи логина. 400")
    public void checkCourierLogInWithoutLogin() {
        courierApi = new CourierApi();
        String unicPassword = "Mimino" + randomLogin.randomText(6);

        FindCourier findCourier = new FindCourier()
                .withPassword(unicPassword);

        Response response = courierApi.postFindIdCourier(findCourier);
        String messageActual = response.path("message");
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Некорректый ответ в Body", message400Expected, messageActual);
    }

    @Test
    @DisplayName("Проверка авторизации курьера без передачи пароля. 400")
    public void checkCourierLogInWithoutPassword() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        String message = "Недостаточно данных для входа";

        FindCourier findCourier = new FindCourier()
                .withLogin(unicLogin);

        Response response = courierApi.postFindIdCourier(findCourier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        String messageActual = response.path("message");
        assertEquals("Некорректый ответ в Body", message400Expected, messageActual);
    }

    @Test
    @DisplayName("Проверка авторизации курьера с несуществующим логином. 404")
    public void checkCourierLogInNotExistLogin() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier()
                .withLogin(unicLogin)
                .withPassword("123Mimino_567")
                .withFirstName("Sasha");

        //Создание курьера
        courierApi.postCreateCourier(courier);

        FindCourier findCourier = new FindCourier()
                .withLogin("NotExistLogin")
                .withPassword(courier.getPassword());

        FindCourier findCourierForCleanTest = new FindCourier()
                .withLogin(courier.getLogin())
                .withPassword(courier.getPassword());

        // Проверка авторизации курьера с несуществующим логином
        Response response = courierApi.postFindIdCourier(findCourier);
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        String messageActual = response.path("message");
        assertEquals("Некорректый ответ в Body", message404Expected, messageActual);

        // Поиск Id курьера для дальнейшего удаления
        Response responseFindCourierForCleanTest = courierApi.postFindIdCourier(findCourierForCleanTest);
        courierId = responseFindCourierForCleanTest.path("id").toString();
    }

    @Test
    @DisplayName("Проверка авторизации курьера с несуществующим паролем. 404")
    public void checkCourierLogInNotExistPassword() {
        courierApi = new CourierApi();
        String unicLogin = "Sasha" + randomLogin.randomText(6);
        CreateCourier courier = new CreateCourier()
                .withLogin(unicLogin)
                .withPassword("123Mimino_567")
                .withFirstName("Sasha");

        //Создание курьера
        courierApi.postCreateCourier(courier);

        FindCourier findCourier = new FindCourier()
                .withLogin(courier.getLogin())
                .withPassword("NotExistPassword");

        FindCourier findCourierForCleanTest = new FindCourier()
                .withLogin(courier.getLogin())
                .withPassword(courier.getPassword());

        //Проверка авторизации курьера с несуществующим паролем
        Response response = courierApi.postFindIdCourier(findCourier);
        String messageActual = response.path("message");
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        assertEquals("Некорректый ответ в Body", message404Expected, messageActual);

        // Поиск Id курьера для дальнейшего удаления
        Response responseFindCourierForCleanTest = courierApi.postFindIdCourier(findCourierForCleanTest);
        courierId = responseFindCourierForCleanTest.path("id").toString();
    }
}
