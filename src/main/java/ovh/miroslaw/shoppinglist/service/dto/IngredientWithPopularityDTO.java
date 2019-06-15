package ovh.miroslaw.shoppinglist.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Ingredient entity.
 */
public class IngredientWithPopularityDTO extends IngredientDTO implements Serializable {

    private Integer popularity;

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IngredientWithPopularityDTO ingredientDTO = (IngredientWithPopularityDTO) o;
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
            ", popularity=" + popularity +
            '}';
    }
}
