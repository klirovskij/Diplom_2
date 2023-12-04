package usertest;

import user.User;
import user.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class EditUserTest {
    private static final String email = "useremail";
    private static final String password = "unipasword";
    private static final String name = "unnamedd";
    String accessToken;

    @Test
    @DisplayName("Изменение данных пользователя")
    @Description("логин под существующим пользователем")
    public void editUserWithAuthorization() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, name + num);
        UserMethods.registrationUser(user);

        accessToken = UserMethods.login(user).then().extract().path("accessToken").toString();

        User newUser = new User("new" + email + num + "@domain.ru", "new" + password + num, "new" + name + num);
        Response response = UserMethods.editUser(newUser, accessToken);

        response.then().assertThat().statusCode(200)
                .and()
                .body("user.email", equalTo("new" + email + num + "@domain.ru"))
                .and()
                .body("user.name", equalTo("new" + name + num));
    }

    @Test
    @DisplayName("Изменение данных пользователя")
    @Description("логин под существующим пользователем")
    public void editUserWithoutAuthorization() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, name + num);
        UserMethods.registrationUser(user);

        accessToken = "";//UserMethods.login(user).then().extract().path("accessToken").toString();

        User newUser = new User("new" + email + num + "@domain.ru", "new" + password + num, "new" + name + num);
        Response response = UserMethods.editUser(newUser, accessToken);

        response.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void delCourier() {
        UserMethods.delUser(accessToken);
    }
}
