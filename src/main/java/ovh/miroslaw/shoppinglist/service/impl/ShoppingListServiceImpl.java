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
import ovh.miroslaw.shoppinglist.service.ShoppingListService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingListServiceImpl implements ShoppingListService {

    private final Logger log = LoggerFactory.getLogger(ShoppingListServiceImpl.class);

    private final UserRepository userRepository;
    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;
    private final IngredientUtil ingredientUtil;

    public ShoppingListServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper,
        UserRepository userRepository, IngredientUtil ingredientUtil) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
        this.ingredientUtil = ingredientUtil;
    }

    @Override
    public void purchasedIngredient(Long ingredientId) {
        User user = ingredientUtil.getCurrentUser();
        Map<Ingredient, Float> shoppingList = user.getShoppingList();
        Ingredient ingredient = ingredientUtil.getIngredientByIdFromMap(ingredientId, shoppingList);
        user.getPurchasedIngredients().add(ingredient);
        user.getUserIngredients().put(ingredient, shoppingList.get(ingredient));
        shoppingList.remove(ingredient);
        userRepository.save(user);
    }

    @Override
    public IngredientWithAmountDTO addIngredient(IngredientWithAmountDTO ingredientDTO) {
        User user = ingredientUtil.getCurrentUser();
        Ingredient ingredient = ingredientUtil.findOrCreateIngredient(ingredientDTO);
        user.addIngredientToShoppingList(ingredient, ingredientDTO.getAmount());
        userRepository.save(user);
        return ingredientMapper.toDtoWithAmount(ingredient, ingredientDTO.getAmount());
    }

    @Override
    public IngredientWithAmountDTO editIngredient(IngredientWithAmountDTO ingredientDTO, Long ingredientId) {
        User user = ingredientUtil.getCurrentUser();
        ingredientUtil.validateAmount(ingredientDTO.getAmount());
        Ingredient oldIngredient = ingredientRepository.findById(ingredientId)
            .orElseThrow(BadRequestException::new);
        Map<Ingredient, Float> shoppingList = user.getShoppingList();
        boolean nameChanged = !oldIngredient.getName().equals(ingredientDTO.getName());
        if (nameChanged) {
            ingredientDTO = ingredientUtil.exchangeIngredient(ingredientDTO, oldIngredient, shoppingList);
        } else {
            shoppingList.put(ingredientMapper.toEntity(ingredientDTO), ingredientDTO.getAmount());
        }
        userRepository.save(user);
        return ingredientDTO;
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = ingredientUtil.getCurrentUser();
        Map<Ingredient, Float> shoppingList = user.getShoppingList();
        Ingredient ingredient = ingredientUtil.getIngredientByIdFromMap(id, shoppingList);
        shoppingList.remove(ingredient);
        userRepository.save(user);
    }

    @Override
    public void deleteAllIngredients() {
        User user = ingredientUtil.getCurrentUser();
        user.setShoppingList(null);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientWithAmountDTO> findShoppingListByUser(Long listId) {
        User user = ingredientUtil.getCurrentUser();
        return userRepository.findUserWithEagerShoppingList(user.getId())
            .map(User::getShoppingList)
            .or(() -> Optional.of(Collections.emptyMap()))
            .get().entrySet().stream()
            .map(e -> ingredientMapper.toDtoWithAmount(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }
}
