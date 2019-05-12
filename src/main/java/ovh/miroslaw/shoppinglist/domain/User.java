package ovh.miroslaw.shoppinglist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity class for User.
 */
@Entity
@Table(name = "user_table")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 6)
    @Column(name = "lang_key", length = 6)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @ManyToMany
    @JoinTable(name = "userIngredient_ingredient",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> userIngredients = new HashSet<>();

    public Ingredient addUserIngredient(Ingredient ingredient) {
        this.userIngredients.add(ingredient);
        ingredient.getUserIngredients().add(this);
        return ingredient;
    }

    public void removeUserIngredient(Ingredient ingredient) {
        this.userIngredients.remove(ingredient);
        ingredient.getUserIngredients().remove(this);
    }

    @ManyToMany
    @JoinTable(name = "shoppingList_ingredient",
               joinColumns = @JoinColumn(name = "shopping_ingredient_id"),
               inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> shoppingIngredients = new HashSet<>();

    public Ingredient addShoppingIngredient(Ingredient ingredient) {
        this.shoppingIngredients.add(ingredient);
        ingredient.getShoppingIngredients().add(this);
        return ingredient;
    }

    public void removeShoppingIngredient(Ingredient ingredient) {
        this.shoppingIngredients.remove(ingredient);
        ingredient.getShoppingIngredients().remove(this);
    }

    @ManyToMany
    @JoinTable(name = "shopping_purchased_ingredient",
               joinColumns = @JoinColumn(name = "shopping_purchased_id"),
               inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> shoppingPurchasedIngredients = new HashSet<>();

    public void addShoppingPurchasedIngredient(Ingredient ingredient) {
        this.shoppingPurchasedIngredients.add(ingredient);
        ingredient.getShoppingPurchasedIngredients().add(this);
    }

    public void removeShoppingPurchasedIngredient(Ingredient ingredient) {
        this.shoppingPurchasedIngredients.remove(ingredient);
        ingredient.getShoppingPurchasedIngredients().remove(this);
    }

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_recipes",
               joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "recipes_id", referencedColumnName = "id"))
    private Set<Recipe> recipes = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login.toLowerCase();
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Ingredient> getShoppingIngredients() {
        return shoppingIngredients;
    }

    public void setShoppingIngredients(Set<Ingredient> ingredients) {
        this.shoppingIngredients = ingredients;
    }

    public User shoppingList(Set<Ingredient> ingredients) {
        this.shoppingIngredients = ingredients;
        return this;
    }

    public Set<Ingredient> getUserIngredients() {
        return userIngredients;
    }

    public User userIngredients(Set<Ingredient> userIngredients) {
        this.userIngredients = userIngredients;
        return this;
    }

    public Set<Ingredient> getShoppingPurchasedIngredients() {
        return shoppingPurchasedIngredients;
    }

    public void setShoppingPurchasedIngredients(Set<Ingredient> purchasedIngredients) {
        this.shoppingPurchasedIngredients = purchasedIngredients;
    }

    public User addPurchasedIngredient(Ingredient ingredient) {
        shoppingPurchasedIngredients.add(ingredient);
        return this;
    }

    public User removePurchasedIngredient(Ingredient ingredient) {
        shoppingPurchasedIngredients.remove(ingredient);
        return this;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public User recipes(Set<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }

    public User addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        return this;
    }

    public User removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
        recipe.getUsers().remove(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return activated == user.activated && Objects.equals(id, user.id) && Objects.equals(login,
            user.login) && Objects.equals(password, user.password) && Objects.equals(email,
            user.email) && Objects.equals(langKey, user.langKey) && Objects.equals(activationKey,
            user.activationKey) && Objects.equals(resetKey, user.resetKey) && Objects.equals(
            resetDate, user.resetDate) && Objects.equals(createdDate, user.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, activated, langKey, activationKey, resetKey, resetDate,
            createdDate);
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", email='" + email + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
