package ovh.miroslaw.shoppinglist.rest.ingredient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ovh.miroslaw.shoppinglist.builders.TestUtils;
import ovh.miroslaw.shoppinglist.service.UserIngredientService;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.createIngredientDtoList;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientDTOWithNameAndAmount;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientIdDTO;
import static ovh.miroslaw.shoppinglist.builders.IngredientTestBuilder.ingredientUnitDTO;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@WithMockUser
@RunWith(SpringRunner.class)
@WebMvcTest(UserIngredientResource.class)
@ContextConfiguration(classes = UserIngredientResource.class)
class UserIngredientResourceTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    UserIngredientService service;
    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void after() {
        reset(service);
    }

    @Test
    void createUserIngredient_shouldReturn201() throws Exception {
        final IngredientDTO expected = make(
            ingredientDTOWithNameAndAmount("milk", 3F).but(with(ingredientUnitDTO, "liter")));

        when(service.addIngredient(any(IngredientDTO.class))).thenReturn(expected);

        MvcResult result = mvc.perform(post(API_VERSION + "/user-ingredients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(expected)))
            .andExpect(status().isCreated())
            .andReturn();
        IngredientDTO ingDTO = TestUtils.mvcResultToObject(result, IngredientDTO.class);

        assertThat(ingDTO).isEqualTo(expected);
    }

    @Test
    void findUserIngredients_shouldReturn200() throws Exception {
        final List<IngredientDTO> expected = createIngredientDtoList(5);
        when(service.findUserIngredients()).thenReturn(expected);

        MvcResult result = mvc.perform(get(API_VERSION + "/user-ingredients"))
            .andExpect(status().isOk())
            .andReturn();

        List<IngredientDTO> ingDTO = TestUtils.mvcResultToList(result, IngredientDTO.class);

        assertThat(ingDTO.get(1).getName()).isEqualTo(expected.get(1).getName());
        assertThat(ingDTO).hasSize(5).hasSameElementsAs(expected);
        verify(service, times(1)).findUserIngredients();

    }

    @Test
    void findUserIngredients_shouldReturn401() throws Exception {
        mvc.perform(delete(API_VERSION + "/user-ingredients")
                .with(csrf())
                .with(anonymous())
            )
            .andExpect(status().isUnauthorized());
        verify(service, times(0)).findUserIngredients();
    }

    @Test
    void updateIngredient_shouldReturn200() throws Exception {
        final IngredientDTO expected = make(
            ingredientDTOWithNameAndAmount("milk", 3F).but(with(ingredientIdDTO, 1L)));

        when(service.editIngredient(any(IngredientDTO.class))).thenReturn(expected);

        MvcResult result = mvc.perform(put(API_VERSION + "/user-ingredients/{id}", 1L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(expected)))
            .andExpect(status().isOk())
            .andReturn();
        IngredientDTO ingDTO = TestUtils.mvcResultToObject(result, IngredientDTO.class);

        assertThat(ingDTO).isEqualTo(expected);
    }

    @Test
    void addIngredientToShoppingList_shouldReturn201() throws Exception {
        doNothing().when(service).addIngredientToShoppingList(anyLong());

        mvc.perform(post(API_VERSION + "/user-ingredients/{id}", 1L)
                .with(csrf()))
            .andExpect(status().isCreated());

        verify(service, times(1)).addIngredientToShoppingList(anyLong());
    }

    @Test
    void deleteIngredient_shouldReturn204() throws Exception {
        mvc.perform(delete(API_VERSION + "/user-ingredients/{id}", 1L)
                .with(csrf())
            )
            .andExpect(status().isNoContent());

        verify(service, times(1)).deleteIngredient(1L);
    }

    @Test
    void deleteIngredient_shouldReturn401() throws Exception {
        mvc.perform(delete(API_VERSION + "/user-ingredients/{id}", 1L)
                .with(csrf())
                .with(anonymous())
            )
            .andExpect(status().isUnauthorized());
        verify(service, times(0)).deleteIngredient(anyLong());
    }

}
