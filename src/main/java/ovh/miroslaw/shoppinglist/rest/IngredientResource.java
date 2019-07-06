package ovh.miroslaw.shoppinglist.rest;

import ovh.miroslaw.shoppinglist.rest.util.ResponseUtil;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

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

    @PostMapping("/user-ingredients")
    public ResponseEntity<IngredientDTO> createUserIngredient(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = ingredientService.addIngredientToUser(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @PostMapping("/shopping-lists/{listId}/ingredients")
    public ResponseEntity<IngredientWithAmountDTO> createIngredientToShoppingList(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = ingredientService.addIngredientToShoppingList(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @PostMapping("/purchased-lists/{listId}/ingredients")
    public ResponseEntity<IngredientDTO> createIngredientToPurchasedList(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = ingredientService.addIngredientToPurchasedList(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping("/shopping-lists/{listId}/ingredients")
    public List<IngredientWithAmountDTO> findUserShoppingList(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserShoppingList(listId);
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

//    @DeleteMapping("/ingredients/{id}")
//    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id ) {
//        log.debug("REST request to delete Ingredient : {}", id);
//        ingredientService.delete(id);
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * PUT  /ingredients : Updates an existing ingredient.
//     *
//     * @param ingredientDTO the ingredientDTO to update
//     * @return the ResponseEntity with status 200 (OK) and with body the updated ingredientDTO,
//     * or with status 400 (Bad Request) if the ingredientDTO is not valid,
//     * or with status 500 (Internal Server Error) if the ingredientDTO couldn't be updated
//     */
//    @PutMapping("/users/{userId}/ingredients")
//    public ResponseEntity<IngredientDTO> updateIngredient(@Valid @RequestBody IngredientDTO ingredientDTO, @PathVariable String userId) {
//        log.debug("REST request to update Ingredient : {}", ingredientDTO);
//        if (ingredientDTO.getId() == null) {
//            throw new BadRequestException("Bad request- id does not exist");
//        }
//        IngredientDTO result = ingredientService.save(ingredientDTO);
//        return ResponseEntity.ok().body(result);
//        return null;
//    }
}
