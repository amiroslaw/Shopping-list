package ovh.miroslaw.shoppinglist.service;

import java.util.List;

/**
 * The interface Unit of measure service.
 */
public interface UnitOfMeasureService {

    /**
     * Finds all the unit of measure.
     *
     * @return the list of the stings of unit of measure
     */
    List<String> findAll();

}
