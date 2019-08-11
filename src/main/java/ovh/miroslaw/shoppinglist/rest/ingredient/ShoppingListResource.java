package ovh.miroslaw.shoppinglist.rest.ingredient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;
import ovh.miroslaw.shoppinglist.service.ShoppingListService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing Ingredient from shoppinglist
 */
@RestController
@RequestMapping(API_VERSION + "/shopping-list")
public class ShoppingListResource {
    private final Logger log = LoggerFactory.getLogger(ShoppingListResource.class);
    private final ShoppingListService shoppingListService;

    public ShoppingListResource(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @PostMapping("/{listId}/ingredients")
    public ResponseEntity<IngredientWithAmountDTO> createIngredient(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = shoppingListService.addIngredient(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping("/{listId}/ingredients")
    public List<IngredientWithAmountDTO> findUserShoppingList(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return shoppingListService.findShoppingListByUser(listId);
    }

    @PatchMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> purchaseIngredient(@PathVariable Long ingredientId) {
        shoppingListService.purchasedIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<IngredientWithAmountDTO> updateIngredient(@Valid @RequestBody IngredientWithAmountDTO ingredient, @PathVariable Long ingredientId) {
        log.debug("REST request to update Ingredient : {}", ingredientId);
        IngredientWithAmountDTO result = shoppingListService.editIngredient(ingredient, ingredientId);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        shoppingListService.deleteIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{listId}/ingredients")
    public ResponseEntity<Void> deleteAllIngredient() {
        log.debug("REST request to delete Ingredient : {}");
        shoppingListService.deleteAllIngredients();
        return ResponseEntity.ok().build();
    }
}
