package ovh.miroslaw.shoppinglist.service.mapper;

import ovh.miroslaw.shoppinglist.domain.*;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ingredient and its DTO IngredientDTO.
 */
@Mapper(componentModel = "spring", uses = {UnitOfMeasureMapper.class})
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {

    @Mapping(source = "unitOfMeasure.id", target = "unitOfMeasureId")
    IngredientDTO toDto(Ingredient ingredient);

//    @Mapping(target = "recipes", ignore = true)
    Ingredient toEntity(IngredientDTO ingredientDTO);

    default Ingredient fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        return ingredient;
    }
}
