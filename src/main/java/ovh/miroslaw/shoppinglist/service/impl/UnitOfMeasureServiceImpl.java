package ovh.miroslaw.shoppinglist.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.domain.enumeration.UnitOfMeasure;
import ovh.miroslaw.shoppinglist.service.UnitOfMeasureService;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * The Unit of measure service.
 */
@Service
@Transactional(readOnly = true)
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureServiceImpl.class);

    @Override
    public List<String> findAll() {
        log.debug("Request to get all UnitOfMeasures");
        return Arrays.stream(UnitOfMeasure.values())
            .map(UnitOfMeasure::getUnit)
            .collect(toList());
    }

}
