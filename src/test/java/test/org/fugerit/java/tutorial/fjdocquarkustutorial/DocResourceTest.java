package test.org.fugerit.java.tutorial.fjdocquarkustutorial;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class DocResourceTest {

    @Test
    void testMarkdown() {
        given().when().get("/doc/example.md").then().statusCode(200);
    }

    @Test
    void testHtml() {
        given().when().get("/doc/example.html").then().statusCode(200);
    }

    @Test
    void testPdf() {
        given().when().get("/doc/example.pdf").then().statusCode(200);
    }
    @Test
    void testXlsx() {
        given().when().get("/doc/example.xlsx").then().statusCode(200);
    }

}