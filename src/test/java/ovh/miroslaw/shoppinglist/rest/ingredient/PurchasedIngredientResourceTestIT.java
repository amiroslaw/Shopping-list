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
import org.springframework.test.context.jdbc.Sql;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientDTOWithNameAndAmount;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientUnitDTO;
import static ovh.miroslaw.shoppinglist.builders.TestUtils.getRequest;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchasedIngredientResourceTestIT {

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
    @Sql(statements = "DELETE FROM SHOPPING_PURCHASED_INGREDIENT  where  INGREDIENT_ID = 9;",
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createIngredient_shouldReturn201() {
        final IngredientDTO expected = make(
            ingredientDTOWithNameAndAmount("milk", 3F).but(with(ingredientUnitDTO, "liter")));

        final ResponseEntity<IngredientDTO> response = rest.exchange(API_VERSION + "/shopping-lists/1/ingredients",
            POST,
            getRequest(idToken, expected), IngredientDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        assertThat(response.getBody()).extracting(IngredientDTO::getName, IngredientDTO::getAmount,
                IngredientDTO::getUnit)
            .containsExactly(expected.getName(), expected.getAmount(), expected.getUnit());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findUserPurchasedIngredients_shouldReturn200() {
        final ResponseEntity<List<IngredientDTO>> response = getIngResponse();
        assertThat(response.getStatusCode()).isEqualTo(OK);

        assertThat(response.getBody()).hasSize(2).extracting(IngredientDTO::getName).contains("egg", "noodles");
    }

    @Test
    void addIngredientToShoppingList_shouldReturn201() {
        final ResponseEntity<List<IngredientDTO>> responseGet = getIngResponse();
        assertThat(responseGet.getBody()).extracting(IngredientDTO::getName).contains("egg");

        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/purchased-lists/1/ingredients/5", POST,
            getRequest(idToken), String.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        final ResponseEntity<List<IngredientDTO>> responseGetShoppingList = getIngResponse(
            "/shopping-lists/1/ingredients");
        assertThat(responseGetShoppingList.getBody()).extracting(IngredientDTO::getName).contains("egg");
    }

    @Test
    void addIngredientToUserList_shouldReturn200() {
        final ResponseEntity<List<IngredientDTO>> responseGet = getIngResponse();
        assertThat(responseGet.getBody()).extracting(IngredientDTO::getName).contains("egg");

        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/purchased-lists/1/ingredients/5", PUT,
            getRequest(idToken), String.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        final ResponseEntity<List<IngredientDTO>> responseGetUserList = getIngResponse("/user-ingredients");
        assertThat(responseGetUserList.getBody()).extracting(IngredientDTO::getName).contains("egg");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void deleteIngredient_shouldReturn204() {
        final ResponseEntity<List<IngredientDTO>> responseGet = getIngResponse();
        assertThat(responseGet.getBody()).extracting(IngredientDTO::getName).contains("egg");

        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/purchased-lists/1/ingredients/5", DELETE,
            getRequest(idToken), String.class);
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);

        final ResponseEntity<List<IngredientDTO>> responseGetAfterDelete = getIngResponse();
        assertThat(responseGetAfterDelete.getBody()).extracting(IngredientDTO::getName).doesNotContain("egg");

    }

    @Test
    void deleteIngredient_shouldReturn404() {
        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/purchased-lists/1/ingredients/45", DELETE,
            getRequest(idToken), String.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private ResponseEntity<List<IngredientDTO>> getIngResponse(String path) {
        ParameterizedTypeReference<List<IngredientDTO>> type = new ParameterizedTypeReference<>() {
        };
        return rest.exchange(API_VERSION + path, GET,
            getRequest(idToken), type);
    }

    private ResponseEntity<List<IngredientDTO>> getIngResponse() {
        return getIngResponse("/purchased-lists/1/ingredients");
    }

}
