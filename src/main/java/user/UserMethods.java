package user;

import io.qameta.allure.Step;

import static constants.URLs.BASE_URL;
import static constants.URLs.LOGIN_PATH;
import static constants.URLs.*;
import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

import java.util.Random;

public class UserMethods {
    @Step("Создать уникального пользователя")
    public static Response registrationUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(BASE_URL + REGISTER_PATH);
    }

    public static int rndNum() {
        Random random = new Random();
        return 3600 + random.nextInt(1000000 - 3600);
    }

    @Step("Удалить  пользователя")
    public static void delUser(String accessToken) {
        if (accessToken != null) {
            given()
                    .header("authorization", accessToken)
                    .delete(BASE_URL + DELETE_PATH);
        }
    }

    @Step("Авторизация пользователя")
    public static Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(BASE_URL + LOGIN_PATH);
    }

    @Step("Обновление данных пользователя")
    public static Response editUser(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(BASE_URL + EDITUSER_PATH);
    }
}
