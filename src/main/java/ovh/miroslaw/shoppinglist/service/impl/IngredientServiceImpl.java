package ovh.miroslaw.shoppinglist.service.impl;

import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public IngredientDTO save(IngredientDTO ingredientDTO) {
        log.debug("Request to save Ingredient : {}", ingredientDTO);
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO);
        ingredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(ingredient);
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
