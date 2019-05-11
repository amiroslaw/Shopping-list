package ovh.miroslaw.shoppinglist.repository;

import ovh.miroslaw.shoppinglist.domain.UnitOfMeasure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {

}
