package ovh.miroslaw.shoppinglist.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

/**
 * Mapper for the entity Ingredient and its DTO IngredientDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {

    @ValueMapping(source = "PIECE", target = "piece")
    default IngredientDTO toDto(Ingredient ingredient) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setName(ingredient.getIngredientName().getName());
        ingredientDTO.setPopularity(ingredient.getIngredientName().getPopularity());
        ingredientDTO.setAmount(ingredient.getIngredientAmount().getAmount());
        ingredientDTO.setUnit(ingredient.getIngredientAmount().getUnit().name());
        return ingredientDTO;
    }

    Ingredient toEntity(IngredientDTO ingredientDTO);

}
