package ovh.miroslaw.shoppinglist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ovh.miroslaw.shoppinglist.domain.Recipe;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(value = "select distinct recipe from Recipe recipe left join fetch recipe.ingredients",
           countQuery = "select count(distinct recipe) from Recipe recipe")
    Page<Recipe> findAllByVisibleAndFilter(Pageable pageable);

    @Query("select distinct recipe from Recipe recipe join recipe.ingredients where recipe.visible = :visible and lower(recipe.title) like %:filter%")
    Page<Recipe> findAllByVisibleAndFilter(@Param("visible") Boolean visible, @Param("filter") String filter, Pageable pageable);

    @Query("select distinct recipe from Recipe recipe join recipe.users u where u.login = :userLogin and lower(recipe.title) like %:filter%")
    Page<Recipe> findAllRecipesByUserLoginAndFilter(@Param("userLogin") String userLogin, @Param("filter") String filter, Pageable pageable );

    @Query("select recipe from Recipe recipe left join fetch recipe.ingredients where recipe.id =:id")
    Optional<Recipe> findOneWithEagerIngredients(@Param("id") Long id);
}
