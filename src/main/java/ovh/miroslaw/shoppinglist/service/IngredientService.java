package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IngredientService {

    Map<IngredientDTO, Float> findUserShoppingList(Long userId);

    @Transactional(readOnly = true)
    Map<IngredientDTO, Float> findUserIngredients(Long userId);

    @Transactional(readOnly = true)
    List<IngredientDTO> findUserPurchasedIngredients(Long userId);

    List<IngredientDTO> findAll();

    Optional<IngredientDTO> findOne(Long id);

    void delete(Long id);

    IngredientDTO addIngredientToUser(Long userId, IngredientDTO ingredientDTO);
    IngredientDTO addIngredientToShoppingList(Long userId, IngredientDTO ingredientDTO);
    IngredientDTO addIngredientToPurchasedList(Long userId, IngredientDTO ingredientDTO);
    IngredientDTO addIngredientToRecipe(Long recipeId, IngredientDTO ingredientDTO);

//    List<IngredientDTO> findAllUserIngredients(Long userId);
}
