package ovh.miroslaw.shoppinglist.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceTest {

    @InjectMocks
    private UnitOfMeasureServiceImpl unitOfMeasureService;

    @Test
    void findAllUnits_shouldMatch() {
        final List<String> expected = List.of("cup", "teaspoon", "pinch", "piece", "liter", "ml", "kg", "grams",
            "tablespoon");

        final List<String> provided = unitOfMeasureService.findAll();

        assertThat(provided).hasSameElementsAs(expected);
    }
}

