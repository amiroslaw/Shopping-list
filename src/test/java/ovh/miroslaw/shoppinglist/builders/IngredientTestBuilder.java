package ovh.miroslaw.shoppinglist.builders;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Maker;
import com.natpryce.makeiteasy.Property;
import net.datafaker.Faker;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.IngredientAmount;
import ovh.miroslaw.shoppinglist.domain.IngredientName;
import ovh.miroslaw.shoppinglist.domain.enumeration.UnitOfMeasure;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.natpryce.makeiteasy.Property.newProperty;

public class IngredientTestBuilder {

    static Faker faker = new Faker();

    public static final Property<Ingredient, Long> ingredientId = newProperty();
    public static final Property<Ingredient, IngredientName> ingredientName = newProperty();
    public static final Property<Ingredient, IngredientAmount> ingredientAmount = newProperty();
    //    private static final Property<Ingredient,> ingredient = newProperty();
    public static Instantiator<Ingredient> IngredientBuilder = lookup -> {
        var ingredient = new Ingredient();
        ingredient.setId(lookup.valueOf(ingredientId, faker.number().numberBetween(1L, 10_000)));
        ingredient.setIngredientName(lookup.valueOf(ingredientName,
            new IngredientName(faker.food().ingredient(), faker.number().numberBetween(1, 999))));
        ingredient.setIngredientAmount(lookup.valueOf(ingredientAmount,
            new IngredientAmount((float) faker.number().randomDouble(1, 1, 40), UnitOfMeasure.PIECE)));
        return ingredient;
    };

    public static Maker<Ingredient> ingredientWithIdAndName(Long id, String name) {
        return a(IngredientBuilder)
            .but(with(ingredientId, id))
            .but(with(ingredientName, new IngredientName(name)));
    }

    public static Maker<Ingredient> ingredientWithNameAmountAndUOM(String name, float amount, UnitOfMeasure uom) {
        return a(IngredientBuilder)
            .but(with(ingredientName, new IngredientName(name)))
            .but(with(ingredientAmount, new IngredientAmount(amount, uom)));
    }

    public static final Property<IngredientDTO, Long> ingredientIdDTO = newProperty();
    public static final Property<IngredientDTO, String> ingredientNameDTO = newProperty();
    public static final Property<IngredientDTO, Integer> ingredientPopularityDTO = newProperty();
    public static final Property<IngredientDTO, Float> ingredientAmountDTO = newProperty();
    public static final Property<IngredientDTO, String> ingredientUnitDTO = newProperty();

    public static Instantiator<IngredientDTO> IngredientDTOBuilder = lookup -> {
        var ingredient = new IngredientDTO();
        ingredient.setId(lookup.valueOf(ingredientIdDTO, faker.number().numberBetween(1L, 10_000)));
        ingredient.setName(lookup.valueOf(ingredientNameDTO, faker.food().ingredient()));
        ingredient.setPopularity(lookup.valueOf(ingredientPopularityDTO, 1));
        ingredient.setAmount(lookup.valueOf(ingredientAmountDTO, (float) faker.number().randomDouble(1, 1, 40)));
        ingredient.setUnit(lookup.valueOf(ingredientUnitDTO, UnitOfMeasure.PIECE.toString()));
        return ingredient;
    };

    public static Maker<IngredientDTO> ingredientDTOWithNameAndAmount(String name, float amount) {
        return a(IngredientDTOBuilder)
            .but(with(ingredientNameDTO, name))
            .but(with(ingredientAmountDTO, amount));
    }

    private static <T> List<T> createList(int size, IntFunction<T> ingFunction) {
        return IntStream.rangeClosed(1, size)
            .mapToObj(ingFunction)
            .collect(Collectors.toList());
    }

    public static List<Ingredient> createIngredientList(int size) {
        final IntFunction<Ingredient> ingredientIntFunction = e -> make(a(IngredientBuilder));
        return createList(size, ingredientIntFunction);
    }

    public static List<IngredientDTO> createIngredientDtoList(int size) {
        final IntFunction<IngredientDTO> ingredientDTOIntFunction = e -> make(a(IngredientDTOBuilder));
        return createList(size, ingredientDTOIntFunction);

    }


}
