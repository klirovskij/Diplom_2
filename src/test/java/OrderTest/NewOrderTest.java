package OrderTest;

import Orders.Order;

import Orders.OrderMethods;
import User.User;
import User.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class NewOrderTest {

    private static final String email = "useremail";
    private static final String password = "unipasword";
    private static final String name = "unnamedd";
    private static final String[] availableIngredients = new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"};
    private static final String[] notAvailableIngredients = new String[]{"61c0aa6d"};
    String accessToken;

    @Test
    @DisplayName("Создание заказа")
    @Description("с авторизацией \n с ингредиентами")
    public void newOrderWithAuthorizatioTest() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, name + num);
        UserMethods.registrationUser(user);

        accessToken = UserMethods.login(user).then().extract().path("accessToken").toString();

        Order order = new Order(availableIngredients);
        Response response = OrderMethods.newOrder(order);

        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("без авторизации \n с ингредиентами")
    public void newOrderWithoutAuthorization() {
        Order order = new Order(availableIngredients);
        Response response = OrderMethods.newOrder(order);

        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("без авторизации \n без ингредиентов")
    public void newOrderEmptyIngredients() {
        Order order = new Order(null);
        Response response = OrderMethods.newOrder(order);

        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("с неверным хешем ингредиентов")
    public void newOrderNotAvailableIngredients() {
        Order order = new Order(notAvailableIngredients);
        Response response = OrderMethods.newOrder(order);

        response.then().assertThat().statusCode(500);
    }

    @After
    public void delCourier() {
        UserMethods.delUser(accessToken);
    }
}
