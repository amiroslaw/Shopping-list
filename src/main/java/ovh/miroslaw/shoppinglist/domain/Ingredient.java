package ovh.miroslaw.shoppinglist.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity class for Ingredient.
 */
@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "shoppingIngredients")
    private Set<User> shoppingIngredients = new HashSet<>();

    @ManyToMany(mappedBy = "ingredients")
    private Set<Recipe> recipe = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "amount_id")
    private IngredientAmount ingredientAmount;

    @ManyToOne
    @JoinColumn(name = "name_id")
    private IngredientName ingredientName;

    @ManyToMany(mappedBy = "userIngredients")
    private Set<User> userIngredients = new HashSet<>();

    @ManyToMany(mappedBy = "shoppingPurchasedIngredients")
    private Set<User> shoppingPurchasedIngredients = new HashSet<>();

    public Set<User> getShoppingPurchasedIngredients() {
        return shoppingPurchasedIngredients;
    }

    public Set<Recipe> getRecipe() {
        return recipe;
    }

    public void setRecipe(Set<Recipe> recipe) {
        this.recipe = recipe;
    }

    public IngredientAmount getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(IngredientAmount ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    public IngredientName getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(IngredientName ingredientsName) {
        this.ingredientName = ingredientsName;
    }

    public Long getId() {
        return id;
    }

    public Set<User> getUserIngredients() {
        return userIngredients;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getShoppingIngredients() {
        return shoppingIngredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ingredient{id=" + id + '}';
    }
}
