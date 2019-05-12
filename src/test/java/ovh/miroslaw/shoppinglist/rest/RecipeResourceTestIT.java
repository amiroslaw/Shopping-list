package ovh.miroslaw.shoppinglist.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.LoginVM;
import ovh.miroslaw.shoppinglist.rest.UserJWTController.JWTToken;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import java.net.URI;

import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static ovh.miroslaw.shoppinglist.builders.RecipeTestBuilder.recipeDTO;
import static ovh.miroslaw.shoppinglist.builders.RecipeTestBuilder.recipeDTOWithTitleAndVisibility;
import static ovh.miroslaw.shoppinglist.builders.TestUtils.getRequest;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeResourceTestIT {

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
    void getUserRecipeRecommendation_shouldReturn200() {
        ParameterizedTypeReference<PagedResources<RecipeDTO>> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<PagedResources<RecipeDTO>> response = rest.exchange(
            API_VERSION + "/recipes?recommendation=true",
            GET, getRequest(idToken), type);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void getUserRecipeRecommendation_shouldReturn403() {
        ParameterizedTypeReference<PagedResources<RecipeDTO>> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<PagedResources<RecipeDTO>> response = rest.exchange(
            API_VERSION + "/recipes?recommendation=true",
            GET, null, type);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @Test
    void getUserRecipe_shouldReturn200() {
        ParameterizedTypeReference<PagedResources<RecipeDTO>> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<PagedResources<RecipeDTO>> response = rest.exchange(
            API_VERSION + "/recipes?recommendation=false",
            GET, null, type);
        assertThat(response.getStatusCode()).isEqualTo(OK);
//        assertThat(response.getBody().getContent()).hasSize(3);
    }

    @Test
    void getPublicRecipe_shouldReturn200() {
        ParameterizedTypeReference<PagedResources<RecipeDTO>> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<PagedResources<RecipeDTO>> response = rest.exchange(
            API_VERSION + "/recipes/public?recommendation=false",
            GET, null, type);
        assertThat(response.getStatusCode()).isEqualTo(OK);
//        assertThat(response.getBody().getContent()).hasSize(3);
    }

    @Disabled
    @Test
    void getPublicRecipeWithFilter_shouldReturn200() {
        ParameterizedTypeReference<PagedResources<RecipeDTO>> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<PagedResources<RecipeDTO>> response = rest.exchange(
            API_VERSION + "/recipes/public?recommendation=false&filter=sandwitch",
            GET, null, type);
        assertThat(response.getBody().getContent()).hasSize(1).extracting(RecipeDTO::getTitle).contains("Chicken Soup");
    }

    @Test
    void getPublicRecipeRecommendation_shouldReturn200() {
        ParameterizedTypeReference<PagedResources<RecipeDTO>> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<PagedResources<RecipeDTO>> response = rest.exchange(
            API_VERSION + "/recipes/public?recommendation=true",
            GET, getRequest(idToken), type);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void getPublicRecipeRecommendation_shouldReturn403() {
        ParameterizedTypeReference<PagedResources<RecipeDTO>> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<PagedResources<RecipeDTO>> response = rest.exchange(
            API_VERSION + "/recipes/public?recommendation=true",
            GET, null, type);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @Test
    void createRecipe_shouldReturn201() {
        final RecipeDTO expected = make(recipeDTOWithTitleAndVisibility("apple pie", true));

        ParameterizedTypeReference<RecipeDTO> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<RecipeDTO> response = rest.exchange(API_VERSION + "/recipes", POST,
            getRequest(idToken, expected), type);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        assertThat(response.getBody()).extracting(RecipeDTO::getTitle, RecipeDTO::getDescription, RecipeDTO::isVisible)
            .containsExactly(expected.getTitle(), expected.getDescription(), expected.isVisible());
    }

    @Test
    void updateRecipe_shouldReturn200() {
        final RecipeDTO expected = make(recipeDTO(2L, "Chicken Soup", "Best thing on a sick day.",
            "https://www.chicknood.jpg", true));

        ParameterizedTypeReference<RecipeDTO> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<RecipeDTO> response = rest.exchange(API_VERSION + "/recipes", PUT,
            getRequest(idToken, expected), type);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    void updateRecipeWithBadId_shouldReturn404() {
        final RecipeDTO expected = make(recipeDTO(999L, "Chicken Soup", "Best thing on a sick day.",
            "https://www.chicknood.jpg", true));

        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/recipes", PUT,
            getRequest(idToken, expected), String.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void updateRecipeEmptyId_shouldReturn404() {
        final RecipeDTO expected = make(recipeDTO(999L, "Chicken Soup", "Best thing on a sick day.",
            "https://www.chicknood.jpg", true));
        expected.setId(null);

        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/recipes", PUT,
            getRequest(idToken, expected), String.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void deleteRecipe_shouldReturn204() {
        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/recipes/1", DELETE,
            getRequest(idToken), String.class);
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    void deleteRecipeIdNotExisted_shouldReturn404() {
        final ResponseEntity<String> response = rest.exchange(API_VERSION + "/recipes/91", DELETE,
            getRequest(idToken), String.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void assignRecipeToUser_shouldReturn201() throws Exception {
        ParameterizedTypeReference<RecipeDTO> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<RecipeDTO> response = rest.exchange(API_VERSION + "/recipes/1", POST,
            getRequest(idToken), type);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        assertThat(response.getBody()).extracting(RecipeDTO::getId).isEqualTo(1L);
    }

    @Test
    void addIngredientToShoppingList_shouldReturn201() throws Exception {
        final RecipeDTO expected = make(recipeDTOWithTitleAndVisibility("apple pie", true));
//        expected.setIngredients();

        ParameterizedTypeReference<RecipeDTO> type = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<RecipeDTO> response = rest.exchange(API_VERSION + "/recipes:move", POST,
            getRequest(idToken, expected), type);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }
}
