package ovh.miroslaw.shoppinglist.service;

import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;

/**
 * The interface Purchased ingredient service.
 */
public interface PurchasedIngredientService {

    /**
     * Add ingredient to shopping list.
     *
     * @param ingredientId the ingredient id
     * @return the ingredient dto
     */
    IngredientDTO addIngredientToShoppingList(Long ingredientId);

    /**
     * Delete ingredient.
     *
     * @param id the id of the ingredient
     */
    void deleteIngredient(Long id);

    /**
     * Find user purchased ingredients list.
     *
     * @param listId the list id
     * @return the list of IngredientDTO
     */
    List<IngredientDTO> findUserPurchasedIngredients(Long listId);

    /**
     * Add ingredient to user list ingredient dto.
     *
     * @param ingredientId the ingredient id
     * @return the IngredientDTO
     */
    IngredientDTO addIngredientToUserList(Long ingredientId);
}
