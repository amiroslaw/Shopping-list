package ovh.miroslaw.shoppinglist.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.miroslaw.shoppinglist.service.UnitOfMeasureService;

import java.util.List;

import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * REST controller for managing UnitOfMeasure.
 */
@RestController
@RequestMapping(API_VERSION)
public class UnitOfMeasureResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureResource.class);

    private final UnitOfMeasureService unitOfMeasureService;

    public UnitOfMeasureResource(UnitOfMeasureService unitOfMeasureService) {
        this.unitOfMeasureService = unitOfMeasureService;
    }

    /**
     * GET  /unit-of-measures : get all the unitOfMeasures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of unitOfMeasures in body
     */
    @GetMapping("/unit-of-measures")
    public List<String> getAllUnitOfMeasures() {
        log.debug("REST request to get all UnitOfMeasures");
        return unitOfMeasureService.findAll();
    }

}
