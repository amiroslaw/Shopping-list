package ovh.miroslaw.shoppinglist.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.rest.errors.ForbiddenException;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.UserIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserIngredientServiceImpl implements UserIngredientService {

    private final Logger log = LoggerFactory.getLogger(UserIngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;

    public UserIngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, UserRepository userRepository) {
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
    public List<IngredientWithAmountDTO> findUserIngredients() {
        User user = getCurrentUser();
        return userRepository.findUserWithEagerIngredients(user.getId())
            .map(User::getUserIngredients)
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
