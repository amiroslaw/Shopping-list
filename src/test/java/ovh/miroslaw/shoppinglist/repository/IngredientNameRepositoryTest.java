package ovh.miroslaw.shoppinglist.repository;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.IngredientName;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class IngredientNameRepositoryTest extends TestCase {

    @Autowired
    private IngredientNameRepository ingredientNameRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void testFindAllNames_shouldReturnAll() {
        final String[] names = {"bread", "carrots", "chicken", "egg", "noodles", "oil", "parmesan", "peanut butter"};
        final Set<String> found = ingredientNameRepository.findAllNames();
        assertThat(found).hasSize(8).containsExactlyInAnyOrder(names);
    }

    @Test
    void findById_shouldFindIngredient() {
        final Optional<Ingredient> optionalIngredient = ingredientRepository.findById(1L);

        assertThat(optionalIngredient).isPresent();
        assertThat(optionalIngredient).hasValueSatisfying(i -> {
            assertThat(i.getIngredientName().getName()).isEqualTo("bread");
            assertThat(i.getIngredientAmount().getAmount()).isEqualTo(1);
        });
    }

    @Test
    void createNotUniqueEntries_shouldThrowException() {
        final IngredientName first = new IngredientName("milk", 1);
        final IngredientName second = new IngredientName("milk", 2);

        assertThrows(DataIntegrityViolationException.class, () -> {
            ingredientNameRepository.save(first);
            ingredientNameRepository.save(second);
        });

    }
}

