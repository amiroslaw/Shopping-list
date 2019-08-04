package ovh.miroslaw.shoppinglist.rest.ingredient;

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

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing all Ingredient.
 */
@RestController
@RequestMapping(API_VERSION + "/user-ingredients")
public class UserIngredientResource {
    private final Logger log = LoggerFactory.getLogger(UserIngredientResource.class);
    private final IngredientService ingredientService;

    public UserIngredientResource(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<IngredientDTO> createUserIngredient(@RequestBody IngredientWithAmountDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientWithAmountDTO result = ingredientService.addIngredientToUser(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping
    public List<IngredientWithAmountDTO> findUserIngredients() {
        log.debug("REST request to get all Ingredients");
        return ingredientService.findUserIngredients();
    }

}
