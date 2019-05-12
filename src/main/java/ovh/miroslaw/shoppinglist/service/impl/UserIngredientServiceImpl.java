package ovh.miroslaw.shoppinglist.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.rest.errors.ForbiddenException;
import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.security.SecurityUtils;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.UserIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The User ingredient service.
 */
@Service
@Transactional
public class UserIngredientServiceImpl implements UserIngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;
    private final IngredientService ingredientService;

    /**
     * Instantiates a new User ingredient service.
     *
     * @param ingredientRepository the ingredient repository
     * @param ingredientMapper the ingredient mapper
     * @param userRepository the user repository
     * @param ingredientService the ingredient service
     */
    public UserIngredientServiceImpl(IngredientRepository ingredientRepository,
        IngredientMapper ingredientMapper,
        UserRepository userRepository,
        IngredientService ingredientService) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
        this.ingredientService = ingredientService;
    }

    /**
     * Gets current user.
     *
     * @return the current user
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(ForbiddenException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findUserIngredients() {
        User user = this.getCurrentUser();
        return userRepository.findUserWithEagerIngredients(user.getId())
            .map(User::getUserIngredients)
            .stream()
            .flatMap(Collection::stream)
            .map(ingredientMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public IngredientDTO addIngredient(IngredientDTO ingredientDTO) {
        User user = this.getCurrentUser();
        Ingredient ingredient = ingredientService.findByNameOrCreateIngredient(ingredientDTO);
        user.addUserIngredient(ingredient);
        userRepository.save(user);
        return ingredientMapper.toDto(ingredient);
    }

    @Override
    public IngredientDTO editIngredient(IngredientDTO ingredientDTO) {
        deleteIngredient(ingredientDTO.getId());
        User user = this.getCurrentUser();
        Ingredient ingredient = ingredientService.findByNameOrCreateIngredient(ingredientDTO);
        user.addUserIngredient(ingredient);
        userRepository.save(user);
        return ingredientMapper.toDto(ingredient);
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = this.getCurrentUser();
        ingredientRepository.findById(id)
            .ifPresent(user::removeUserIngredient);
        userRepository.save(user);
    }

    @Override
    public void addIngredientToShoppingList(Long ingredientId) {
        User user = this.getCurrentUser();
        ingredientRepository.findById(ingredientId)
            .map(user::addShoppingIngredient)
            .orElseThrow(NotFoundException::new);
        userRepository.save(user);
        deleteIngredient(ingredientId);
    }

}
