package ovh.miroslaw.shoppinglist.service.mapper;

import ovh.miroslaw.shoppinglist.domain.*;
import ovh.miroslaw.shoppinglist.service.dto.UnitOfMeasureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UnitOfMeasure and its DTO UnitOfMeasureDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UnitOfMeasureMapper extends EntityMapper<UnitOfMeasureDTO, UnitOfMeasure> {


    @Mapping(target = "ingredient", ignore = true)
    UnitOfMeasure toEntity(UnitOfMeasureDTO unitOfMeasureDTO);

    default UnitOfMeasure fromId(Long id) {
        if (id == null) {
            return null;
        }
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(id);
        return unitOfMeasure;
    }
}
