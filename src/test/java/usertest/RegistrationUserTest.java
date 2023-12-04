package usertest;

import user.User;
import user.UserMethods;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class RegistrationUserTest {
    private static final String email = "useremail";
    private static final String password = "unipasword";
    private static final String name = "unnamedd";
    String accessToken;

    @Test
    @DisplayName("Создание пользователя")
    @Description("создать уникального пользователя")
    public void registrationUniqueUser() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, name + num);
        Response response = UserMethods.registrationUser(user);
        accessToken = response.then().extract().path("accessToken").toString();

        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("создать пользователя, который уже зарегистрирован")
    public void registrationNotUniqueUser() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, name + num);
        UserMethods.registrationUser(user);
        Response response = UserMethods.registrationUser(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("создать пользователя и не заполнить одно (email) из обязательных полей.")
    public void registrationUserRequiredFieldEmail() {
        int num = UserMethods.rndNum();
        User user = new User("", password + num, name + num);
        Response response = UserMethods.registrationUser(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("создать пользователя и не заполнить одно (password) из обязательных полей.")
    public void registrationUserRequiredFieldPass() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", "", name + num);
        Response response = UserMethods.registrationUser(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("создать пользователя и не заполнить одно (name) из обязательных полей.")
    public void registrationUserRequiredFieldName() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, "");
        Response response = UserMethods.registrationUser(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void delCourier() {
        UserMethods.delUser(accessToken);
    }
}
