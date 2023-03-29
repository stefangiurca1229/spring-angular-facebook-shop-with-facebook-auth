package com.springangularfacebook.oauth2;

import io.restassured.filter.cookie.CookieFilter;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Uuid {
private CookieFilter cookieFilter;
public Uuid(){
    cookieFilter = new CookieFilter();
}
@Test
public void sendUuid(){
    given()
            .relaxedHTTPSValidation()
            .filter(cookieFilter)
            .baseUri("https://192.168.1.226:1111/uuid")
            .queryParam("uuid","test")
            .when()
            .post()
            .then()
            .log()
            .body()
            .assertThat()
            .statusCode(200);
}
}
