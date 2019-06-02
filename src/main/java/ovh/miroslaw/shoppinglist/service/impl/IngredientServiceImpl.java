package ovh.miroslaw.shoppinglist.service.impl;

import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;
import ovh.miroslaw.shoppinglist.rest.errors.ForbiddenException;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
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
    public IngredientDTO addIngredientToUser(Long userId, IngredientDTO ingredientDTO) {
        Optional<User> user = userRepository.findOneById(userId);
        if (user.isEmpty()) {
            throw new ForbiddenException();
            //TODO check for security. has logged user this ID
        }
        Ingredient ingredient = findOrCreateIngredient(ingredientDTO);
        user.get().addUserIngredient(ingredient, ingredientDTO.getAmount());
        return ingredientMapper.toDto(ingredient, ingredientDTO.getAmount());
    }

    @Override
    public IngredientDTO addIngredientToShoppingList(Long userId, IngredientDTO ingredientDTO) {
        Optional<User> user = userRepository.findOneById(userId);
        if (user.isEmpty()) {
            throw new ForbiddenException();
            //TODO check for security. has logged user this ID
        }
        Ingredient ingredient = findOrCreateIngredient(ingredientDTO);
        user.get().addIngredientToShoppingList(ingredient, ingredientDTO.getAmount());
        return ingredientMapper.toDto(ingredient, ingredientDTO.getAmount());
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientDTO addIngredientToPurchasedList(Long userId, IngredientDTO ingredientDTO) {
        Optional<User> user = userRepository.findOneById(userId);
        if (user.isEmpty()) {
            throw new ForbiddenException();
            //TODO check for security. has logged user this ID
        }
        Optional<Ingredient> ingredient = ingredientRepository.findByName(ingredientDTO.getName().toLowerCase());
        if (ingredient.isEmpty()) {
            throw new BadRequestException("Entity does not exits");
        }
        user.get().addPurchasedIngredient(ingredient.get());
        return ingredientMapper.toDto(ingredient.get());
    }

    @Override
    public IngredientDTO addIngredientToRecipe(Long recipeId, IngredientDTO ingredientDTO) {
        return null;
    }

    private Ingredient findOrCreateIngredient(IngredientDTO ingredientDTO){
        final String nameInLowercase = ingredientDTO.getName().toLowerCase();
        return ingredientRepository.findByName(nameInLowercase)
            .orElseGet(() -> {
                Ingredient ing = ingredientMapper.toEntity(ingredientDTO);
                ing = ingredientRepository.save(ing);
                return ing;
            });
    }
    @Override
    @Transactional(readOnly = true)
    public Map<IngredientDTO, Float> findUserShoppingList(Long userId) {
        return userRepository.findUserWithEagerShoppingList(userId)
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
    public Map<IngredientDTO, Float> findUserIngredients(Long userId) {
        return userRepository.findUserWithEagerIngredients(userId)
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
    public List<IngredientDTO> findUserPurchasedIngredients(Long userId) {
        return userRepository.findPurchasedIngredients(userId)
            .stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findAll() {
        log.debug("Request to get all Ingredients");
        return ingredientRepository.findAll().stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientDTO> findOne(Long id) {
        log.debug("Request to get Ingredient : {}", id);
        return ingredientRepository.findById(id)
            .map(ingredientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingredient : {}", id);
        ingredientRepository.deleteById(id);
    }
}
