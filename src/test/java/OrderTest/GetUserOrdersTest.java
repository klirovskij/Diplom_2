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

public class GetUserOrdersTest {
    private static final String email = "useremail";
    private static final String password = "unipasword";
    private static final String name = "unnamedd";
    private static final String[] oneAvailableIngredients = new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"};
    private static final String[] twoAvailableIngredients = new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"};
    String accessToken;

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    @Description("авторизованный пользователь")
    public void getOrdersAuthorizationUserTest() {
        int num = UserMethods.rndNum();
        User user = new User(email + num + "@domain.ru", password + num, name + num);
        UserMethods.registrationUser(user);

        accessToken = UserMethods.login(user).then().extract().path("accessToken").toString();

        Order order = new Order(oneAvailableIngredients);
        Order order2 = new Order(twoAvailableIngredients);
        OrderMethods.newOrder(order);
        OrderMethods.newOrder(order2);

        Response response = OrderMethods.getOrders(accessToken);

        response.then().assertThat().statusCode(200)
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    @Description("неавторизованный пользователь")
    public void getOrdersNotAuthorizationUserTest() {
        Order order = new Order(oneAvailableIngredients);
        OrderMethods.newOrder(order);

        Response response = OrderMethods.getOrders("");

        response.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void delCourier() {
        UserMethods.delUser(accessToken);
    }
}
