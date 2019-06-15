package ovh.miroslaw.shoppinglist.service.impl;

import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;
import ovh.miroslaw.shoppinglist.rest.errors.ForbiddenException;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithPopularityDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
    }

    @Override
    public IngredientWithAmountDTO addIngredientToUser(IngredientWithAmountDTO ingredientDTO) {
        User user = getCurrentUser();
        Ingredient ingredient = findOrCreateIngredient(ingredientDTO);
        user.addUserIngredient(ingredient, ingredientDTO.getAmount());
        userRepository.save(user);
        return ingredientMapper.toDtoWithAmount(ingredient, ingredientDTO.getAmount());
    }

    @Override
    public IngredientWithAmountDTO addIngredientToShoppingList(IngredientWithAmountDTO ingredientDTO) {
        User user = getCurrentUser();
        Ingredient ingredient = findOrCreateIngredient(ingredientDTO);
        user.addIngredientToShoppingList(ingredient, ingredientDTO.getAmount());
        userRepository.save(user);
        return ingredientMapper.toDtoWithAmount(ingredient, ingredientDTO.getAmount());
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientDTO addIngredientToPurchasedList(IngredientDTO ingredientDTO) {
        User user = getCurrentUser();
        userRepository.save(user);
        Optional<Ingredient> ingredient = ingredientRepository.findByName(ingredientDTO.getName().toLowerCase());
        user.addPurchasedIngredient(ingredient.get());
        userRepository.save(user);
        return ingredientMapper.toDto(ingredient.get());
    }

    @Override
    public IngredientDTO addIngredientToRecipe(Long recipeId, IngredientDTO ingredientDTO) {
        return null;
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
                ing = ingredientRepository.save(ing);
                return ing;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Map<IngredientDTO, Float> findUserShoppingList(Long listId) {
        User user = getCurrentUser();
        return userRepository.findUserWithEagerShoppingList(user.getId())
            .map(User::getShoppingList)
            .or(() -> Optional.of(Collections.emptyMap()))
            .get().entrySet().stream()
            .collect(Collectors.toMap(
                e -> ingredientMapper.toDto(e.getKey()),
                e -> e.getValue()
            ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<IngredientDTO, Float> findUserIngredients() {
        User user = getCurrentUser();
        return userRepository.findUserWithEagerIngredients(user.getId())
            .map(User::getUserIngredients)
            .or(() -> Optional.of(Collections.emptyMap()))
            .get().entrySet().stream()
            .collect(Collectors.toMap(
                e -> ingredientMapper.toDto(e.getKey()),
                e -> e.getValue()
            ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientWithAmountDTO> findUserIngredients2() {
        User user = getCurrentUser();
        return userRepository.findUserWithEagerIngredients(user.getId())
            .map(User::getUserIngredients)
            .or(() -> Optional.of(Collections.emptyMap()))
            .get().entrySet().stream()
            .map(e -> ingredientMapper.toDtoWithAmount(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
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

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findAll() {
        log.debug("Request to get all Ingredients");
        return ingredientRepository.findAll().stream()
            .map(ingredientMapper::toDtoWithPopularity)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientDTO> findOne(Long id) {
        log.debug("Request to get Ingredient : {}", id);
        return ingredientRepository.findById(id)
            .map(ingredientMapper::toDtoWithPopularity);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingredient : {}", id);
        ingredientRepository.deleteById(id);
    }

    private User getCurrentUser() {
        //TODO get user from context
        Optional<User> user = userRepository.findOneById(1L);
        return user.orElseThrow(ForbiddenException::new);
    }
}
