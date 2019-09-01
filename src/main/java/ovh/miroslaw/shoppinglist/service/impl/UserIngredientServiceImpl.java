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
import ovh.miroslaw.shoppinglist.service.UserIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserIngredientServiceImpl implements UserIngredientService {

    private final Logger log = LoggerFactory.getLogger(UserIngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;
    private final IngredientUtil ingredientUtil;

    public UserIngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper,
        UserRepository userRepository, IngredientUtil ingredientUtil) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
        this.ingredientUtil = ingredientUtil;
    }

    @Override
    public IngredientWithAmountDTO addIngredient(IngredientWithAmountDTO ingredientDTO) {
        User user = ingredientUtil.getCurrentUser();
        Ingredient ingredient = ingredientUtil.findOrCreateIngredient(ingredientDTO);
        user.addUserIngredient(ingredient, ingredientDTO.getAmount());
        userRepository.save(user);
        return ingredientMapper.toDtoWithAmount(ingredient, ingredientDTO.getAmount());
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientWithAmountDTO> findUserIngredients() {
        User user = ingredientUtil.getCurrentUser();
        return userRepository.findUserWithEagerIngredients(user.getId())
            .map(User::getUserIngredients)
            .or(() -> Optional.of(Collections.emptyMap()))
            .get().entrySet().stream()
            .map(e -> ingredientMapper.toDtoWithAmount(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public IngredientWithAmountDTO editIngredient(IngredientWithAmountDTO ingredientDTO, Long ingredientId) {
        Ingredient oldIngredient = ingredientRepository.findById(ingredientId)
            .orElseThrow(BadRequestException::new);
        ingredientUtil.validateAmount(ingredientDTO.getAmount());

        User user = ingredientUtil.getCurrentUser();
        Map<Ingredient, Float> ingredientMap = user.getUserIngredients();

        boolean nameChanged = !oldIngredient.getName().equals(ingredientDTO.getName());
        if (nameChanged) {
            ingredientDTO = ingredientUtil.exchangeIngredient(ingredientDTO, oldIngredient, ingredientMap);
        } else {
            ingredientMap.put(ingredientMapper.toEntity(ingredientDTO), ingredientDTO.getAmount());
        }
        userRepository.save(user);
        return ingredientDTO;
    }

    @Override
    public void addIngredientToShoppingList(Long ingredientId) {
        User user = ingredientUtil.getCurrentUser();
        Map<Ingredient, Float> userIngredients = user.getUserIngredients();
        Ingredient ingredient = ingredientUtil.getIngredientByIdFromMap(ingredientId, userIngredients);
        user.getShoppingList().put(ingredient, 0F);
        user.getUserIngredients().put(ingredient, userIngredients.get(ingredient));
        userIngredients.remove(ingredient);
        userRepository.save(user);
    }

   @Override
    public void deleteIngredient(Long id) {
        User user = ingredientUtil.getCurrentUser();
        Map<Ingredient, Float> userIngredients = user.getUserIngredients();
        Ingredient ingredient = ingredientUtil.getIngredientByIdFromMap(id, userIngredients);
        userIngredients.remove(ingredient);
        userRepository.save(user);
    }
}
