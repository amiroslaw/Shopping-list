package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;

public interface UserIngredientService {

    @Transactional(readOnly = true)
    List<IngredientDTO> findUserIngredients();

    IngredientDTO addIngredient(IngredientDTO ingredientDTO);

    IngredientDTO editIngredient(IngredientDTO ingredientDTO);

    void addIngredientToShoppingList(Long ingredientId);

    void deleteIngredient(Long id);

}
