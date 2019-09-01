package ovh.miroslaw.shoppinglist.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.service.PurchasedIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchasedIngredientServiceImpl implements PurchasedIngredientService {

    private final Logger log = LoggerFactory.getLogger(PurchasedIngredientServiceImpl.class);

    private final IngredientMapper ingredientMapper;
    private final IngredientUtil ingredientUtil;
    private final UserRepository userRepository;

    public PurchasedIngredientServiceImpl(IngredientMapper ingredientMapper,
        IngredientUtil ingredientUtil, UserRepository userRepository) {
        this.ingredientMapper = ingredientMapper;
        this.ingredientUtil = ingredientUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void addIngredientToShoppingList(Long ingredientId) {
        User user = ingredientUtil.getCurrentUser();
        Set<Ingredient> purchasedIngredients = user.getPurchasedIngredients();
        Ingredient ingredient = ingredientUtil.getIngredientByIdFromSet(ingredientId, purchasedIngredients);
        purchasedIngredients.remove(ingredient);
        user.getShoppingList().put(ingredient, 0F);
        userRepository.save(user);
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = ingredientUtil.getCurrentUser();
        Set<Ingredient> purchasedIngredients = user.getPurchasedIngredients();
        Ingredient deletedIngredient = ingredientUtil.getIngredientByIdFromSet(id, purchasedIngredients);
        purchasedIngredients.remove(deletedIngredient);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findUserPurchasedIngredients(Long listId) {
        User user = ingredientUtil.getCurrentUser();
        return userRepository.findPurchasedIngredients(user.getId())
            .stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
