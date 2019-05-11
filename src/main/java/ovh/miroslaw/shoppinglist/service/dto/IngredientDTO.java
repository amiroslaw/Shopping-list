package ovh.miroslaw.shoppinglist.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Ingredient entity.
 */
public class IngredientDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;


    private Long unitOfMeasureId;

    private Long shoppingListId;

    private Long ingredientsId;

    private Long purchasedIngredientsId;

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

    public Long getUnitOfMeasureId() {
        return unitOfMeasureId;
    }

    public void setUnitOfMeasureId(Long unitOfMeasureId) {
        this.unitOfMeasureId = unitOfMeasureId;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public Long getIngredientsId() {
        return ingredientsId;
    }

    public void setIngredientsId(Long ingredientsId) {
        this.ingredientsId = ingredientsId;
    }

    public Long getPurchasedIngredientsId() {
        return purchasedIngredientsId;
    }

    public void setPurchasedIngredientsId(Long purchasedIngredientsId) {
        this.purchasedIngredientsId = purchasedIngredientsId;
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
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unitOfMeasure=" + getUnitOfMeasureId() +
            ", shoppingList=" + getShoppingListId() +
            ", ingredients=" + getIngredientsId() +
            ", purchasedIngredients=" + getPurchasedIngredientsId() +
            "}";
    }
}
