package ovh.miroslaw.shoppinglist.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.service.PurchasedIngredientService;
import ovh.miroslaw.shoppinglist.service.UserService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Purchased ingredient service.
 */
@Service
@Transactional
public class PurchasedIngredientServiceImpl implements PurchasedIngredientService {

    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;
    private final UserService userService;

    /**
     * Instantiates a new Purchased ingredient service.
     *
     * @param ingredientMapper the ingredient mapper
     * @param ingredientRepository the ingredient repository
     * @param userService the user service
     */
    public PurchasedIngredientServiceImpl(IngredientMapper ingredientMapper,
        IngredientRepository ingredientRepository,
        UserService userService) {
        this.ingredientMapper = ingredientMapper;
        this.ingredientRepository = ingredientRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findUserPurchasedIngredients(Long listId) {
        User user = userService.getCurrentUser();
        return userService.findPurchasedIngredients(user.getId())
            .stream()
            .filter(Objects::nonNull)
            .map(ingredientMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public IngredientDTO addIngredientToUserList(Long ingredientId) {
        User user = userService.getCurrentUser();
        final IngredientDTO ingredientDTO = ingredientRepository.findById(ingredientId)
            .map(user::addUserIngredient)
            .map(ingredientMapper::toDto)
            .orElseThrow(NotFoundException::new);
        userService.save(user);
        deleteIngredient(ingredientId);
        return ingredientDTO;
    }

    @Override
    public IngredientDTO addIngredientToShoppingList(Long ingredientId) {
        User user = userService.getCurrentUser();
        final IngredientDTO ingredientDTO = ingredientRepository.findById(ingredientId)
            .map(user::addShoppingIngredient)
            .map(ingredientMapper::toDto)
            .orElseThrow(NotFoundException::new);
        userService.save(user);
        deleteIngredient(ingredientId);
        return ingredientDTO;
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = userService.getCurrentUser();
        Set<Ingredient> purchasedIngredients = user.getShoppingPurchasedIngredients();
        Ingredient deletedIngredient = this.getIngredientByIdFromSet(id, purchasedIngredients);
        purchasedIngredients.remove(deletedIngredient);
        userService.save(user);
    }

    Ingredient getIngredientByIdFromSet(Long id, Set<Ingredient> ingredientSet) {
        return ingredientSet
            .stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }
}
