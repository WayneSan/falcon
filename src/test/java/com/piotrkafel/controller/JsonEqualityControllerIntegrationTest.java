package com.piotrkafel.controller;

import com.jayway.restassured.http.ContentType;
import com.piotrkafel.WebInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class JsonEqualityControllerIntegrationTest {

    public static final String BODY_NOT_EQUAL_JSONS = "json1={\"a\":1, \"b\":3}&json2={\"b\":3, \"a\":2}",
                               BODY_EQUAL_JSONS = "json1={\"a\":1, \"b\":3}&json2={\"b\":3, \"a\":1}",
                               BODY_MALFORMED_JSONS = "json1={\"a\":1, \"b\"}&json2={\"b\":3, \"a\":1}";

    public static final String URL = "http://localhost:4567/json/compare";

    @BeforeClass
    public static void beforeTest() {
        WebInitializer.main(new String[0]);
    }

    @AfterClass
    public static void afterTest() {
        Spark.stop();
    }

    @Test
    public void falseWhenNotEqualJsons() {
        given()
            .contentType(ContentType.URLENC)
            .body(BODY_NOT_EQUAL_JSONS)
        .when()
            .post(URL)
        .then()
            .statusCode(200)
            .body(containsString("false"));
    }

    @Test
    public void trueWhenEqualJsons() {
        given()
            .contentType(ContentType.URLENC)
            .body(BODY_EQUAL_JSONS)
        .when()
            .post(URL)
        .then()
            .statusCode(200)
            .body(containsString("true"));
    }

    @Test
    public void shouldReturn400WhenMalformedDataSent() {
        given()
            .contentType(ContentType.URLENC)
            .body(BODY_MALFORMED_JSONS)
        .when()
            .post(URL)
        .then()
            .statusCode(400);
    }
}
