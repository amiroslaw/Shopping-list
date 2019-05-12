package ovh.miroslaw.shoppinglist.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.ShoppingListService;
import ovh.miroslaw.shoppinglist.service.UserService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Shopping list service.
 */
@Service
@Transactional
public class ShoppingListServiceImpl implements ShoppingListService {

    private final UserService userService;
    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;
    private final IngredientService ingredientService;

    /**
     * Instantiates a new Shopping list service.
     *
     * @param ingredientRepository the ingredient repository
     * @param ingredientMapper the ingredient mapper
     * @param userService the user service
     * @param ingredientService the ingredient service
     */
    public ShoppingListServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper,
        UserService userService,
        IngredientService ingredientService) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userService = userService;
        this.ingredientService = ingredientService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findShoppingListByUser(Long listId) {
        User user = userService.getCurrentUser();

        return userService.findUserWithEagerShoppingList(user.getId())
            .map(User::getShoppingIngredients)
            .stream()
            .flatMap(Collection::stream)
            .map(ingredientMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public IngredientDTO addIngredient(IngredientDTO ingredientDTO) {
        User user = userService.getCurrentUser();
        Ingredient ingredient = ingredientService.findByNameOrCreateIngredient(ingredientDTO);
        user.addShoppingIngredient(ingredient);
        userService.save(user);
        return ingredientMapper.toDto(ingredient);
    }

    @Override
    public IngredientDTO editIngredient(IngredientDTO ingredientDTO) {
        deleteIngredient(ingredientDTO.getId());
        User user = userService.getCurrentUser();
        Ingredient ingredient = ingredientService.findByNameOrCreateIngredient(ingredientDTO);
        user.addShoppingIngredient(ingredient);
        userService.save(user);
        return ingredientMapper.toDto(ingredient);
    }

    @Override
    public void purchasedIngredient(Long ingredientId) {
        User user = userService.getCurrentUser();
        ingredientRepository.findById(ingredientId)
            .map(user::addPurchasedIngredient)
            .map(userService::save)
            .orElseThrow(NotFoundException::new);
        deleteIngredient(ingredientId);
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = userService.getCurrentUser();
        ingredientRepository.findById(id)
            .ifPresent(user::removeShoppingIngredient);
        userService.save(user);
    }

    @Override
    public void deleteAllIngredients() {
        User user = userService.getCurrentUser();
        user.setShoppingIngredients(new HashSet<>());
        userService.save(user);
    }

}
