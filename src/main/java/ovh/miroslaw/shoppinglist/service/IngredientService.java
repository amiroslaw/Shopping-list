package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IngredientService {

    Map<IngredientDTO, Float> findUserShoppingList(Long listId);

    @Transactional(readOnly = true)
    List<IngredientWithAmountDTO> findUserIngredients2();

    @Transactional(readOnly = true)
    Map<IngredientDTO, Float> findUserIngredients();

    @Transactional(readOnly = true)
    List<IngredientDTO> findUserPurchasedIngredients(Long listId);

    List<IngredientDTO> findAll();

    Optional<IngredientDTO> findOne(Long id);

    void delete(Long id);

    IngredientWithAmountDTO addIngredientToUser(IngredientWithAmountDTO ingredientDTO);

    IngredientWithAmountDTO addIngredientToShoppingList(IngredientWithAmountDTO ingredientDTO);

    IngredientDTO addIngredientToPurchasedList(IngredientDTO ingredientDTO);
    IngredientDTO addIngredientToRecipe(Long recipeId, IngredientDTO ingredientDTO);

//    List<IngredientDTO> findAllUserIngredients(Long userId);
}
