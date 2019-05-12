package ovh.miroslaw.shoppinglist.rest.ingredient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.service.ShoppingListService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing Ingredient from shoppinglist
 */
@RestController
@RequestMapping(API_VERSION + "/shopping-lists")
public class ShoppingListResource {
    private final Logger log = LoggerFactory.getLogger(ShoppingListResource.class);
    private final ShoppingListService shoppingListService;

    /**
     * Instantiates a new Shopping list resource.
     *
     * @param shoppingListService the shopping list service
     */
    public ShoppingListResource(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    /**
     * Creates ingredient in shopping list.
     *
     * @param ingredientDTO the ingredient dto
     * @return the response entity with the url of created ingredient
     * @throws URISyntaxException if the uri syntax is incorrect
     */
    @PostMapping("/{listId}/ingredients")
    public ResponseEntity<IngredientDTO> createIngredient(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = shoppingListService.addIngredient(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    /**
     * Method to get user ingredients from a specific shopping list
     * @param listId id of the shopping list
     * @return list of ingredients
     */
    @GetMapping("/{listId}/ingredients")
    public List<IngredientDTO> findUserShoppingList(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return shoppingListService.findShoppingListByUser(listId);
    }

    /**
     * Purchases ingredient. The ingredient will be removed from shopping list and add to the purchase list of the user.
     *
     * @param ingredientId the ingredient id
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{listId}/ingredients/{ingredientId}")
    public void purchaseIngredient(@PathVariable Long ingredientId) {
        shoppingListService.purchasedIngredient(ingredientId);
    }

    /**
     * Updates ingredient form shopping list.
     *
     * @param ingredient the ingredient
     * @param ingredientId the ingredient id
     * @return the response entity with status 200 and updated ingredient
     */
    @PutMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<IngredientDTO> editIngredient(@Valid @RequestBody IngredientDTO ingredient, @PathVariable Long ingredientId) {
        log.debug("REST request to update Ingredient : {}", ingredientId);
        if (ingredient.getId() == null) {
            ingredient.setId(ingredientId);
        }
        IngredientDTO result = shoppingListService.editIngredient(ingredient);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Deletes ingredient from shopping list.
     *
     * @param ingredientId the ingredient id
     * @return the response entity with status 204
     */
    @DeleteMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        shoppingListService.deleteIngredient(ingredientId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes ingredients from shopping list.
     *
     * @return the response entity with status 204
     */
    @DeleteMapping("/{listId}/ingredients")
    public ResponseEntity<Void> deleteAllIngredient() {
        log.debug("REST request to delete all Ingredient in shopping list");
        shoppingListService.deleteAllIngredients();
        return ResponseEntity.noContent().build();
    }
}
