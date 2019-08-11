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
import ovh.miroslaw.shoppinglist.service.ShoppingListService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
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

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;

    public ShoppingListServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void purchasedIngredient(Long ingredientId) {
        User user = getCurrentUser();
        Map<Ingredient, Float> shoppingList = user.getShoppingList();
        Ingredient ingredient = getIngredientByIdFromMap(ingredientId, shoppingList);
        user.getPurchasedIngredients().add(ingredient);
        user.getUserIngredients().put(ingredient, shoppingList.get(ingredient));
        shoppingList.remove(ingredient);
        userRepository.save(user);
    }

    @Override
    public IngredientWithAmountDTO addIngredient(IngredientWithAmountDTO ingredientDTO) {
        User user = getCurrentUser();
        Ingredient ingredient = findOrCreateIngredient(ingredientDTO);
        user.addIngredientToShoppingList(ingredient, ingredientDTO.getAmount());
        userRepository.save(user);
        return ingredientMapper.toDtoWithAmount(ingredient, ingredientDTO.getAmount());
    }

    @Override
    public IngredientWithAmountDTO editIngredient(IngredientWithAmountDTO ingredientDTO, Long ingredientId) {
        User user = getCurrentUser();
        Ingredient oldIngredient = ingredientRepository.findById(ingredientId)
            .orElseThrow(BadRequestException::new);
        Map<Ingredient, Float> shoppingList = user.getShoppingList();
        boolean nameChanged = !oldIngredient.getName().equals(ingredientDTO.getName());
        if (nameChanged) {
            ingredientDTO = exchangeIngredient(ingredientDTO, oldIngredient, shoppingList);
        } else {
            shoppingList.put(ingredientMapper.toEntity(ingredientDTO), ingredientDTO.getAmount());
        }
        userRepository.save(user);
        return ingredientDTO;
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = getCurrentUser();
        Map<Ingredient, Float> shoppingList = user.getShoppingList();
        Ingredient ingredient = getIngredientByIdFromMap(id, shoppingList);
        shoppingList.remove(ingredient);
        userRepository.save(user);
    }

    @Override
    public void deleteAllIngredients() {
        User user = getCurrentUser();
        user.setShoppingList(null);
        userRepository.save(user);
    }

    private Ingredient getIngredientByIdFromMap(Long id, Map<Ingredient, Float> shoppingList) {
        Map<Long, Ingredient> longIngredientMap = shoppingList
            .entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getId(),
                entry -> entry.getKey())
        );
        return Optional.ofNullable(longIngredientMap.get(id))
            .orElseThrow(BadRequestException::new);
    }

    private IngredientWithAmountDTO exchangeIngredient(IngredientWithAmountDTO ingredientDTO, Ingredient oldIngredient, Map<Ingredient, Float> ingredientMap) {
        ingredientMap.remove(oldIngredient);
        ingredientDTO.setId(null);
        Ingredient newIngredient = findOrCreateIngredient(ingredientDTO);
        ingredientMap.put(newIngredient, ingredientDTO.getAmount());
        ingredientDTO = ingredientMapper.toDtoWithAmount(newIngredient, ingredientDTO.getAmount());
        return ingredientDTO;
    }

    private Ingredient findOrCreateIngredient(IngredientDTO ingredientDTO) {
        final String nameInLowercase = ingredientDTO.getName().toLowerCase();
        return ingredientRepository.findByName(nameInLowercase)
            .map(e -> {
                e.setPopularity(e.getPopularity() + 1);
                return ingredientRepository.save(e);
            })
            .orElseGet(() -> {
                Ingredient ing = ingredientMapper.toEntityInitPopularity(ingredientDTO);
                return ingredientRepository.save(ing);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientWithAmountDTO> findShoppingListByUser(Long listId) {
        User user = getCurrentUser();
        return userRepository.findUserWithEagerShoppingList(user.getId())
            .map(User::getShoppingList)
            .or(() -> Optional.of(Collections.emptyMap()))
            .get().entrySet().stream()
            .map(e -> ingredientMapper.toDtoWithAmount(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        //TODO get user from context
        Optional<User> user = userRepository.findOneById(1L);
        return user.orElseThrow(ForbiddenException::new);
    }
}
