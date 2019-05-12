package ovh.miroslaw.shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ovh.miroslaw.shoppinglist.domain.IngredientName;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IngredientNameRepository extends JpaRepository<IngredientName, Long> {

    Optional<IngredientName> findByName(String name);

    @Query("SELECT ingredientName.name FROM IngredientName ingredientName")
    Set<String> findAllNames();
}
