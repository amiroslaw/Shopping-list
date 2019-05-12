package ovh.miroslaw.shoppinglist.rest;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.rest.util.ResponseUtil;
import ovh.miroslaw.shoppinglist.service.RecipeService;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.validation.Valid;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * The type Recipe resource.
 */
@RestController
@RequestMapping(API_VERSION)
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private final RecipeService recipeService;

    /**
     * Instantiates a new Recipe resource.
     *
     * @param recipeService the recipe service
     */
    public RecipeResource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * POST  /recipes : Create a new recipe.
     *
     * @param recipeDTO the recipeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeDTO, or with status 400 (Bad
     * Request) if the recipe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipes")
    public ResponseEntity<RecipeDTO> createRecipe(@Valid @RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeDTO);
        if (recipeDTO.getId() != null) {
            recipeDTO.setId(null);
        }
        RecipeDTO result = recipeService.save(recipeDTO);
        return ResponseEntity.created(new URI("/api/recipes/" + result.getId()))
            .body(result);
    }

    /**
     * PUT  /recipes : Updates an existing recipe.
     *
     * @param recipeDTO the recipeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeDTO, or with status 400 (Bad
     * Request) if the recipeDTO is not valid,
     */
    @PutMapping("/recipes")
    public ResponseEntity<RecipeDTO> updateRecipe(@Valid @RequestBody RecipeDTO recipeDTO) {
        log.debug("REST request to update Recipe : {}", recipeDTO);
        if (recipeDTO.getId() == null) {
            throw new NotFoundException("Bad request- id does not exist");
        }
        RecipeDTO result = recipeService.save(recipeDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /recipes : get user recipes.
     *
     * @param pageable the pageable
     * @param recommendation the recommendation flag
     * @param filter the filter for search
     * @return the ResponseEntity with status 200 (OK) and the list of recipes in body
     */
    @GetMapping("/recipes")
    public Page<RecipeDTO> getUserRecipes(Pageable pageable, @RequestParam boolean recommendation,
        @RequestParam(required = false) Optional<String> filter) {
        log.debug("REST request to get user Recipes");
        if (recommendation) {
            return recipeService.findRecommendations(pageable, false);
        } else {
            return filter.map(f -> recipeService.findUserRecipes(pageable, f))
                .orElse(recipeService.findUserRecipes(pageable, Strings.EMPTY));
        }
    }

    /**
     * GET  /recipes : get all public the recipes.
     *
     * @param pageable the pageable
     * @param recommendation the recommendation flag
     * @param filter the filter for search
     * @return the ResponseEntity with status 200 (OK) and the list of recipes in body
     */
    @GetMapping("/recipes/public")
    public Page<RecipeDTO> getRecipes(Pageable pageable, @RequestParam boolean recommendation,
        @RequestParam(required = false) Optional<String> filter) {
        if (recommendation) {
            return recipeService.findRecommendations(pageable, true);
        } else {
            return filter.map(f -> recipeService.findVisible(pageable, f))
                .orElse(recipeService.findVisible(pageable, Strings.EMPTY));
        }
    }

    /**
     * GET  /recipes/:id : get the "id" recipe.
     *
     * @param id the id of the recipeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        return ResponseUtil.wrapOrNotFound(recipeService.findOneWithEagerIngredients(id));
    }

    /**
     * DELETE  /recipes/:id : delete the "id" recipe.
     *
     * @param id the id of the recipeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assigns recipe to user response entity.
     *
     * @param id the id
     * @return the ResponseEntity with status 200 (OK) and with URL of the assigned recipe
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> assignRecipeToUser(@PathVariable Long id) throws URISyntaxException {
        RecipeDTO result = recipeService.assignRecipeToUser(id);
        return ResponseEntity.created(new URI("/api/recipes/" + result.getId())).body(result);
    }

    /**
     * Add ingredient to shopping list response entity.
     *
     * @param recipeDTO the recipe dto
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipes:move")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addIngredientToShoppingList(@RequestBody RecipeDTO recipeDTO)
        throws URISyntaxException {
        recipeService.addIngredientsToShoppingList(recipeDTO);
    }
}
