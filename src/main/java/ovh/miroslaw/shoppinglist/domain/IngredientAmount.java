package ovh.miroslaw.shoppinglist.domain;

import ovh.miroslaw.shoppinglist.domain.enumeration.UnitOfMeasure;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Positive;

/**
 * Entity class for IngredientAmount.
 */
@Entity
public class IngredientAmount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Amount should have positive value")
    private Float amount;

    @OneToMany(mappedBy = "ingredientAmount")
    private Set<Ingredient> ingredient;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unit;

    public IngredientAmount() {
    }

    public IngredientAmount(float amount, UnitOfMeasure unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public UnitOfMeasure getUnit() {
        return unit;
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientAmount)) {
            return false;
        }
        IngredientAmount that = (IngredientAmount) o;
        return Objects.equals(id, that.id) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }
}
