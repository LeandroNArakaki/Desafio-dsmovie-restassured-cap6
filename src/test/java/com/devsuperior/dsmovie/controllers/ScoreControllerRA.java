package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ScoreControllerRA {

    private Double existingScore, nonExistingScore;
    private Long existingMovie, nonExistingMovie;
    private String adminToken, clientToken, invalidToken;
    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private Map<String, Object> postScore;


    @BeforeEach
    void setUp() throws Exception {
        baseURI = "http://localhost:8080";

        existingScore = 4.5;
        nonExistingScore = 10.0;

        existingMovie = 1L;
        nonExistingMovie = 100L;

        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        clientUsername = "alex@gmail.com";
        clientPassword = "123456";

        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        invalidToken = adminToken + "invalid";

        postScore = new HashMap<>();
        postScore.put("movieId", existingMovie);
        postScore.put("score", existingScore);

    }

    @Test
    public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
        postScore.put("movieId", nonExistingMovie);
        JSONObject newScore = new JSONObject(postScore);

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newScore)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(404);
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
        postScore.put("movieId", null);
        JSONObject newScore = new JSONObject(postScore);

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newScore)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(422);
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
        postScore.put("score", -1.0);
        JSONObject newScore = new JSONObject(postScore);

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newScore)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(422);
    }
}
