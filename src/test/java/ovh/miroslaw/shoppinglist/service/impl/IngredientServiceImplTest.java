package ovh.miroslaw.shoppinglist.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ovh.miroslaw.shoppinglist.domain.IngredientAmount;
import ovh.miroslaw.shoppinglist.domain.IngredientName;
import ovh.miroslaw.shoppinglist.repository.IngredientAmountRepository;
import ovh.miroslaw.shoppinglist.repository.IngredientNameRepository;
import ovh.miroslaw.shoppinglist.repository.IngredientRepository;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;

import java.util.Optional;

import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ovh.miroslaw.shoppinglist.domain.enumeration.UnitOfMeasure.PIECE;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientDTOWithNameAndAmount;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientWithNameAmountAndUOM;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    public static final String EGG = "egg";
    public static final int POPULARITY = 44;
    public static final float AMOUNT = 2.0F;
    @Mock
    private IngredientAmountRepository ingredientAmountRepository;
    @Mock
    private IngredientNameRepository ingredientNameRepository;
    @Mock
    private IngredientRepository ingredientRepository;

    @Spy
    @InjectMocks
    private IngredientMapper ingredientMapper = Mappers.getMapper(IngredientMapper.class);

    @InjectMocks
    private IngredientServiceImpl service;

    @Test
    void findOne_shouldReturnEmptyOptional() {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        final Optional<IngredientDTO> ingredientDTO = service.findOne(1L);

        assertThat(ingredientDTO).isNotPresent();
        verify(ingredientRepository, times(1)).findById(anyLong());
    }

    @Test
    void findOne_shouldReturnDTO() {
        when(ingredientRepository.findById(1L)).thenReturn(
            Optional.of(make(ingredientWithNameAmountAndUOM(EGG, AMOUNT, PIECE))));

        final IngredientDTO expected = make(ingredientDTOWithNameAndAmount(EGG, AMOUNT));

        final Optional<IngredientDTO> ingredientDTO = service.findOne(1L);

        assertThat(ingredientDTO).hasValueSatisfying(a -> {
                assertThat(a.getName()).isEqualTo(expected.getName());
                assertThat(a.getAmount()).isEqualTo(expected.getAmount());
                assertThat(a.getUnit()).isEqualTo(expected.getUnit());
            }
        );
        verify(ingredientRepository, times(1)).findById(anyLong());
    }

    private void findByNameOrCreateIngredient_setup() {
        when(ingredientNameRepository.findByName(EGG)).thenReturn(
            Optional.of(new IngredientName(EGG, POPULARITY)));
        when(ingredientAmountRepository.findByAmountAndUnit(AMOUNT, PIECE)).thenReturn(
            Optional.of(new IngredientAmount(AMOUNT, PIECE)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"egg", "Egg", "EGG"})
    void findByNameOrCreateIngredient_shouldFindIngredient(String name) {
        findByNameOrCreateIngredient_setup();

        service.findByNameOrCreateIngredient(make(ingredientDTOWithNameAndAmount(name, AMOUNT)));

        ArgumentCaptor<IngredientAmount> ingredientAmountCaptor = ArgumentCaptor.forClass(IngredientAmount.class);
        ArgumentCaptor<IngredientName> ingredientNameCaptor = ArgumentCaptor.forClass(IngredientName.class);
        verify(ingredientAmountRepository, times(1)).save(ingredientAmountCaptor.capture());
        verify(ingredientNameRepository, times(1)).save(ingredientNameCaptor.capture());

        assertThat(ingredientAmountCaptor.getValue().getAmount()).isEqualTo(AMOUNT);
        assertThat(ingredientAmountCaptor.getValue().getUnit()).isEqualTo(PIECE);
        assertThat(ingredientNameCaptor.getValue().getName()).isEqualTo(EGG);
        assertThat(ingredientNameCaptor.getValue().getPopularity()).isEqualTo(POPULARITY + 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"egg", "Egg", "EGG"})
    void findByNameOrCreateIngredient_shouldCreate(String name) {
        when(ingredientNameRepository.findByName(EGG)).thenReturn(
            Optional.empty());
        when(ingredientAmountRepository.findByAmountAndUnit(AMOUNT, PIECE)).thenReturn(
            Optional.empty());

        service.findByNameOrCreateIngredient(make(ingredientDTOWithNameAndAmount(name, AMOUNT)));

        ArgumentCaptor<IngredientAmount> ingredientAmountCaptor = ArgumentCaptor.forClass(IngredientAmount.class);
        ArgumentCaptor<IngredientName> ingredientNameCaptor = ArgumentCaptor.forClass(IngredientName.class);
        verify(ingredientAmountRepository, times(1)).save(ingredientAmountCaptor.capture());
        verify(ingredientNameRepository, times(1)).save(ingredientNameCaptor.capture());

        assertThat(ingredientAmountCaptor.getValue().getAmount()).isEqualTo(AMOUNT);
        assertThat(ingredientAmountCaptor.getValue().getUnit()).isEqualTo(PIECE);
        assertThat(ingredientNameCaptor.getValue().getName()).isEqualTo(EGG);
        assertThat(ingredientNameCaptor.getValue().getPopularity()).isEqualTo(1);
    }
}
