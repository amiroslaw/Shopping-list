package ovh.miroslaw.shoppinglist.repository;

import ovh.miroslaw.shoppinglist.domain.Ingredient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
