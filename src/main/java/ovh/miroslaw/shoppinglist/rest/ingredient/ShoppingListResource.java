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
import ovh.miroslaw.shoppinglist.service.IngredientService;
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
    private final IngredientService ingredientService;

    public ShoppingListResource(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping("/{listId}/ingredients")
    public ResponseEntity<IngredientWithAmountDTO> createIngredient(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = ingredientService.addIngredientToShoppingList(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping("/{listId}/ingredients")
    public List<IngredientWithAmountDTO> findUserShoppingList(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserShoppingList(listId);
    }

    @PatchMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> purchaseIngredient(@PathVariable Long ingredientId) {
        ingredientService.purchasedIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<IngredientWithAmountDTO> updateIngredient(@Valid @RequestBody IngredientWithAmountDTO ingredient, @PathVariable Long ingredientId) {
        log.debug("REST request to update Ingredient : {}", ingredientId);
        if (ingredient.getId() == null) {
            throw new BadRequestException("Bad request- id does not exist");
        }
        IngredientWithAmountDTO result = ingredientService.editIngredientToShoppingList(ingredient);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        ingredientService.deleteIngredientFromShoppingList(ingredientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{listId}/ingredients")
    public ResponseEntity<Void> deleteAllIngredient() {
        log.debug("REST request to delete Ingredient : {}");
        ingredientService.deleteAllIngredientFromShoppingList();
        return ResponseEntity.ok().build();
    }
}
