package ovh.miroslaw.shoppinglist.rest.ingredient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ovh.miroslaw.shoppinglist.domain.LoginVM;
import ovh.miroslaw.shoppinglist.rest.UserJWTController.JWTToken;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.net.URI;
import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientDTOWithNameAndAmount;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientIdDTO;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientUnitDTO;
import static ovh.miroslaw.shoppinglist.builders.TestUtils.getRequest;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIngredientResourceTestIT {

    private static String idToken;

    @Autowired
    private TestRestTemplate rest;

    @BeforeAll
    static void login(@Autowired TestRestTemplate rest) {
        final LoginVM credentials = new LoginVM("hulio", "passff", true);
        RequestEntity<LoginVM> request = RequestEntity.post(URI.create(API_VERSION + "/authenticate"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(credentials);

        ResponseEntity<JWTToken> response = rest.exchange(request, JWTToken.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        idToken = response.getBody().getIdToken();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findUserIngredients_shouldReturn200() {
        final ResponseEntity<List<IngredientDTO>> response = getUserIngResponse("/user-ingredients");
        assertThat(response.getStatusCode()).isEqualTo(OK);

        assertThat(response.getBody()).hasSize(3).extracting(IngredientDTO::getName)
            .contains("oil", "carrots", "noodles");
    }

    @Test
    void findUserIngredients_shouldReturn403() {
        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/user-ingredients", GET,
            null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @Test
    void createUserIngredient_shouldReturn201() {
        final IngredientDTO expected = make(
            ingredientDTOWithNameAndAmount("milk", 3F).but(with(ingredientUnitDTO, "liter")));

        final ResponseEntity<IngredientDTO> response = rest.exchange(API_VERSION + "/user-ingredients", POST,
            getRequest(idToken, expected), IngredientDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        assertThat(response.getBody()).extracting(IngredientDTO::getName, IngredientDTO::getAmount,
                IngredientDTO::getUnit)
            .containsExactly(expected.getName(), expected.getAmount(), expected.getUnit());
    }

    @Test
    void updateIngredient_shouldReturn200() {
        final IngredientDTO expected = make(
            ingredientDTOWithNameAndAmount("palm oil", 3F).but(with(ingredientIdDTO, 3L)));

        final ResponseEntity<IngredientDTO> response = rest.exchange(API_VERSION + "/user-ingredients/3", PUT,
            getRequest(idToken, expected), IngredientDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        assertThat(response.getBody()).extracting(IngredientDTO::getName, IngredientDTO::getAmount,
                IngredientDTO::getUnit)
            .contains(expected.getName(), expected.getAmount(), expected.getUnit());
    }

    @Test
    void addIngredientToShoppingList_shouldAdd() {
        final ResponseEntity<String> responseAdd =
            rest.exchange(API_VERSION + "/user-ingredients/6", POST,
                getRequest(idToken), String.class);
        assertThat(responseAdd.getStatusCode()).isEqualTo(CREATED);

        final ResponseEntity<List<IngredientDTO>> responseGet = getUserIngResponse("/shopping-lists/1/ingredients");
        assertThat(responseGet.getStatusCode()).isEqualTo(OK);
        assertThat(responseGet.getBody()).extracting(IngredientDTO::getName).contains("noodles");
    }

    @Test
    void deleteIngredient_shouldReturn204() {
        final ResponseEntity<List<IngredientDTO>> responseGet = getUserIngResponse("/user-ingredients");
        assertThat(responseGet.getBody()).extracting(IngredientDTO::getName).contains("oil");

        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/user-ingredients/3", DELETE,
            getRequest(idToken), String.class);
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);

        final ResponseEntity<List<IngredientDTO>> responseGetAfterDelete = getUserIngResponse("/user-ingredients");
        assertThat(responseGetAfterDelete.getBody()).extracting(IngredientDTO::getName).doesNotContain("oil");
    }

    @Test
    void deleteIngredient_shouldReturn401() {
        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/user-ingredients/1", DELETE,
            null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    private ResponseEntity<List<IngredientDTO>> getUserIngResponse(String path) {
        ParameterizedTypeReference<List<IngredientDTO>> type = new ParameterizedTypeReference<>() {
        };
        return rest.exchange(API_VERSION + path, GET,
            getRequest(idToken), type);
    }
}
