package ovh.miroslaw.shoppinglist.service.dto;

import org.apache.commons.lang3.EnumUtils;
import ovh.miroslaw.shoppinglist.domain.enumeration.UnitOfMeasure;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * A DTO for the Ingredient entity.
 */
public class IngredientDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer popularity;

    @Positive(message = "Amount should have positive value")
    private Float amount;
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {

        if (EnumUtils.isValidEnumIgnoreCase(UnitOfMeasure.class, unit)) {
            this.unit = unit.toUpperCase();
        } else {
            this.unit = UnitOfMeasure.PIECE.toString();
        }
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IngredientDTO ingredientDTO = (IngredientDTO) o;
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
               "id=" + id +
               ", name='" + name + '\'' +
               ", popularity=" + popularity +
               ", amount=" + amount +
               ", unit='" + unit + '\'' +
               '}';
    }
}
