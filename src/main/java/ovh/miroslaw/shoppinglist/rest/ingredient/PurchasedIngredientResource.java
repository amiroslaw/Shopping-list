package ovh.miroslaw.shoppinglist.rest.ingredient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.service.PurchasedIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing Ingredient from purchased ingredient
 */
@RestController
@RequestMapping(API_VERSION + "/purchased-lists")
public class PurchasedIngredientResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedIngredientResource.class);
    private final PurchasedIngredientService purchasedIngredientService;

    public PurchasedIngredientResource(PurchasedIngredientService purchasedIngredientService) {
        this.purchasedIngredientService = purchasedIngredientService;
    }

    @PostMapping("/purchased-lists/{listId}/ingredients")
    public ResponseEntity<IngredientDTO> createIngredientToPurchasedList(@RequestBody IngredientDTO ingredientDTO)
        throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        IngredientDTO result = purchasedIngredientService.addIngredientToPurchasedList(ingredientDTO);
        return ResponseEntity.created(new URI(API_VERSION + "/ingredients/" + result.getId())).body(result);
    }

    @GetMapping("/purchased-lists/{listId}/ingredients")
    public List<IngredientDTO> findUserPurchasedIngredients(@PathVariable Long listId) {
        log.debug("REST request to get all Ingredients");
        return purchasedIngredientService.findUserPurchasedIngredients(listId);
    }

}
