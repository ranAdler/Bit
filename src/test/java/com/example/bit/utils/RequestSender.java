package com.example.bit.utils;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestSender {

    private String baseUrl;
    private Gson gson;
    private static final String CONTENT_TYPE = "application/json";

    public RequestSender(String baseUrl) {
        this.baseUrl = baseUrl;
        this.gson = new Gson();
    }

    public <T> T post(String endpoint, Object requestBody, Class<T> responseClass) {
        return sendRequest(endpoint, "POST", requestBody, responseClass, HttpStatusCode.SUCCESS.getCode());
    }


    public <T> T post(String endpoint, Object requestBody, Class<T> responseClass , HttpStatusCode  httpStatusCode) {
        return sendRequest(endpoint, "POST", requestBody, responseClass, httpStatusCode.getCode());
    }

    public <T> T put(String endpoint, Object requestBody, Class<T> responseClass) {
        return sendRequest(endpoint, "PUT", requestBody, responseClass, HttpStatusCode.SUCCESS.getCode());
    }

    public <T> T patch(String endpoint, Object requestBody, Class<T> responseClass) {
        return sendRequest(endpoint, "PATCH", requestBody, responseClass, HttpStatusCode.SUCCESS.getCode());
    }

    public <T> T get(String endpoint, Class<T> responseClass) {
        return sendRequest(endpoint, "GET", null, responseClass, HttpStatusCode.SUCCESS.getCode());
    }

    public <T> T delete(String endpoint, Class<T> responseClass) {
        return sendRequest(endpoint, "DELETE", null, responseClass, HttpStatusCode.SUCCESS.getCode());
    }

    private <T> T sendRequest(String endpoint, String httpMethod, Object requestBody, Class<T> responseClass, int httpStatusCode) {
        String payload = requestBody != null ? gson.toJson(requestBody) : null;

        RequestSpecification spec = RestAssured
                .given()
                .baseUri(baseUrl)
                .header("Content-Type", CONTENT_TYPE);

        if (payload != null) {
            spec.body(payload);
        }

        Response response = switch (httpMethod.toUpperCase()) {
            case "POST" -> spec.when().post(endpoint).then().statusCode(httpStatusCode).extract().response();
            case "PUT" -> spec.when().put(endpoint).then().statusCode(httpStatusCode).extract().response();
            case "PATCH" -> spec.when().patch(endpoint).then().statusCode(httpStatusCode).extract().response();
            case "GET" -> spec.when().get(endpoint).then().statusCode(httpStatusCode).extract().response();
            case "DELETE" -> spec.when().delete(endpoint).then().statusCode(httpStatusCode).extract().response();
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        };

        return gson.fromJson(response.getBody().asString(), responseClass);
    }
}