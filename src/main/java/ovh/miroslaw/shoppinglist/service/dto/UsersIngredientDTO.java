package ovh.miroslaw.shoppinglist.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for the User's details about the shopping list.
 */
public class UsersIngredientDTO implements Serializable {

    private Long id;

    private Set<IngredientDTO> shoppingIngredients = new HashSet<>();
    private Set<IngredientDTO> userIngredients = new HashSet<>();

    private Set<IngredientDTO> shoppingPurchasedIngredients = new HashSet<>();

    private Set<RecipeDTO> recipes = new HashSet<>();

    public Set<RecipeDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<IngredientDTO> getShoppingIngredients() {
        return shoppingIngredients;
    }

    public void setShoppingIngredients(Set<IngredientDTO> shoppingIngredients) {
        this.shoppingIngredients = shoppingIngredients;
    }

    public Set<IngredientDTO> getShoppingPurchasedIngredients() {
        return shoppingPurchasedIngredients;
    }

    public void setShoppingPurchasedIngredients(Set<IngredientDTO> shoppingPurchasedIngredients) {
        this.shoppingPurchasedIngredients = shoppingPurchasedIngredients;
    }

    public Set<IngredientDTO> getUserIngredients() {
        return userIngredients;
    }

    public void setUserIngredients(Set<IngredientDTO> userIngredients) {
        this.userIngredients = userIngredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsersIngredientDTO that = (UsersIngredientDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(shoppingIngredients, that.shoppingIngredients) &&
            Objects.equals(recipes, that.recipes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shoppingIngredients, recipes);
    }

    @Override
    public String toString() {
        return "UsersIngredientDTO{" +
            "id=" + id +
            ", shoppingList=" + shoppingIngredients +
            ", purchasedIngredients=" + shoppingPurchasedIngredients +
            ", userIngredients=" + userIngredients +
            ", recipes=" + recipes +
            '}';
    }
}
