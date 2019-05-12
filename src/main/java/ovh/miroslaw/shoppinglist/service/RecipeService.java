package ovh.miroslaw.shoppinglist.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import java.util.Optional;

/**
 * The interface Recipe service.
 */
public interface RecipeService {

    /**
     * Save recipe.
     *
     * @param recipeDTO the recipe dto
     * @return the recipe dto
     */
    RecipeDTO save(RecipeDTO recipeDTO);

    /**
     * Find one recipe with eager ingredients.
     *
     * @param id the id of the recipe
     * @return the optional of the RecipeDTO
     */
    Optional<RecipeDTO> findOneWithEagerIngredients(Long id);

    /**
     * Delete recipe.
     *
     * @param id the id of the recipe
     */
    void delete(Long id);

    /**
     * Assign recipe to user.
     *
     * @param recipeId the recipe id
     * @return the recipe dto
     */
    RecipeDTO assignRecipeToUser(Long recipeId);

    /**
     * Add ingredients to shopping list.
     *
     * @param recipeDTO the recipe dto
     */
    void addIngredientsToShoppingList(RecipeDTO recipeDTO);

    /**
     * Finds public recipes page.
     *
     * @param pageable the pageable
     * @param filter the search query
     * @return the page of the RecipeDTO
     */
    Page<RecipeDTO> findVisible(Pageable pageable, String filter);

    /**
     * Find user recipes.
     *
     * @param pageable the pageable
     * @param filter the search query
     * @return the page of the RecipeDTO
     */
    Page<RecipeDTO> findUserRecipes(Pageable pageable, String filter);

    /**
     * Find recommended recipes.
     *
     * @param pageable the pageable
     * @param isPublicRepository the flag of public repository
     * @return the page of the RecipeDTO
     */
    Page<RecipeDTO> findRecommendations(Pageable pageable, boolean isPublicRepository);
}
