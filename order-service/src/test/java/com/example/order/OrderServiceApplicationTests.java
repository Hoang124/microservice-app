package com.example.order;

import com.example.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@Autowired
	MySQLContainer mySQLContainer;

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		// Set the base URI for RestAssured
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldCreateOrder() {
		String orderJson = """
				{
					"skuCode": "iphone_15",
					"quantity": 2,
					"price": 1000.0
				}
				""";
		InventoryClientStub.stubInventoryCall("iphone_15", 2);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(orderJson)
				.when()
				.post("/api/orders")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
	}

}
