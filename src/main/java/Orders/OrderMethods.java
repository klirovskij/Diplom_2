package Orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static Constants.URLs.BASE_URL;
import static Constants.URLs.ORDER_PATH;
import static Constants.URLs.*;
import static io.restassured.RestAssured.given;


public class OrderMethods {
    @Step("Создание уникального заказа")
    public static Response newOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(BASE_URL + ORDER_PATH);
    }

    @Step("Получить заказы конкретного пользователя")
    public static Response getOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("authorization", accessToken)
                .get(BASE_URL + ORDER_PATH);
    }

}
