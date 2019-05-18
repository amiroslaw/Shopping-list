package ovh.miroslaw.shoppinglist.rest;

import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.rest.util.ResponseUtil;
import ovh.miroslaw.shoppinglist.service.RecipeService;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

@RestController
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "recipe";

    private final RecipeService recipeService;

    public RecipeResource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * POST  /recipes : Create a new recipe.
     *
     * @param recipeDTO the recipeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeDTO, or with status 400 (Bad Request) if the recipe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipes")
    public ResponseEntity<RecipeDTO> createRecipe(@Valid @RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeDTO);
        if (recipeDTO.getId() != null) {
            throw new NotFoundException("Invalid id");
        }
        RecipeDTO result = recipeService.save(recipeDTO);
        return ResponseEntity.created(new URI("/api/recipes/" + result.getId()))
            .body(result);
    }

    /**
     * PUT  /recipes : Updates an existing recipe.
     *
     * @param recipeDTO the recipeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeDTO,
     * or with status 400 (Bad Request) if the recipeDTO is not valid,
     * or with status 500 (Internal Server Error) if the recipeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipes")
    public ResponseEntity<RecipeDTO> updateRecipe(@Valid @RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}", recipeDTO);
        if (recipeDTO.getId() == null) {
            throw new NotFoundException("Invalid id");
        }
        RecipeDTO result = recipeService.save(recipeDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /recipes : get all the recipes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of recipes in body
     */
    @GetMapping("/recipes")
    public List<RecipeDTO> getAllRecipes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Recipes");
        return recipeService.findAll();
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
        return ResponseUtil.wrapOrNotFound(recipeService.findOne(id));
//            return recipeService.findOne(id)
//            .map( user -> ResponseEntity.ok().body(user) )          //200 OK
//            .orElseGet( () -> ResponseEntity.notFound().build() );  //404 Not found

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
        return ResponseEntity.ok().build();
    }
}
