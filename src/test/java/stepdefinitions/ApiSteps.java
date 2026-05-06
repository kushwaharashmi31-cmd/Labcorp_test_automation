package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiSteps {
    Response response;

    @Given("I send GET request")
    public void send_get() {
        response = given().when().get("https://echo.free.beeceptor.com/sample-request?author=beeceptor");
    }

    @Then("I validate response fields")
    public void validate_get() {
        response.then().statusCode(anyOf(is(200), is(429)))
                .body("path", notNullValue())
                .body("ip", notNullValue())
                .body("headers", notNullValue());
    }

    @Given("I send POST request")
    public void send_post() {
        String payload = "{ \"order_id\": \"12345\", \"customer\": {\"name\": \"Jane Smith\"}}";
        response = given().contentType(ContentType.JSON).body(payload)
                   .post("https://echo.free.beeceptor.com/sample-request?author=beeceptor");
    }

    @Then("I validate POST response")
    public void validate_post() {
        response.then().statusCode(anyOf(is(200), is(429)))
                .body("parsedBody.order_id", equalTo("12345"))
                .body("parsedBody.customer.name", equalTo("Jane Smith"));
    }
}
