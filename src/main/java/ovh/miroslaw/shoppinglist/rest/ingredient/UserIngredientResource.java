package ovh.miroslaw.shoppinglist.rest.ingredient;

import ovh.miroslaw.shoppinglist.service.UserIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

import javax.validation.Valid;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing all Ingredient.
 */
@RestController
@RequestMapping(API_VERSION + "/user-ingredients")
public class UserIngredientResource {
    private final Logger log = LoggerFactory.getLogger(UserIngredientResource.class);
    private final UserIngredientService userIngredientService;

    public UserIngredientResource(UserIngredientService userIngredientService) {
        this.userIngredientService = userIngredientService;
    }

    @PostMapping
    public ResponseEntity<IngredientDTO> createUserIngredient(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = userIngredientService.addIngredient(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping
    public List<IngredientWithAmountDTO> findUserIngredients() {
        log.debug("REST request to get all Ingredients");
        return userIngredientService.findUserIngredients();
    }

    @PutMapping("/{ingredientId}")
    public ResponseEntity<IngredientWithAmountDTO> updateIngredient(@Valid @RequestBody IngredientWithAmountDTO ingredient, @PathVariable Long ingredientId) {
        log.debug("REST request to update Ingredient : {}", ingredientId);
        IngredientWithAmountDTO result = userIngredientService.editIngredient(ingredient, ingredientId);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/{ingredientId}")
    public ResponseEntity<Void> purchaseIngredient(@PathVariable Long ingredientId) {
        userIngredientService.addIngredientToShoppingList(ingredientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        userIngredientService.deleteIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }
}
