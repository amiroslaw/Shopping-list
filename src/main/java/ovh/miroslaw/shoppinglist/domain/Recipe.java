package ovh.miroslaw.shoppinglist.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.*;

import ovh.miroslaw.shoppinglist.domain.enumeration.Difficulty;

@Entity
@Table(name = "recipe")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "visible")
    private Boolean visible;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(name = "recipe_ingredients",
        joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "ingredients_id", referencedColumnName = "id"))
//    @ManyToMany(mappedBy = "recipes")
//    @JsonIgnore
    @MapKey(name = "amount")
    private Map<Float, Ingredient> ingredients = new HashMap<>();
    //    private Map<Ingredient, Float> ingredients = new HashMap<>();
    //    private Set<Ingredient> ingredients = new HashSet<>();
//    @Column(name = "amount")
//    private Float amount;
    @ManyToMany(mappedBy = "recipes")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Recipe title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Recipe description(String description) {
        this.description = description;
        return this;
    }

//    public Float getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Float amount) {
//        this.amount = amount;
//    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Recipe imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean isVisible() {
        return visible;
    }

    public Recipe visible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Recipe difficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Map<Float, Ingredient> getIngredients() {
        return ingredients;
    }
//
//    public Recipe ingredients(Map<Ingredient, Float> ingredients) {
//        this.ingredients = ingredients;
//        return this;
//    }
//
//    public void setIngredients(Map<Ingredient, Float> ingredients) {
//        this.ingredients = ingredients;
//    }

//    public Recipe addIngredients(Ingredient ingredient, Float amount) {
//        this.ingredients.put(ingredient, amount);
//        ingredient.getRecipes().add(this);
//        return this;
//    }
//
//    public Recipe removeIngredients(Ingredient ingredient) {
//        this.ingredients.remove(ingredient);
//        ingredient.getRecipes().remove(this);
//        return this;
//    }
//
//    public Set<Ingredient> getIngredients() {
//        return ingredients;
//    }
//
//    public Recipe ingredients(Set<Ingredient> ingredients) {
//        this.ingredients = ingredients;
//        return this;
//    }
//
//    public Recipe addIngredients(Ingredient ingredient) {
//        this.ingredients.add(ingredient);
//        ingredient.getRecipes().add(this);
//        return this;
//    }
//
//    public Recipe removeIngredients(Ingredient ingredient) {
//        this.ingredients.remove(ingredient);
//        ingredient.getRecipes().remove(this);
//        return this;
//    }

//    public void setIngredients(Set<Ingredient> ingredients) {
//        this.ingredients = ingredients;
//    }

    public Set<User> getUsers() {
        return users;
    }

    public Recipe users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Recipe addUsers(User uUser) {
        this.users.add(uUser);
        uUser.getRecipes().add(this);
        return this;
    }

//    public Recipe removeUsers(User uUser) {
//        this.users.remove(uUser);
//        uUser.getRecipes().remove(this);
//        return this;
//    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        if (recipe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recipe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", visible='" + isVisible() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            "}";
    }
}
