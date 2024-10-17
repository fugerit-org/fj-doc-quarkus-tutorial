package test.org.fugerit.java.demo.fjdocquarkustutorial;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

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
    void testAsciiDoc() {
        given().when().get("/doc/example.adoc").then().statusCode(200);
    }
    @Test
    void testPdf() {
        given().when().get("/doc/example.pdf?sourceType=2").then().statusCode(200);
    }
    @Test
    void testXlsx() {
        given().when().get("/doc/example.xlsx").then().statusCode(200);
    }
    @Test
    void testCsv() {
        given().when().get("/doc/example.csv?sourceType=9").then().statusCode(200);
    }
    @Test
    void testOpenPDF() {
        given().when().get("/doc/openpdf/example.pdf?sourceType=3").then().statusCode(200);
    }
    @Test
    void testOpenPDFHTML() {
        given().when().get("/doc/openpdf/example.html").then().statusCode(200);
    }
    @Test
    void testOpenRTF() {
        given().when().get("/doc/example.rtf").then().statusCode(200);
    }
    @Test
    void testPdfA1b() {
        given().when().get("/doc/pdfa1b/example.pdf").then().statusCode(200);
    }
}