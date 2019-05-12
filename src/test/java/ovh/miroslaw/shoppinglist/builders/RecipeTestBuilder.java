package ovh.miroslaw.shoppinglist.builders;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Maker;
import com.natpryce.makeiteasy.Property;
import net.datafaker.Faker;
import ovh.miroslaw.shoppinglist.domain.Recipe;
import ovh.miroslaw.shoppinglist.domain.enumeration.Difficulty;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.natpryce.makeiteasy.Property.newProperty;

public class RecipeTestBuilder {

    static Faker faker = new Faker();

    public static final Property<Recipe, Long> recipeId = newProperty();
    public static final Property<Recipe, String> recipeTitle = newProperty();
    public static final Property<Recipe, String> recipeDescription = newProperty();
    public static final Property<Recipe, String> recipeImgUrl = newProperty();
    public static final Property<Recipe, Boolean> recipeVisible = newProperty();
    public static final Property<Recipe, Difficulty> recipeDifficulty = newProperty();

    public static Instantiator<Recipe> RecipeBuilder = lookup -> {
        var Recipe = new Recipe();
        Recipe.setId(lookup.valueOf(recipeId, faker.number().numberBetween(1L, 10_000)));
        Recipe.setTitle(lookup.valueOf(recipeTitle, faker.food().dish()));
        Recipe.setDescription(lookup.valueOf(recipeDescription, faker.lorem().paragraph()));
        Recipe.setImgUrl(lookup.valueOf(recipeImgUrl, faker.internet().url()));
        Recipe.setVisible(lookup.valueOf(recipeVisible, true));
        Recipe.setDifficulty(lookup.valueOf(recipeDifficulty, Difficulty.EASY));
        return Recipe;
    };

    public static Maker<Recipe> recipeWithTitleAndVisibility(String title, Boolean visibility) {
        return a(RecipeBuilder)
            .but(with(recipeTitle, title))
            .but(with(recipeVisible, visibility));
    }

    public static final Property<RecipeDTO, Long> recipeIdDTO = newProperty();
    public static final Property<RecipeDTO, String> recipeTitleDTO = newProperty();
    public static final Property<RecipeDTO, String> recipeDescriptionDTO = newProperty();
    public static final Property<RecipeDTO, String> recipeImgUrlDTO = newProperty();
    public static final Property<RecipeDTO, Boolean> recipeVisibleDTO = newProperty();
    public static final Property<RecipeDTO, Difficulty> recipeDifficultyDTO = newProperty();

    public static Instantiator<RecipeDTO> RecipeDTOBuilder = lookup -> {
        var Recipe = new RecipeDTO();
        Recipe.setId(lookup.valueOf(recipeIdDTO, faker.number().numberBetween(1L, 10_000)));
        Recipe.setTitle(lookup.valueOf(recipeTitleDTO, faker.food().dish()));
        Recipe.setDescription(lookup.valueOf(recipeDescriptionDTO, faker.lorem().paragraph()));
        Recipe.setImgUrl(lookup.valueOf(recipeImgUrlDTO, faker.internet().url()));
        Recipe.setVisible(lookup.valueOf(recipeVisibleDTO, true));
        Recipe.setDifficulty(lookup.valueOf(recipeDifficultyDTO, Difficulty.EASY));
        return Recipe;
    };

    public static Maker<RecipeDTO> recipeDTOWithTitleAndVisibility(String title, Boolean visibility) {
        return a(RecipeDTOBuilder)
            .but(with(recipeTitleDTO, title))
            .but(with(recipeVisibleDTO, visibility));
    }
    public static Maker<RecipeDTO> recipeDTO(Long id, String title, String description, String url, Boolean visibility) {
        return a(RecipeDTOBuilder)
            .but(with(recipeIdDTO, id))
            .but(with(recipeTitleDTO, title))
            .but(with(recipeDescriptionDTO, description))
            .but(with(recipeImgUrlDTO, url))
            .but(with(recipeVisibleDTO, visibility));
    }

}
