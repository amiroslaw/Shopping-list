package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import java.util.List;
import java.util.Optional;

public interface PurchasedIngredientService {

    @Transactional(readOnly = true)
    List<IngredientDTO> findUserPurchasedIngredients(Long listId);

    IngredientDTO addIngredientToPurchasedList(IngredientDTO ingredientDTO);

}
