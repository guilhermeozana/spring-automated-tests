package br.com.microservice.integration.swagger;

import br.com.microservice.config.TestConfig;
import br.com.microservice.integration.testcontainer.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	@DisplayName("GivenBaseSwaggerPath_whenGetRequest_thenReturnSwaggerPage")
	void integrationTestGivenBaseSwaggerPath_whenGetRequest_thenReturnSwaggerPage() {
		var content = given()
						.basePath("/swagger-ui/index.html")
						.port(TestConfig.SERVER_PORT)
					  .when()
						.get()
				      .then()
						.statusCode(200)
				      .extract()
						.body()
						.asString();

		assertTrue(content.contains("Swagger UI"));
	}

}
