package ovh.miroslaw.shoppinglist.service.mapper;

import ovh.miroslaw.shoppinglist.domain.*;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ingredient and its DTO IngredientDTO.
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {

//    @Mapping(source = "unitOfMeasure.id", target = "unitOfMeasureId")
    @Mapping(source = "ingredient.id", target = "id")
    @Mapping(source = "ingredient.name", target = "name")
    @Mapping(source = "ingredient.popularity", target = "popularity")
    @Mapping(source = "amount", target = "amount")
    IngredientDTO toDto(Ingredient ingredient, Float amount);
//    IngredientDTO toDto(Ingredient ingredient);

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
