package ovh.miroslaw.shoppinglist.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(unique = true)
    private UnitOfMeasure unitOfMeasure;

//    @ManyToMany(mappedBy = "ingredients")
//    @JsonIgnore
//    @ManyToMany
//    @JoinTable(name = "recipe_ingredients",
//        joinColumns = @JoinColumn(name = "ingredients_id", referencedColumnName = "id"),
//        inverseJoinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"))
//    @MapKey(name = "amount")
//    private Map<Float, Ingredient> recipes = new HashMap<>();
//    private Set<Recipe> recipes = new HashSet<>();

    private Float amount;
    @ManyToMany(mappedBy = "userIngredients")
    @JsonIgnore
    private Set<User> userIngredients = new HashSet<>();

    @ManyToMany(mappedBy = "shoppingList")
    @JsonIgnore
    private Set<User> shoppingList = new HashSet<>();

    @ManyToMany(mappedBy = "purchasedIngredients")
    @JsonIgnore
    private Set<User> purchasedIngredients = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Ingredient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Ingredient unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

//    public Map<Recipe> getRecipes() {
//        return recipes;
//    }
//
//    public void setRecipe(Set<Recipe> recipes) {
//        this.recipes = recipes;
//    }
//
//    public Ingredient recipes(Set<Recipe> recipes) {
//        this.recipes = recipes;
//        return this;
//    }
//
//    public Ingredient addRecipe(Recipe recipe) {
//        this.recipes.add(recipe);
////        recipe.getIngredients().add(this);
//        return this;
//    }
//
//    public Ingredient removeRecipe(Recipe recipe) {
//        this.recipes.remove(recipe);
//        recipe.getIngredients().remove(this);
//        return this;
//    }
//    public Set<Recipe> getRecipes() {
//        return recipes;
//    }
//
//    public void setRecipe(Set<Recipe> recipes) {
//        this.recipes = recipes;
//    }
//
//    public Ingredient recipes(Set<Recipe> recipes) {
//        this.recipes = recipes;
//        return this;
//    }
//
//    public Ingredient addRecipe(Recipe recipe) {
//        this.recipes.add(recipe);
////        recipe.getIngredients().add(this);
//        return this;
//    }
//
//    public Ingredient removeRecipe(Recipe recipe) {
//        this.recipes.remove(recipe);
//        recipe.getIngredients().remove(this);
//        return this;
//    }

    public Ingredient addUserToShoppingList(User user) {
        shoppingList.add(user);
        return this;
    }

    public Ingredient removeUserfromPurchasedIngredients(User user) {
        purchasedIngredients.remove(user);
        user.getPurchasedIngredients().remove(this);
        return this;
    }

    public Ingredient removeUserfromShoppingList(User user) {
        shoppingList.remove(user);
        user.getShoppingList().remove(this);
        return this;
    }

    public Ingredient removeUserfromUserIngredients(User user) {
        userIngredients.remove(user);
        user.getUserIngredients().remove(this);
        return this;
    }

    public Ingredient addUserToPurchasedIngredients(User user) {
        purchasedIngredients.add(user);
        return this;
    }

    public Ingredient addUserToUserIngredients(User user) {
        userIngredients.add(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingredient ingredient = (Ingredient) o;
        if (ingredient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ingredient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
