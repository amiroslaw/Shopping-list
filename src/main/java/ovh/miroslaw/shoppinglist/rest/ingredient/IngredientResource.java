package ovh.miroslaw.shoppinglist.rest.ingredient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.rest.util.ResponseUtil;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

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
