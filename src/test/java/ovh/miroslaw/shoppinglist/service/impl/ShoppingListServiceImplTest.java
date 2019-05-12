package ovh.miroslaw.shoppinglist.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.UserService;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.HashSet;
import java.util.Optional;

import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.createIngredientList;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientWithIdAndName;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ShoppingListServiceImplTest {

    public static final String EGG = "egg";

    @Mock
    private UserService userService;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private IngredientService ingredientService;
    @Spy
    @InjectMocks
    private IngredientMapper ingredientMapper = Mappers.getMapper(IngredientMapper.class);
    @InjectMocks
    private ShoppingListServiceImpl service;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        final HashSet<Ingredient> ingredients = new HashSet<>(createIngredientList(3));
        final Ingredient egg = make(ingredientWithIdAndName(3L, EGG));
        ingredients.add(egg);
        user.setShoppingIngredients(ingredients);

        when(ingredientRepository.findById(3L)).thenReturn(Optional.of(egg));
        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    void deleteIngredient_shouldDelete() {
        service.deleteIngredient(3L);

        assertThat(user.getShoppingIngredients()).hasSize(3).extracting(Ingredient::getId).doesNotContain(3L);

        verify(ingredientRepository).findById(3L);
        verify(userService).save(any(User.class));
    }

    @Test
    void deleteAllIngredients_shouldDelete() {
        service.deleteAllIngredients();

        assertThat(user.getShoppingIngredients()).isEmpty();
    }

    @Disabled
    @Test
    void purchasedIngredient_shouldAddIngredientToPurchasedList() {
        assertThat(user.getShoppingIngredients()).hasSize(4);
        final Ingredient milk = make(ingredientWithIdAndName(4L, "Milk"));
        user.getShoppingIngredients().add(milk);
        when(ingredientRepository.findById(4L)).thenReturn(Optional.of(milk));

        service.purchasedIngredient(4L);

        assertThat(user.getShoppingPurchasedIngredients()).hasSize(1).isEqualTo(milk);
    }

    @Test
    void purchasedIngredient_shouldNotFind() {
        assertThrows(NotFoundException.class, () -> service.purchasedIngredient(8L));
    }

}
