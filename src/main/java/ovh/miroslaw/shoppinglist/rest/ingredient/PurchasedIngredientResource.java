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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.service.PurchasedIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing Ingredient from purchased ingredient. Checked ingredients from shopping list will have status "purchased".
 */
@RestController
@RequestMapping(API_VERSION + "/purchased-lists")
public class PurchasedIngredientResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedIngredientResource.class);
    private final PurchasedIngredientService purchasedIngredientService;

    /**
     * Instantiates a new Purchased ingredient resource.
     *
     * @param purchasedIngredientService the purchased ingredient service
     */
    public PurchasedIngredientResource(PurchasedIngredientService purchasedIngredientService) {
        this.purchasedIngredientService = purchasedIngredientService;
    }

    /**
     * Find user purchased ingredients list.
     *
     * @param listId the list id
     * @return the list
     */
    @GetMapping("/{listId}/ingredients")
    public List<IngredientDTO> findUserPurchasedIngredients(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return purchasedIngredientService.findUserPurchasedIngredients(listId);
    }

    /**
     * Add ingredient to shopping list.
     *
     * @param ingredientId the ingredient id
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{listId}/ingredients/{ingredientId}")
    public void addIngredientToShoppingList(@PathVariable Long ingredientId) {
        log.debug("REST request to addIngredientToShoppingList : {}", ingredientId);
        purchasedIngredientService.addIngredientToShoppingList(ingredientId);
    }

    /**
     * Add ingredient to user list.
     *
     * @param ingredientId the id of the ingredient to delete
     */
    @PutMapping("/{listId}/ingredients/{ingredientId}")
    public void addIngredientToUserList(@PathVariable Long ingredientId) {
        log.debug("REST request to addIngredientToShoppingList : {}", ingredientId);
        purchasedIngredientService.addIngredientToUserList(ingredientId);
    }

    /**
     * Delete ingredient response entity.
     *
     * @param ingredientId the ingredient id
     * @return the response entity with status 204
     */
    @DeleteMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        purchasedIngredientService.deleteIngredient(ingredientId);
        return ResponseEntity.noContent().build();
    }
}
