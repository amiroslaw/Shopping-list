package ovh.miroslaw.shoppinglist.service.mapper;

import ovh.miroslaw.shoppinglist.domain.*;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import org.mapstruct.*;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithAmountDTO;
import ovh.miroslaw.shoppinglist.service.dto.IngredientWithPopularityDTO;

/**
 * Mapper for the entity Ingredient and its DTO IngredientDTO.
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {

    @Mapping(source = "amount", target = "amount")
    IngredientWithAmountDTO toDtoWithAmount(Ingredient ingredient, Float amount);

    @Named("withPopularity")
    IngredientWithPopularityDTO toDtoWithPopularity(Ingredient ingredient);

    Ingredient toEntity(IngredientDTO ingredientDTO);

    @Named("initPopularity")
    default Ingredient toEntityInitPopularity(IngredientDTO ingredientDTO) {
        if ( ingredientDTO == null ) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId( ingredientDTO.getId() );
        ingredient.setName( ingredientDTO.getName() );
        ingredient.setPopularity(1);

        return ingredient;
    }

    default Ingredient fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        return ingredient;
    }

}
