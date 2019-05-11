package ovh.miroslaw.shoppinglist.service.mapper;

import ovh.miroslaw.shoppinglist.domain.*;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Recipe and its DTO RecipeDTO.
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {IngredientMapper.class})
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {


    @Mapping(target = "users", ignore = true)
    Recipe toEntity(RecipeDTO recipeDTO);

    default Recipe fromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }
}
