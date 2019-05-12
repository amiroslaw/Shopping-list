package ovh.miroslaw.shoppinglist.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.rest.errors.NotFoundException;
import ovh.miroslaw.shoppinglist.service.UserService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.HashSet;

import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.createIngredientList;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientWithIdAndName;

@ExtendWith(MockitoExtension.class)
class PurchasedIngredientServiceImplTest {

    public static final String EGG = "egg";

    @Mock
    private UserService userService;
    @Mock
    private IngredientRepository ingredientRepository;

    @Spy
    @InjectMocks
    private IngredientMapper ingredientMapper = Mappers.getMapper(IngredientMapper.class);

    @InjectMocks
    private PurchasedIngredientServiceImpl service;

    private User user;

    @BeforeEach
    void before() {
        user = new User();
        final HashSet<Ingredient> purchasedIngredients = new HashSet<>(createIngredientList(3));
        user.setShoppingPurchasedIngredients(purchasedIngredients);
        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    void addIngredientToShoppingList_shouldAdd() {
        final Ingredient expected = make(ingredientWithIdAndName(5L, EGG));
        user.getShoppingPurchasedIngredients().add(expected);

        when(ingredientRepository.findById(5L)).thenReturn(java.util.Optional.of(expected));

        final IngredientDTO ingredientDTO = service.addIngredientToShoppingList(5L);

        assertThat(ingredientDTO).extracting(IngredientDTO::getId, IngredientDTO::getName, IngredientDTO::getUnit)
            .containsExactly(5L, EGG, "PIECE");
    }

    @Test
    void deleteIngredient_shouldDelete() {
        final Ingredient expected = make(ingredientWithIdAndName(4L, "milk"));
        user.getShoppingPurchasedIngredients().add(expected);

        service.deleteIngredient(4L);

        assertThat(user.getShoppingPurchasedIngredients()).hasSize(3).extracting(Ingredient::getId).doesNotContain(4L);
    }

    @Test
    void deleteIngredient_shouldThrowException() {
        when(userService.getCurrentUser()).thenReturn(new User());

        assertThrows(NotFoundException.class, () -> service.deleteIngredient(433L));
    }
}
