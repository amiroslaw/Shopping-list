package ovh.miroslaw.shoppinglist.rest.ingredient;

import org.springframework.http.HttpStatus;
import ovh.miroslaw.shoppinglist.service.UserIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

import javax.validation.Valid;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing user ingredients.
 */
@RestController
@RequestMapping(API_VERSION + "/user-ingredients")
public class UserIngredientResource {
    private final Logger log = LoggerFactory.getLogger(UserIngredientResource.class);
    private final UserIngredientService userIngredientService;

    /**
     * Instantiates a new User ingredient resource.
     *
     * @param userIngredientService the user ingredient service
     */
    public UserIngredientResource(UserIngredientService userIngredientService) {
        this.userIngredientService = userIngredientService;
    }

    /**
     * Create user ingredient response entity.
     *
     * @param ingredientDTO the ingredient dto
     * @return the response entity with status 201 (Created) and URI of the created resource
     * @throws URISyntaxException if the uri syntax is incorrect
     */
    @PostMapping
    public ResponseEntity<IngredientDTO> createUserIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = userIngredientService.addIngredient(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    /**
     * Find user ingredients.
     *
     * @return the list of user ingredients
     */
    @GetMapping
    public List<IngredientDTO> findUserIngredients() {
        log.debug("REST request to get all Ingredients");
        return userIngredientService.findUserIngredients();
    }

    /**
     * Update ingredient response entity.
     *
     * @param ingredient the ingredient
     * @param ingredientId the ingredient id, if null it will take from ingredient dto
     * @return the response entity with status 200 (OK) and with body the updated ingredient
     */
    @PutMapping("/{ingredientId}")
    public ResponseEntity<IngredientDTO> updateIngredient(@Valid @RequestBody IngredientDTO ingredient, @PathVariable Long ingredientId) {
        log.debug("REST request to update Ingredient : {}", ingredientId);
        if (ingredient.getId() == null) {
            ingredient.setId(ingredientId);
        }
        IngredientDTO result = userIngredientService.editIngredient(ingredient);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Add ingredient to shopping list.
     *
     * @param ingredientId the ingredient id
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{ingredientId}")
    public void addIngredientToShoppingList(@PathVariable Long ingredientId) {
        userIngredientService.addIngredientToShoppingList(ingredientId);
    }

    /**
     * Delete ingredient response entity.
     *
     * @param ingredientId the ingredient id
     * @return the response entity with status 204
     */
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        userIngredientService.deleteIngredient(ingredientId);
        return ResponseEntity.noContent().build();
    }
}
