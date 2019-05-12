package ovh.miroslaw.shoppinglist.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.IngredientName;
import ovh.miroslaw.shoppinglist.domain.Recipe;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.RecipeRepository;
import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.security.SecurityUtils;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.RecipeService;
import ovh.miroslaw.shoppinglist.service.UserService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;
import ovh.miroslaw.shoppinglist.service.mapper.RecipeMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Recipe service.
 */
@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final UserService userService;
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientMapper ingredientMapper;
    private final IngredientService ingredientService;

    /**
     * Instantiates a new Recipe service.
     *
     * @param userService the user service
     * @param recipeRepository the recipe repository
     * @param recipeMapper the recipe mapper
     * @param ingredientMapper the ingredient mapper
     * @param ingredientService the ingredient service
     */
    public RecipeServiceImpl(UserService userService, RecipeRepository recipeRepository,
        RecipeMapper recipeMapper,
        IngredientMapper ingredientMapper,
        IngredientService ingredientService) {
        this.userService = userService;
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientMapper = ingredientMapper;
        this.ingredientService = ingredientService;
    }

    private Function<Recipe, Recipe> createNewRecipeInstance(Recipe recipe, User currentUser) {
        return r -> {
            currentUser.removeRecipe(r);
            recipe.setId(null);
            return recipe;
        };
    }

    @Override
    public RecipeDTO save(RecipeDTO recipeDTO) {
        log.debug("Request to save Recipe : {}", recipeDTO);
        Recipe mappedRecipe = recipeMapper.toEntity(recipeDTO);

        final Set<Ingredient> ingredients = recipeDTO.getIngredients().stream()
            .map(ingredientService::findByNameOrCreateIngredient)
            .collect(Collectors.toSet());
        mappedRecipe.setIngredients(ingredients);

        final User currentUser = userService.getCurrentUser();
        if (mappedRecipe.getId() == null) {
            mappedRecipe.addUser(currentUser);
        } else {
            mappedRecipe = recipeRepository.findById(mappedRecipe.getId())
                .filter(r -> r.getUsers().size() > 1)
                .map(createNewRecipeInstance(mappedRecipe, currentUser))
                .orElse(mappedRecipe.addUser(currentUser));
        }

        recipeRepository.save(mappedRecipe);
        return recipeMapper.toDto(mappedRecipe);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDTO> findVisible(Pageable pageable, String filter) {
        log.debug("Request to get all Recipes");
        return recipeRepository.findAllByVisibleAndFilter(true, filter.toLowerCase(), pageable)
            .map(recipeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDTO> findUserRecipes(Pageable pageable, String filter) {
        return SecurityUtils.getCurrentUserLogin()
            .map(u -> recipeRepository.findAllRecipesByUserLoginAndFilter(u, filter, pageable))
            .orElse(Page.empty())
            .map(recipeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDTO> findRecommendations(Pageable pageable, boolean isPublicRepository) {
        final Set<String> userIngredients = userService.getCurrentUser()
            .getUserIngredients()
            .stream()
            .map(Ingredient::getIngredientName)
            .map(IngredientName::getName)
            .collect(Collectors.toSet());

        final Page<RecipeDTO> recipesPage;
        if (isPublicRepository) {
            recipesPage = findVisible(pageable, Strings.EMPTY);
        } else {
            recipesPage = findUserRecipes(pageable, Strings.EMPTY);
        }
        final List<RecipeDTO> recipes = recipesPage.getContent();

        Map<String, List<RecipeDTO>> recommendationMap = new HashMap<>();
        userIngredients.forEach(i -> recommendationMap.put(i, Collections.emptyList()));
        for (RecipeDTO recipe : recipes) {
            recipe.getIngredients().stream()
                .map(IngredientDTO::getName)
                .forEach(i -> recommendationMap.computeIfPresent(i, (k, list) -> addRecipeToMap(recipe, list)));
        }

        final Map<RecipeDTO, Long> groupedRecipes = recommendationMap.values().parallelStream()
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Set<RecipeDTO> sortedRecipes = new LinkedHashSet<>();
        groupedRecipes.entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .forEach(entry -> {
                for (int i = 1; i <= entry.getValue(); i++) {
                    sortedRecipes.add(entry.getKey());
                }
            });

        final ArrayList<RecipeDTO> recipeDTOS = new ArrayList<>(sortedRecipes);
        return new PageImpl<>(recipeDTOS, recipesPage.getPageable(), recipesPage.getTotalElements());
    }

    private ArrayList<RecipeDTO> addRecipeToMap(RecipeDTO recipe, List<RecipeDTO> list) {
        ArrayList<RecipeDTO> newRecipeList = new ArrayList<>(list);
        newRecipeList.add(recipe);
        return newRecipeList;
    }

    @Override
    public RecipeDTO assignRecipeToUser(Long recipeId) {
        final User currentUser = userService.getCurrentUser();
        return recipeRepository.findById(recipeId)
            .map(recipe -> recipe.addUser(currentUser))
            .map(recipeRepository::save)
            .map(recipeMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Recipe not found: recipe id " + recipeId));
    }

    @Override
    public void addIngredientsToShoppingList(RecipeDTO recipeDTO) {
        final User currentUser = userService.getCurrentUser();
        recipeDTO.getIngredients().stream()
            .map(ingredientMapper::toEntity)
            .forEach(currentUser::addShoppingIngredient);
    }

    @Transactional(readOnly = true)
    public Page<RecipeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recipeRepository.findAllByVisibleAndFilter(pageable).map(recipeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeDTO> findOneWithEagerIngredients(Long id) {
        log.debug("Request to get Recipe : {}", id);
        return recipeRepository.findOneWithEagerIngredients(id)
            .map(recipeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Recipe : {}", id);
        final User user = userService.getCurrentUser();
        Recipe recipe = recipeRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        user.removeRecipe(recipe);

        deleteLastRecipe(recipe);
    }

    private void deleteLastRecipe(Recipe recipe) {
        if (recipe.getUsers().isEmpty()) {
            recipeRepository.delete(recipe);
        }
    }
}
