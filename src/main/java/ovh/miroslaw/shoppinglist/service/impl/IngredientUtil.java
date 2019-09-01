package ovh.miroslaw.shoppinglist.service.impl;

import org.springframework.stereotype.Service;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;
import ovh.miroslaw.shoppinglist.rest.errors.ForbiddenException;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IngredientUtil {

    private final UserRepository userRepository;
    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;

    public IngredientUtil(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper,
        UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
    }

    User getCurrentUser() {
        //TODO get user from context
        Optional<User> user = userRepository.findOneById(1L);
        return user.orElseThrow(ForbiddenException::new);
    }

    IngredientWithAmountDTO exchangeIngredient(IngredientWithAmountDTO ingredientDTO, Ingredient oldIngredient,
        Map<Ingredient, Float> ingredientMap) {
        ingredientMap.remove(oldIngredient);
        ingredientDTO.setId(null);
        Ingredient newIngredient = findOrCreateIngredient(ingredientDTO);
        ingredientMap.put(newIngredient, ingredientDTO.getAmount());
        ingredientDTO = ingredientMapper.toDtoWithAmount(newIngredient, ingredientDTO.getAmount());
        return ingredientDTO;
    }

    Ingredient findOrCreateIngredient(IngredientDTO ingredientDTO) {
        ingredientDTO.setId(null);
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

    void validateAmount(Float amount) {
        boolean invalidAmount = amount.isNaN() || amount < 0.0;
        if (invalidAmount) {
            throw new BadRequestException();
        }
    }
    Ingredient getIngredientByIdFromMap(Long id, Map<Ingredient, Float> ingredientMap) {
        Map<Long, Ingredient> longIngredientMap = ingredientMap
            .entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getId(),
                entry -> entry.getKey())
            );
        return Optional.ofNullable(longIngredientMap.get(id))
            .orElseThrow(BadRequestException::new);
    }

    Ingredient getIngredientByIdFromSet(Long id, Set<Ingredient> ingredientSet) {
        return ingredientSet
            .stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElseThrow(BadRequestException::new);
    }

}

