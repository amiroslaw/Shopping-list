package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;

import java.util.List;
import java.util.Optional;

public interface UserIngredientService {


    @Transactional(readOnly = true)
    List<IngredientWithAmountDTO> findUserIngredients();

    IngredientWithAmountDTO addIngredientToUser(IngredientWithAmountDTO ingredientDTO);

//    List<IngredientDTO> findAllUserIngredients(Long userId);
}
