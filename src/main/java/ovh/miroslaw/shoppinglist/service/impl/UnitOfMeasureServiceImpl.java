package ovh.miroslaw.shoppinglist.service.impl;

import ovh.miroslaw.shoppinglist.domain.UnitOfMeasure;
import ovh.miroslaw.shoppinglist.service.UnitOfMeasureService;
import ovh.miroslaw.shoppinglist.repository.UnitOfMeasureRepository;
import ovh.miroslaw.shoppinglist.service.dto.UnitOfMeasureDTO;
import ovh.miroslaw.shoppinglist.service.mapper.UnitOfMeasureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureServiceImpl.class);

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final UnitOfMeasureMapper unitOfMeasureMapper;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureMapper unitOfMeasureMapper) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureMapper = unitOfMeasureMapper;
    }

    @Override
    public UnitOfMeasureDTO save(UnitOfMeasureDTO unitOfMeasureDTO) {
        log.debug("Request to save UnitOfMeasure : {}", unitOfMeasureDTO);
        UnitOfMeasure unitOfMeasure = unitOfMeasureMapper.toEntity(unitOfMeasureDTO);
        unitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);
        return unitOfMeasureMapper.toDto(unitOfMeasure);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitOfMeasureDTO> findAll() {
        log.debug("Request to get all UnitOfMeasures");
        return unitOfMeasureRepository.findAll().stream()
            .map(unitOfMeasureMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<UnitOfMeasureDTO> findAllWhereIngredientIsNull() {
        log.debug("Request to get all unitOfMeasures where Ingredient is null");
        return StreamSupport
            .stream(unitOfMeasureRepository.findAll().spliterator(), false)
            .filter(unitOfMeasure -> unitOfMeasure.getIngredient() == null)
            .map(unitOfMeasureMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitOfMeasureDTO> findOne(Long id) {
        log.debug("Request to get UnitOfMeasure : {}", id);
        return unitOfMeasureRepository.findById(id)
            .map(unitOfMeasureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UnitOfMeasure : {}", id);
        unitOfMeasureRepository.deleteById(id);
    }
}
