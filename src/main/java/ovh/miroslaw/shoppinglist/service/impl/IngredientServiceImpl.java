package ovh.miroslaw.shoppinglist.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.IngredientAmount;
import ovh.miroslaw.shoppinglist.domain.IngredientName;
import ovh.miroslaw.shoppinglist.domain.enumeration.UnitOfMeasure;
import ovh.miroslaw.shoppinglist.repository.IngredientAmountRepository;
import ovh.miroslaw.shoppinglist.repository.IngredientNameRepository;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * The Ingredient service.
 */
@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final IngredientAmountRepository ingredientAmountRepository;
    private final IngredientNameRepository ingredientNameRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final UnaryOperator<IngredientName> increasePopularity = e -> {
        e.setPopularity(e.getPopularity() + 1);
        return e;
    };

    /**
     * Instantiates a new Ingredient service.
     *
     * @param ingredientRepository the ingredient repository
     * @param ingredientAmountRepository the ingredient amount repository
     * @param ingredientNameRepository the ingredient name repository
     * @param ingredientMapper the ingredient mapper
     */
    public IngredientServiceImpl(
        IngredientRepository ingredientRepository,
        IngredientAmountRepository ingredientAmountRepository,
        IngredientNameRepository ingredientNameRepository,
        IngredientMapper ingredientMapper
    ) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientAmountRepository = ingredientAmountRepository;
        this.ingredientNameRepository = ingredientNameRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> findAllNames() {
        log.debug("Request to get all Ingredients");
        return ingredientNameRepository.findAllNames();
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

    @Override
    @Transactional(readOnly = true)
    public Ingredient findByNameOrCreateIngredient(IngredientDTO ingredientDTO) {
        final String nameInLowercase = ingredientDTO.getName().toLowerCase();
        IngredientName ingredientName = ingredientNameRepository.findByName(nameInLowercase)
            .map(increasePopularity)
            .orElse(new IngredientName(nameInLowercase, 1));

        final UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(ingredientDTO.getUnit());

        final IngredientAmount ingredientAmount = ingredientAmountRepository
            .findByAmountAndUnit(ingredientDTO.getAmount(), unitOfMeasure)
            .orElse(new IngredientAmount(ingredientDTO.getAmount(), unitOfMeasure));
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName(ingredientNameRepository.save(ingredientName));
        ingredient.setIngredientAmount(ingredientAmountRepository.save(ingredientAmount));
        return ingredientRepository.save(ingredient);
    }

}
