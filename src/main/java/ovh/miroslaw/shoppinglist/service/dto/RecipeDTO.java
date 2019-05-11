package ovh.miroslaw.shoppinglist.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.enumeration.Difficulty;

/**
 * A DTO for the Recipe entity.
 */
public class RecipeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String title;

    private String description;

    private String imgUrl;

    private Boolean visible;

    private Difficulty difficulty;


    private Map<Float, Ingredient> ingredients = new HashMap<>();
//    private Set<IngredientDTO> ingredients = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

//    public Set<IngredientDTO> getIngredients() {
//        return ingredients;
//    }
//    public void setIngredients(Set<IngredientDTO> ingredients) {
//        this.ingredients = ingredients;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RecipeDTO recipeDTO = (RecipeDTO) o;
        if (recipeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recipeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecipeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", visible='" + isVisible() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            "}";
    }
}
