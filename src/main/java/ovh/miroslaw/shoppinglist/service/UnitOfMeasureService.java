package ovh.miroslaw.shoppinglist.service;

import ovh.miroslaw.shoppinglist.service.dto.UnitOfMeasureDTO;

import java.util.List;
import java.util.Optional;

public interface UnitOfMeasureService {

    UnitOfMeasureDTO save(UnitOfMeasureDTO unitOfMeasureDTO);

    List<UnitOfMeasureDTO> findAll();
    /**
     * Get all the UnitOfMeasureDTO where Ingredient is null.
     *
     * @return the list of entities
     */
    List<UnitOfMeasureDTO> findAllWhereIngredientIsNull();

    Optional<UnitOfMeasureDTO> findOne(Long id);

    void delete(Long id);
}
