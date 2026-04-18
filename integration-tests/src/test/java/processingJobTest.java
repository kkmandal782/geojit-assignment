import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class processingJobTest {



    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:8080";
    } //baseurl

        @Test
        void testSubmitJob_success() {
            String requestBody = """
        {
            "name": "job33",
             "job_creation_time": "2026-04-15"
        }
        """;

            Response response = given()
                    .auth().basic("admin", "password")
                    .contentType("application/json")
                    .body(requestBody)
                    .when()
                    .post("/submit")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Queued For Https and inserted in db!!"))
                    .extract()
                    .response();
            System.out.println("Response::"+response.asPrettyString());

        }

    @Test
    void testNoAuth() {
        given()
                .when()
                .post("/submit")
                .then()
                .statusCode(401);
    }

    @Test
    void testUserCannotAccessJobs() {
        given()
                .auth().basic("user", "password")
                .when()
                .get("/jobs")
                .then()
                .statusCode(401);
    }

    @Test
    void testAdminAccessJobs() {
        given()
                .auth().basic("admin", "password")
                .when()
                .get("/jobs")
                .then()
                .statusCode(200);
    }

    @Test
    void testSubmitJob_validationFailure() {
        String requestBody = """
    {
        "name": "",
        "job_creation_time": "2026-04-15"
    }
    """;

      Response response =            given()
                .auth().basic("admin","password")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/submit")
                .then()
                .statusCode(400)
                .body("name", equalTo("Processing Name is required")) // depends on your validation message
                .extract()
                .response();
        System.out.println("Response2:"+response.asPrettyString());
    }

    @Test
    void testSubmitJob_duplicateJob() {
        String requestBody = """
    {
        "name": "job7",
        "job_creation_time": "2026-04-15"
    }
    """;

        Response response = given()
                .auth().basic("admin", "password")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/submit")
                .then()
                .statusCode(400)
                .body("message", equalTo("Processing Job already exists"))
                .extract()
                .response();
        System.out.println("Response 3 "+response.asPrettyString());
    }

    @Test
    void testSubmitJob_invalidDateFormat() {
        String requestBody = """
    {
        "name": "job8",
        "job_creation_time": "15-04-2026"
    }
    """;

        Response response = given()
                .auth().basic("admin", "password")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/submit")
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid Date Format"))
                .extract().response();
        System.out.println("Response 4:"+response.asPrettyString());
//                .body("message", containsString("yyyy-MM-dd"));
    }

    @Test
    void testSubmitJob_loopCalls() throws InterruptedException {

        for (int i = 35; i < 45; i++) {
            String requestBody = """
        {
            "name": "job-%d",
            "job_creation_time": "2026-04-15"
        }
        """.formatted(i);

            given()
                    .auth().basic("admin", "password")
                    .contentType("application/json")
                    .body(requestBody)
                    .when()
                    .post("/submit")
                    .then()
                    .statusCode(200);

            Thread.sleep(500); // avoid hammering server
        }
    }

    @Test
    void testRedisCacheImprovesResponseTime() {

        // FIRST CALL (likely DB hit)
        long firstCallTime = given()
                .auth().basic("admin", "password")
                .when()
                .get("/jobs")
                .time();

        // SECOND CALL (likely cache hit)
        long secondCallTime = given()
                .auth().basic("admin", "password")
                .when()
                .get("/jobs")
                .time();

        long thirdCallTime = given()
                .auth().basic("admin", "password")
                .when()
                .get("/jobs")
                .time();

        System.out.println("First call: " + firstCallTime + " ms");
        System.out.println("Second call: " + secondCallTime + " ms");
        System.out.println("Third call: " + thirdCallTime + " ms");

        // basic assertion (loose but useful)
        assert secondCallTime <= firstCallTime;
    }

}
