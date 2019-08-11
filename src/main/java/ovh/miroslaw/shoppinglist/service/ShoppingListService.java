package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import java.util.List;

public interface ShoppingListService {

    @Transactional(readOnly = true)
    List<IngredientWithAmountDTO> findShoppingListByUser(Long listId);

    IngredientWithAmountDTO addIngredient(IngredientWithAmountDTO ingredientDTO);

    IngredientWithAmountDTO editIngredient(IngredientWithAmountDTO ingredient, Long ingredientId);

    void deleteIngredient(Long id);

    void deleteAllIngredients();

    void purchasedIngredient(Long ingredientId);
}
