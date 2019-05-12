package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;

/**
 * The interface Shopping list service.
 */
public interface ShoppingListService {

    /**
     * Find shopping list by user list.
     *
     * @param listId the list id
     * @return the list of {@link IngredientDTO}
     */
    @Transactional(readOnly = true)
    List<IngredientDTO> findShoppingListByUser(Long listId);

    /**
     * Add ingredient to the shopping list.
     *
     * @param ingredientDTO the ingredient dto
     * @return the ingredient dto
     */
    IngredientDTO addIngredient(IngredientDTO ingredientDTO);

    /**
     * Updates ingredient.
     *
     * @param ingredientDTO the ingredient dto
     * @return the ingredient dto
     */
    IngredientDTO editIngredient(IngredientDTO ingredientDTO);

    /**
     * Delete ingredient.
     *
     * @param id the id of the ingredient
     */
    void deleteIngredient(Long id);

    /**
     * Delete all ingredients.
     */
    void deleteAllIngredients();

    /**
     * Purchased ingredient.
     *
     * @param ingredientId the ingredient id
     */
    void purchasedIngredient(Long ingredientId);
}
