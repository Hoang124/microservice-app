package com.example.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@Autowired
	MongoDBContainer mongoDBContainer;

	@LocalServerPort
	int port;


	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
//		if (!mongoDBContainer.isRunning()) {
//			mongoDBContainer.start();
//		}
	}

	@Test
	void shouldCreateProduct() {
		String productJson = """
				{
					"name": "Test Product",
					"description": "This is a test product",
					"price": 19.99
				}
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(productJson)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Test Product"))
				.body("description", Matchers.equalTo("This is a test product"))
				.body("price", Matchers.equalTo(19.99f));
	}

}
