package ovh.miroslaw.shoppinglist.service;

import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    RecipeDTO save(RecipeDTO recipeDTO);

    List<RecipeDTO> findAll();

    Page<RecipeDTO> findAllWithEagerRelationships(Pageable pageable);
    
    Optional<RecipeDTO> findOne(Long id);

    void delete(Long id);
}
