package com.springangularfacebook.oauth2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.Base64;

import static io.restassured.RestAssured.given;

//@PropertySource("classpath:application.yaml")
public class GetAuthCode {
    private final String clientId="1224681105055584";
    private final String redirectUri="https://stefy.ocl.ro/callback";
    private final String scope="public_profile";
    private String username="0771564989";
    private String password="Masterbootrecord2";

    public static String encode(String str1, String str2) {
        return new String(Base64.getEncoder().encode((str1 + ":" + str2).getBytes()));
    }

    public Response getCode() {
        String authorization = encode(username, password);

        return  given()
                        .relaxedHTTPSValidation()
                        .header("authorization", "Basic " + authorization)
                        .contentType(ContentType.URLENC)
                        .formParam("response_type", "code")
                        .queryParam("client_id", clientId)
              //          .queryParam("redirect_uri", redirectUri)
                        .queryParam("scope", scope)
                        .post("/oauth2/authorization/facebook")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
    }
    public String parseForOAuth2Code(Response response) {
        return response.jsonPath().getString("code");
    }

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://192.168.1.226:1111";
    }

    @Test
    public void iShouldGetCode() {
        Response response = getCode();
        String code = parseForOAuth2Code(response);

        Assertions.assertNotNull(code);
    }
}
