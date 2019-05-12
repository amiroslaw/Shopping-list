package ovh.miroslaw.shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.miroslaw.shoppinglist.domain.IngredientAmount;
import ovh.miroslaw.shoppinglist.domain.enumeration.UnitOfMeasure;

import java.util.Optional;

@Repository
public interface IngredientAmountRepository extends JpaRepository<IngredientAmount, Long> {

    Optional<IngredientAmount> findByAmountAndUnit(Float amount, UnitOfMeasure unit);
}
