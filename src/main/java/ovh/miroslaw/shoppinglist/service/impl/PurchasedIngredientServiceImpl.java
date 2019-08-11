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
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchasedIngredientServiceImpl implements PurchasedIngredientService {

    private final Logger log = LoggerFactory.getLogger(PurchasedIngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;

    public PurchasedIngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
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

    @Override
    @Transactional(readOnly = true)
    public IngredientDTO addIngredientToPurchasedList(IngredientDTO ingredientDTO) {
        User user = getCurrentUser();
        Optional<Ingredient> ingredient = ingredientRepository.findByName(ingredientDTO.getName().toLowerCase());
        user.addPurchasedIngredient(ingredient.get());
        userRepository.save(user);
        return ingredientMapper.toDto(ingredient.get());
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
