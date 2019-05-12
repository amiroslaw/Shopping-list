package ovh.miroslaw.shoppinglist.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ovh.miroslaw.shoppinglist.domain.Recipe;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

/**
 * Mapper for the entity Recipe and its DTO RecipeDTO.
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {IngredientMapper.class})
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {

    RecipeDTO toDto(Recipe recipe);

    @Mapping(target = "users", ignore = true)
    Recipe toEntity(RecipeDTO recipeDTO);

}
