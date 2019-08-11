package ovh.miroslaw.shoppinglist.rest.ingredient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.service.PurchasedIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import java.util.List;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing Ingredient from purchased ingredient
 */
@RestController
@RequestMapping(API_VERSION + "/purchased-lists")
public class PurchasedIngredientResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedIngredientResource.class);
    private final PurchasedIngredientService purchasedIngredientService;

    public PurchasedIngredientResource(PurchasedIngredientService purchasedIngredientService) {
        this.purchasedIngredientService = purchasedIngredientService;
    }

    @PatchMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> addIngredientToShoppingList(@PathVariable Long ingredientId) {
        log.debug("REST request to addIngredientToShoppingList : {}", ingredientId);
        purchasedIngredientService.addIngredientToShoppingList(ingredientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{listId}/ingredients")
    public List<IngredientDTO> findUserPurchasedIngredients(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return purchasedIngredientService.findUserPurchasedIngredients(listId);
    }

    @DeleteMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        purchasedIngredientService.deleteIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }

}
