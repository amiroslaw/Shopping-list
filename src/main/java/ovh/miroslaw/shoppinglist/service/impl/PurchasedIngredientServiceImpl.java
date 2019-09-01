package ovh.miroslaw.shoppinglist.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;
import ovh.miroslaw.shoppinglist.rest.errors.ForbiddenException;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.PurchasedIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchasedIngredientServiceImpl implements PurchasedIngredientService {

    private final Logger log = LoggerFactory.getLogger(PurchasedIngredientServiceImpl.class);

    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;

    public PurchasedIngredientServiceImpl(IngredientMapper ingredientMapper, UserRepository userRepository) {
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void addIngredientToShoppingList(Long ingredientId) {
        User user = getCurrentUser();
        Set<Ingredient> purchasedIngredients = user.getPurchasedIngredients();
        Ingredient ingredient = getIngredientByIdFromSet(ingredientId, purchasedIngredients);
        purchasedIngredients.remove(ingredient);
        user.getShoppingList().put(ingredient, 0F);
        userRepository.save(user);
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = getCurrentUser();
        Set<Ingredient> purchasedIngredients = user.getPurchasedIngredients();
        Ingredient deletedIngredient = getIngredientByIdFromSet(id, purchasedIngredients);
        purchasedIngredients.remove(deletedIngredient);
        userRepository.save(user);
    }

    private Ingredient getIngredientByIdFromSet(Long id, Set<Ingredient> ingredientSet) {
        return ingredientSet
            .stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElseThrow(BadRequestException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findUserPurchasedIngredients(Long listId) {
        User user = getCurrentUser();
        return userRepository.findPurchasedIngredients(user.getId())
            .stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private User getCurrentUser() {
        //TODO get user from context
        Optional<User> user = userRepository.findOneById(1L);
        return user.orElseThrow(ForbiddenException::new);
    }
}
