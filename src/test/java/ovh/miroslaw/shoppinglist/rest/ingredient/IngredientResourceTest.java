package ovh.miroslaw.shoppinglist.rest.ingredient;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ovh.miroslaw.shoppinglist.service.IngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.IngredientDTOBuilder;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientIdDTO;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@WithMockUser
@RunWith(SpringRunner.class)
@WebMvcTest(IngredientResource.class)
@ContextConfiguration(classes = IngredientResource.class)
class IngredientResourceTest extends TestCase {

    @Autowired
    private MockMvc mvc;
    @MockBean
    IngredientService ingredientService;

    @Test
    void getIngredient_shouldReturn200() throws Exception {

        final IngredientDTO ingredientDTO = make(a(IngredientDTOBuilder, with(ingredientIdDTO, 1L)));
        when(ingredientService.findOne(1L)).thenReturn(Optional.of(ingredientDTO));

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = mvc.perform(get(API_VERSION + "/ingredients/{id}", 1L))
            .andExpect(status().isOk())
            .andReturn();
        IngredientDTO ingredientResult = objectMapper.readValue(result.getResponse().getContentAsString(),
            IngredientDTO.class);

        assertThat(ingredientResult).isEqualTo(ingredientDTO);
        verify(ingredientService, times(1)).findOne(1L);
    }

    @Test
    void getAllIngredients_shouldReturn200() throws Exception {
        final String[] names = new String[]{"bread", "milk"};
        when(ingredientService.findAllNames()).thenReturn(Set.of(names));

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = mvc.perform(get(API_VERSION + "/ingredients"))
            .andExpect(status().isOk())
            .andReturn();
        String[] ingredients = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);

        assertThat(ingredients).hasSize(2).containsOnly(names);
        verify(ingredientService, times(1)).findAllNames();
    }
}
