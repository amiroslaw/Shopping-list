package ovh.miroslaw.shoppinglist.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity class for IngredientName
 */
@Entity
public class IngredientName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer popularity;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "ingredientName", fetch = FetchType.LAZY)
    private Set<Ingredient> ingredient;

    public IngredientName() {
    }

    public IngredientName(String name) {
        this.name = name;
    }

    public IngredientName(String name, Integer popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    public Set<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(Set<Ingredient> ingredients) {
        this.ingredient = ingredients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientName)) {
            return false;
        }
        IngredientName that = (IngredientName) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
