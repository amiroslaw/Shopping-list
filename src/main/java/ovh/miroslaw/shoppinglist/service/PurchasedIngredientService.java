package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;

public interface PurchasedIngredientService {

    void addIngredientToShoppingList(Long ingredientId);

    void deleteIngredient(Long id);

    @Transactional(readOnly = true)
    List<IngredientDTO> findUserPurchasedIngredients(Long listId);

}
