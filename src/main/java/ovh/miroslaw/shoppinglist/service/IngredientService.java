package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.Optional;
import java.util.Set;

/**
 * The interface Ingredient service.
 */
public interface IngredientService {

    /**
     * Find all names of ingredients.
     *
     * @return the set of names
     */
    Set<String> findAllNames();

    /**
     * Find one ingredient.
     *
     * @param id the id
     * @return the optional of IngredientDTO
     */
    Optional<IngredientDTO> findOne(Long id);

    /**
     * Delete ingredient.
     *
     * @param id the id of the ingredient
     */
    void delete(Long id);

    /**
     * Find by name or create ingredient .
     *
     * @param ingredientDTO the ingredient dto
     * @return the ingredient
     */
    @Transactional(readOnly = true)
    Ingredient findByNameOrCreateIngredient(IngredientDTO ingredientDTO);
}
