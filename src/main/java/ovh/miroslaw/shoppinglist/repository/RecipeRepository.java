package ovh.miroslaw.shoppinglist.repository;

import ovh.miroslaw.shoppinglist.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(value = "select distinct recipe from Recipe recipe left join fetch recipe.ingredients",
        countQuery = "select count(distinct recipe) from Recipe recipe")
    Page<Recipe> findAllWithEagerIngredients(Pageable pageable);

    @Query(value = "select distinct recipe from Recipe recipe left join fetch recipe.ingredients")
    List<Recipe> findAllWithEagerIngredients();

    @Query("select recipe from Recipe recipe left join fetch recipe.ingredients where recipe.id =:id")
    Optional<Recipe> findOneWithEagerIngredients(@Param("id") Long id);
}
