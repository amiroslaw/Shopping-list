package ovh.miroslaw.shoppinglist.service.impl;

import ovh.miroslaw.shoppinglist.service.RecipeService;
import ovh.miroslaw.shoppinglist.domain.Recipe;
import ovh.miroslaw.shoppinglist.repository.RecipeRepository;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;
import ovh.miroslaw.shoppinglist.service.mapper.RecipeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public RecipeDTO save(RecipeDTO recipeDTO) {
        log.debug("Request to save Recipe : {}", recipeDTO);
        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> findAll() {
        log.debug("Request to get all Recipes");
        return recipeRepository.findAllWithEagerRelationships().stream()
            .map(recipeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<RecipeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recipeRepository.findAllWithEagerRelationships(pageable).map(recipeMapper::toDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeDTO> findOne(Long id) {
        log.debug("Request to get Recipe : {}", id);
        return recipeRepository.findOneWithEagerRelationships(id)
            .map(recipeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Recipe : {}", id);
        recipeRepository.deleteById(id);
    }
}
