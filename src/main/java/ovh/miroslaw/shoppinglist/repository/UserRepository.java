package ovh.miroslaw.shoppinglist.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.Recipe;
import ovh.miroslaw.shoppinglist.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);
    Optional<User> findOneById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    @Query("select distinct user from User user left join fetch user.shoppingIngredients")
    List<User> findAllUsersWithEagerShoppingList();

    @Query("select  user from User user left join fetch user.shoppingIngredients where user.id =:id")
    Optional<User> findUserWithEagerShoppingList(@Param("id") Long id);

    @Query("select  user from User user left join fetch user.userIngredients where user.id =:id")
    Optional<User> findUserWithEagerIngredients(@Param("id") Long id);

    @Query("select user from User user left join fetch user.shoppingPurchasedIngredients i where user.id =:id")
    Optional<User> findUserWithEagerPurchasedIngredients(@Param("id") Long id);

    @Query("select i from User user left join user.shoppingPurchasedIngredients i where user.id =:id")
    List<Ingredient> findPurchasedIngredients(@Param("id") Long id);

    @Query("select i from User user left join user.recipes i where user.id =:id")
    List<Recipe> findRecipes(@Param("id") Long id);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
}
