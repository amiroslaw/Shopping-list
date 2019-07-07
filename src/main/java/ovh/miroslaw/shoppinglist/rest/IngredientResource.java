package ovh.miroslaw.shoppinglist.rest;

import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;
import ovh.miroslaw.shoppinglist.rest.util.ResponseUtil;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing Ingredient.
 */
@RestController
@RequestMapping(API_VERSION)
public class IngredientResource {
    private final Logger log = LoggerFactory.getLogger(IngredientResource.class);
    private final IngredientService ingredientService;

    public IngredientResource(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping("/shopping-lists/{listId}/ingredients")
    public ResponseEntity<IngredientWithAmountDTO> createIngredientToShoppingList(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = ingredientService.addIngredientToShoppingList(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping("/shopping-lists/{listId}/ingredients")
    public List<IngredientWithAmountDTO> findUserShoppingList(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserShoppingList(listId);
    }

    @PatchMapping("/shopping-lists/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> purchaseIngredient(@PathVariable Long ingredientId) {
        ingredientService.purchasedIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/shopping-lists/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<IngredientWithAmountDTO> updateIngredient(@Valid @RequestBody IngredientWithAmountDTO ingredient, @PathVariable Long ingredientId) {
        log.debug("REST request to update Ingredient : {}", ingredientId);
        if (ingredient.getId() == null) {
            throw new BadRequestException("Bad request- id does not exist");
        }
        IngredientWithAmountDTO result = ingredientService.editIngredientToShoppingList(ingredient);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/shopping-lists/{listId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredientFromShoppingList(@PathVariable Long ingredientId) {
        log.debug("REST request to delete Ingredient : {}", ingredientId);
        ingredientService.deleteIngredientFromShoppingList(ingredientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/shopping-lists/{listId}/ingredients")
    public ResponseEntity<Void> deleteAllIngredientFromShoppingList() {
        log.debug("REST request to delete Ingredient : {}");
        ingredientService.deleteAllIngredientFromShoppingList();
        return ResponseEntity.ok().build();
    }
    @PostMapping("/user-ingredients")
    public ResponseEntity<IngredientDTO> createUserIngredient(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = ingredientService.addIngredientToUser(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @PostMapping("/purchased-lists/{listId}/ingredients")
    public ResponseEntity<IngredientDTO> createIngredientToPurchasedList(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = ingredientService.addIngredientToPurchasedList(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping("/purchased-lists/{listId}/ingredients")
    public List<IngredientDTO> findUserPurchasedIngredients(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserPurchasedIngredients(listId);
    }

    @GetMapping("/user-ingredients")
    public List<IngredientWithAmountDTO> findUserIngredients() {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserIngredients();
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable Long id) {
        log.debug("REST request to get Ingredient : {}", id);
        Optional<IngredientDTO> ingredientDTO = ingredientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingredientDTO);
    }

    @GetMapping("/ingredients")
    public List<IngredientDTO> getAllIngredients() {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findAll();
    }

}
