package ovh.miroslaw.shoppinglist.rest;

import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;
import ovh.miroslaw.shoppinglist.rest.util.ResponseUtil;
import ovh.miroslaw.shoppinglist.service.UnitOfMeasureService;
import ovh.miroslaw.shoppinglist.service.dto.UnitOfMeasureDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UnitOfMeasure.
 */
@RestController
@RequestMapping("/api")
public class UnitOfMeasureResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureResource.class);

    private final UnitOfMeasureService unitOfMeasureService;

    public UnitOfMeasureResource(UnitOfMeasureService unitOfMeasureService) {
        this.unitOfMeasureService = unitOfMeasureService;
    }

    /**
     * POST  /unit-of-measures : Create a new unitOfMeasure.
     *
     * @param unitOfMeasureDTO the unitOfMeasureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unitOfMeasureDTO, or with status 400 (Bad Request) if the unitOfMeasure has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/unit-of-measures")
    public ResponseEntity<UnitOfMeasureDTO> createUnitOfMeasure(@Valid @RequestBody UnitOfMeasureDTO unitOfMeasureDTO) throws URISyntaxException {
        log.debug("REST request to save UnitOfMeasure : {}", unitOfMeasureDTO);
        UnitOfMeasureDTO result = unitOfMeasureService.save(unitOfMeasureDTO);
        return ResponseEntity.created(new URI("/api/unit-of-measures/" + result.getId()))
            .body(result);
    }

    /**
     * PUT  /unit-of-measures : Updates an existing unitOfMeasure.
     *
     * @param unitOfMeasureDTO the unitOfMeasureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unitOfMeasureDTO,
     * or with status 400 (Bad Request) if the unitOfMeasureDTO is not valid,
     * or with status 500 (Internal Server Error) if the unitOfMeasureDTO couldn't be updated
     */
    @PutMapping("/unit-of-measures")
    public ResponseEntity<UnitOfMeasureDTO> updateUnitOfMeasure(@Valid @RequestBody UnitOfMeasureDTO unitOfMeasureDTO) {
        log.debug("REST request to update UnitOfMeasure : {}", unitOfMeasureDTO);
        if (unitOfMeasureDTO.getId() == null) {
            throw new BadRequestException("Bad request- id does not exist");
        }
        UnitOfMeasureDTO result = unitOfMeasureService.save(unitOfMeasureDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * GET  /unit-of-measures : get all the unitOfMeasures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of unitOfMeasures in body
     */
    @GetMapping("/unit-of-measures")
    public List<UnitOfMeasureDTO> getAllUnitOfMeasures() {
        log.debug("REST request to get all UnitOfMeasures");
        return unitOfMeasureService.findAll();
    }

    /**
     * GET  /unit-of-measures/:id : get the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unitOfMeasureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/unit-of-measures/{id}")
    public ResponseEntity<UnitOfMeasureDTO> getUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to get UnitOfMeasure : {}", id);
        Optional<UnitOfMeasureDTO> unitOfMeasureDTO = unitOfMeasureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitOfMeasureDTO);
    }

    /**
     * DELETE  /unit-of-measures/:id : delete the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/unit-of-measures/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to delete UnitOfMeasure : {}", id);
        unitOfMeasureService.delete(id);
        return ResponseEntity.ok().build();
    }
}
