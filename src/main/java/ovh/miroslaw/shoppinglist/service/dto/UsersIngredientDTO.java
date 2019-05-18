package ovh.miroslaw.shoppinglist.service.dto;

import java.io.Serializable;
import java.util.*;

public class UsersIngredientDTO implements Serializable {
    public UsersIngredientDTO(){}
    private Long id;

    private Map<IngredientDTO, Float> shoppingList = new HashMap<>();
    private Map<IngredientDTO, Float> userIngredients  = new HashMap<>();

    private Set<IngredientDTO> purchasedIngredients = new HashSet<>();


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

    public Map<IngredientDTO, Float> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(Map<IngredientDTO, Float> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Set<IngredientDTO> getPurchasedIngredients() {
        return purchasedIngredients;
    }

    public void setPurchasedIngredients(Set<IngredientDTO> purchasedIngredients) {
        this.purchasedIngredients = purchasedIngredients;
    }

    public Map<IngredientDTO, Float> getUserIngredients() {
        return userIngredients;
    }

    public void setUserIngredients(Map<IngredientDTO, Float> userIngredients) {
        this.userIngredients = userIngredients;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersIngredientDTO that = (UsersIngredientDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(shoppingList, that.shoppingList) &&
            Objects.equals(recipes, that.recipes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,  shoppingList, recipes);
    }

    @Override
    public String toString() {
        return "UsersIngredientDTO{" +
            "id=" + id +
            ", shoppingList=" + shoppingList +
            ", purchasedIngredients=" + purchasedIngredients +
            ", userIngredients=" + userIngredients +
            ", recipes=" + recipes +
            '}';
    }
}
