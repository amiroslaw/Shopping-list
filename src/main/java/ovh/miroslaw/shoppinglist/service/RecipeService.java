package ovh.miroslaw.shoppinglist.service;

import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RecipeService {

    RecipeDTO save(RecipeDTO recipeDTO);

    @Transactional(readOnly = true)
    Map<IngredientDTO, Float> findRecipeIngredients(Long recipeId);

    List<RecipeDTO> findAll();

    Page<RecipeDTO> findAllWithEagerRelationships(Pageable pageable);
    
    Optional<RecipeDTO> findOneWithEagerIngredients(Long id);

    void delete(Long id);
}
