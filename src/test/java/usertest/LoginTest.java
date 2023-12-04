package usertest;

import user.User;
import user.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginTest {
    private static final String email = "useremail";
    private static final String password = "unipasword";
    private static final String name = "unnamedd";
    String accessToken;

    @Test
    @DisplayName("Логин пользователя")
    @Description("логин под существующим пользователем")
    public void loginUser() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, name + num);
        UserMethods.registrationUser(user);
        User userReg = new User(email + num + "@domain.ru", password + num);
        Response response = UserMethods.login(userReg);
        accessToken = response.then().extract().path("accessToken").toString();
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин пользователя")
    @Description("логин с неверным логином")
    public void loginWithBadUser() {
        int num = UserMethods.rndNum();
        User user = new User("", password + num);
        Response response = UserMethods.login(user);
        response.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя")
    @Description("логин с неверным паролем")
    public void loginWithBadPass() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", "");
        Response response = UserMethods.login(user);
        response.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void delCourier() {
        UserMethods.delUser(accessToken);
    }
}
