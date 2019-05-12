package ovh.miroslaw.shoppinglist.rest.ingredient;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IngredientResourceTestIT extends TestCase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getIngredient_shouldReturn200() {
        ResponseEntity<IngredientDTO> response = restTemplate.getForEntity(API_VERSION + "/ingredients/1",
            IngredientDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .extracting(IngredientDTO::getName, IngredientDTO::getAmount, IngredientDTO::getPopularity)
            .containsExactly("bread", 1F, 1);
    }

    @Test
    void getIngredient_shouldReturn404() {
        ResponseEntity<IngredientDTO> response = restTemplate.getForEntity(API_VERSION + "/ingredients/11",
            IngredientDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllIngredients_shouldReturn200() {

        ParameterizedTypeReference<List<String>> type = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<String>> response = restTemplate.exchange(API_VERSION + "/ingredients", HttpMethod.GET,
            null, type);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly("bread", "carrots", "chicken", "egg", "noodles", "oil",
            "parmesan", "peanut butter");
    }
}
