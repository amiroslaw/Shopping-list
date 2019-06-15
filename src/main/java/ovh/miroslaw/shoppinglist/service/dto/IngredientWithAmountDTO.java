package ovh.miroslaw.shoppinglist.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Ingredient entity.
 */
public class IngredientWithAmountDTO extends IngredientDTO implements Serializable {

    private Float amount;

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IngredientWithAmountDTO ingredientDTO = (IngredientWithAmountDTO) o;
        if (ingredientDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ingredientDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IngredientDTO{" +
            "id=" + super.getId() +
            ", name='" + super.getName() + '\'' +
            ", amount=" + amount +
            '}';
    }
}
