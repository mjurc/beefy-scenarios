package io.quarkus.qe.multiplepus;

import io.quarkus.qe.multiplepus.containers.MariaDbDatabaseTestResource;
import io.quarkus.qe.multiplepus.containers.PostgreSqlDatabaseTestResource;
import io.quarkus.qe.multiplepus.model.fruit.Fruit;
import io.quarkus.qe.multiplepus.model.vegetable.Vegetable;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@QuarkusTestResource(MariaDbDatabaseTestResource.class)
@QuarkusTestResource(PostgreSqlDatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiplePersistenceUnitTest {

    @Test
    @Order(1)
    public void getAllFruits() {
        when()
                .get("/fruit")
        .then()
                .statusCode(200)
                .body("", hasSize(7));
    }

    @Test
    @Order(2)
    public void getFruitById() {
        when()
                .get("/fruit/7")
        .then()
                .statusCode(200)
                .body("name", equalTo("Cranberry"));
    }

    @Test
    @Order(3)
    public void createFruit() {
        Fruit fruit = new Fruit();
        fruit.name = "Canteloupe";

        given()
        .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruit")
        .then()
                .statusCode(201)
                .body("id", equalTo(8))
                .body("name", equalTo("Canteloupe"));

        when()
                .get("/fruit/8")
        .then()
                .statusCode(200)
                .body("name", equalTo("Canteloupe"));
    }

    @Test
    @Order(4)
    public void createInvalidPayloadFruit() {
        given()
        .when()
                .contentType(ContentType.TEXT)
                .body("")
                .post("/fruit")
        .then()
                .statusCode(415)
                .body("code", equalTo(415));
    }

    @Test
    @Order(5)
    public void createBadPayloadFruit() {
        Fruit fruit = new Fruit();
        fruit.id = 999L;
        fruit.name = "foo";

        given()
        .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruit")
        .then()
                .statusCode(422)
                .body("code", equalTo(422))
                .body("error", equalTo("unexpected ID in request"));
    }

    @Test
    @Order(6)
    public void updateFruit() {
        Fruit fruit = new Fruit();
        fruit.id = 8L;
        fruit.name = "Dragonfruit";

        given()
        .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .put("/fruit/8")
        .then()
                .statusCode(200)
                .body("id", equalTo(8))
                .body("name", equalTo("Dragonfruit"));

        when()
                .get("/fruit/8")
        .then()
                .statusCode(200)
                .body("name", equalTo("Dragonfruit"));
    }

    @Test
    @Order(7)
    public void updateFruitWithUnknownId() {
        Fruit fruit = new Fruit();
        fruit.id = 999L;
        fruit.name = "foo";

        given()
        .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .put("/fruit/999")
        .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("error", equalTo("fruit '999' not found"));
    }

    @Test
    @Order(8)
    public void updateFruitInvalidPayload() {
        given()
        .when()
                .contentType(ContentType.TEXT)
                .body("")
                .put("/fruit/8")
        .then()
                .statusCode(415)
                .body("code", equalTo(415));
    }

    @Test
    @Order(9)
    public void updateFruitBadPayload() {
        Fruit fruit = new Fruit();

        given()
        .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .put("/fruit/8")
        .then()
                .statusCode(422)
                .body("code", equalTo(422))
                .body("error.message", contains("Fruit name must be set!"));
    }

    @Test
    @Order(10)
    public void deleteFruit() {
        when()
                .delete("/fruit/8")
        .then()
                .statusCode(204);

        when()
                .get("/fruit/8")
        .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("error", equalTo("fruit '8' not found"));
    }

    @Test
    @Order(11)
    public void deleteFruitWithUnknownId() {
        when()
                .delete("/fruit/999")
        .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("error", equalTo("fruit '999' not found"));
    }

    @Test
    @Order(12)
    public void getAllVegetables() {
        when()
                .get("/vegetable")
                .then()
                .statusCode(200)
                .body("", hasSize(7));
    }

    @Test
    @Order(13)
    public void getVegetableById() {
        when()
                .get("/vegetable/7")
                .then()
                .statusCode(200)
                .body("name", equalTo("Garlic"));
    }

    @Test
    @Order(14)
    public void createVegetable() {
        Vegetable vegetable = new Vegetable();
        vegetable.name = "Eggplant";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .post("/vegetable")
                .then()
                .statusCode(201)
                .body("id", equalTo(8))
                .body("name", equalTo("Eggplant"));

        when()
                .get("/vegetable/8")
                .then()
                .statusCode(200)
                .body("name", equalTo("Eggplant"));
    }

    @Test
    @Order(15)
    public void createVegetableInvalidPayload() {
        given()
                .when()
                .contentType(ContentType.TEXT)
                .body("")
                .post("/vegetable")
                .then()
                .statusCode(415)
                .body("code", equalTo(415));
    }

    @Test
    @Order(16)
    public void createVegetableBadPayload() {
        Vegetable vegetable = new Vegetable();
        vegetable.id = 999L;
        vegetable.name = "foo";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .post("/vegetable")
                .then()
                .statusCode(422)
                .body("code", equalTo(422))
                .body("error", equalTo("unexpected ID in request"));
    }

    @Test
    @Order(17)
    public void updateVegetable() {
        Vegetable vegetable = new Vegetable();
        vegetable.id = 8L;
        vegetable.name = "Okra";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .put("/vegetable/8")
                .then()
                .statusCode(200)
                .body("id", equalTo(8))
                .body("name", equalTo("Okra"));

        when()
                .get("/vegetable/8")
                .then()
                .statusCode(200)
                .body("name", equalTo("Okra"));
    }

    @Test
    @Order(18)
    public void updateVegetableWithUnknownId() {
        Vegetable vegetable = new Vegetable();
        vegetable.id = 999L;
        vegetable.name = "foo";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .put("/vegetable/999")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("error", equalTo("vegetable '999' not found"));
    }

    @Test
    @Order(19)
    public void updateVegetableInvalidPayload() {
        given()
                .when()
                .contentType(ContentType.TEXT)
                .body("")
                .put("/vegetable/8")
                .then()
                .statusCode(415)
                .body("code", equalTo(415));
    }

    @Test
    @Order(20)
    public void updateVegetableBadPayload() {
        Vegetable vegetable = new Vegetable();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .put("/vegetable/8")
                .then()
                .statusCode(422)
                .body("code", equalTo(422))
                .body("error.message", contains("Vegetable name must be set!"));
    }

    @Test
    @Order(21)
    public void deleteVegetable() {
        when()
                .delete("/vegetable/8")
                .then()
                .statusCode(204);

        when()
                .get("/vegetable/8")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("error", equalTo("vegetable '8' not found"));
    }

    @Test
    @Order(22)
    public void deleteVegetableWithUnknownId() {
        when()
                .delete("/vegetable/999")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("error", equalTo("vegetable '999' not found"));
    }
}
