package ovh.miroslaw.shoppinglist.rest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UnitOfMeasureResourceTestIT {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void getAllUOM_shouldReturn200() {
        ParameterizedTypeReference<List<String>> type = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<String>> response = rest.exchange(API_VERSION + "/unit-of-measures", HttpMethod.GET,
            null, type);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody())
            .contains("teaspoon", "pinch", "piece", "liter", "ml", "kg", "grams",
                "tablespoon");
    }
}
