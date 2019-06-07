package ovh.miroslaw.shoppinglist.rest;

import ovh.miroslaw.shoppinglist.rest.util.ResponseUtil;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Ingredient.
 */
@RestController
public class IngredientResource {
    private final Logger log = LoggerFactory.getLogger(IngredientResource.class);
    private final IngredientService ingredientService;

    public IngredientResource(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * POST  /ingredients : Create a new ingredient into user ingredient list
     *
     * @param ingredientDTO the ingredientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ingredientDTO, or with status 400 (Bad Request) if the ingredient has already an ID
     */
    @PostMapping("/api/user-ingredients")
    public ResponseEntity<IngredientDTO> createUserIngredient(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = ingredientService.addIngredientToUser(ingredientDTO);
        return ResponseEntity.created(new URI("/api/ingredients/" + result.getId())).body(result);
    }

    /**
     * POST  /ingredients : Create a new ingredient into shopping list
     *
     * @param ingredientDTO the ingredientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ingredientDTO, or with status 400 (Bad Request) if the ingredient has already an ID
     */
    @PostMapping("/api/shopping-lists/{listId}/ingredients")
    public ResponseEntity<IngredientDTO> createIngredientToShoppingList(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = ingredientService.addIngredientToShoppingList(ingredientDTO);
        return ResponseEntity.created(new URI("/api/ingredients/" + result.getId())).body(result);
    }
    /**
     * POST  /ingredients : Create a new ingredient into purchased list
     *
     * @param ingredientDTO the ingredientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ingredientDTO, or with status 400 (Bad Request) if the ingredient has already an ID
     */
    @PostMapping("/api/purchased-lists/{listId}/ingredients")
    public ResponseEntity<IngredientDTO> createIngredientToPurchasedList(@RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = ingredientService.addIngredientToPurchasedList(ingredientDTO);
        return ResponseEntity.created(new URI("/api/ingredients/" + result.getId())).body(result);
    }
    /**
     * GET  /ingredients : get all ingredients in shopping list.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ingredients in body
     */
    @GetMapping("/api/shopping-lists/{listId}/ingredients")
    public Map<IngredientDTO, Float> findUserShoppingList(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserShoppingList(listId);
    }

    /**
     * GET  /ingredients : get all ingredients in purchased list.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ingredients in body
     */
    @GetMapping("/api/purchased-lists/{listId}/ingredients")
    public List<IngredientDTO> findUserPurchasedIngredients(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserPurchasedIngredients(listId);
    }

    /**
     * GET  /ingredients : get all user ingredients
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ingredients in body
     */
    @GetMapping("/api/user-ingredients")
    public Map<IngredientDTO, Float> findUserIngredients() {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserIngredients();
    }
    /**
     * GET  /ingredients/:id : get the "id" ingredient.
     *
     * @param id the id of the ingredientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ingredientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/api/ingredients/{id}")
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable Long id) {
        log.debug("REST request to get Ingredient : {}", id);
        Optional<IngredientDTO> ingredientDTO = ingredientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingredientDTO);
    }
    /**
     * GET  /ingredients : get all user the ingredients.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ingredients in body
     */
    @GetMapping("/api/ingredients")
    public List<IngredientDTO> getAllIngredients() {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findAll();
    }

//    /**
//     * DELETE  /ingredients/:id : delete the "id" ingredient.
//     *
//     * @param id the id of the ingredientDTO to delete
//     * @return the ResponseEntity with status 200 (OK)
//     */
//    @DeleteMapping("/api/ingredients/{id}")
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
//    @PutMapping("/api/users/{userId}/ingredients")
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
