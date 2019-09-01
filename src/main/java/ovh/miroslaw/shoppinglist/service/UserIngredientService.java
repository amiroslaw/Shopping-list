package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import java.util.List;

public interface UserIngredientService {

    @Transactional(readOnly = true)
    List<IngredientWithAmountDTO> findUserIngredients();

    IngredientWithAmountDTO addIngredient(IngredientWithAmountDTO ingredientDTO);

    IngredientWithAmountDTO editIngredient(IngredientWithAmountDTO ingredient, Long ingredientId);

    void addIngredientToShoppingList(Long ingredientId);

    void deleteIngredient(Long id);

}
