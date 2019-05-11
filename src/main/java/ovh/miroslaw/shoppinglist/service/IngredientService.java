package ovh.miroslaw.shoppinglist.service;

import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;
import java.util.Optional;

public interface IngredientService {

    IngredientDTO save(IngredientDTO ingredientDTO);

    List<IngredientDTO> findAll();

    Optional<IngredientDTO> findOne(Long id);

    void delete(Long id);
}
