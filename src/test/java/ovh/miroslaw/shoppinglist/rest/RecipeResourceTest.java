package ovh.miroslaw.shoppinglist.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ovh.miroslaw.shoppinglist.builders.TestUtils;
import ovh.miroslaw.shoppinglist.service.RecipeService;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;

import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ovh.miroslaw.shoppinglist.builders.RecipeTestBuilder.recipeDTOWithTitleAndVisibility;
import static ovh.miroslaw.shoppinglist.builders.RecipeTestBuilder.recipeIdDTO;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

@WithMockUser
@WebMvcTest(RecipeResource.class)
@ContextConfiguration(classes = RecipeResource.class)
class RecipeResourceTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    RecipeService service;
    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void after() {
        reset(service);
    }

    @Test
    void getUserRecipeRecommendation_shouldReturn200() throws Exception {
        mvc.perform(get(API_VERSION + "/recipes?recommendation=true"))
            .andExpect(status().isOk());

        verify(service, times(1)).findRecommendations(any(Pageable.class), eq(false));
    }

    @Test
    void getUserRecipe_shouldReturn200() throws Exception {
        mvc.perform(get(API_VERSION + "/recipes?recommendation=false"))
            .andExpect(status().isOk());

        verify(service, times(1)).findUserRecipes(any(Pageable.class), any(String.class));
    }

    @Test
    void getPublicRecipeRecommendation_shouldReturn200() throws Exception {
        mvc.perform(get(API_VERSION + "/recipes/public?recommendation=true"))
            .andExpect(status().isOk());

        verify(service, times(1)).findRecommendations(any(Pageable.class), eq(true));
    }

    @Test
    void getPublicRecipe_shouldReturn200() throws Exception {
        mvc.perform(get(API_VERSION + "/recipes/public?recommendation=false"))
            .andExpect(status().isOk());

        verify(service, times(1)).findVisible(any(Pageable.class), any(String.class));
    }

    @Test
    void createRecipe_shouldReturn201() throws Exception {
        final RecipeDTO expected = make(recipeDTOWithTitleAndVisibility("apple pie", true));
        when(service.save(any(RecipeDTO.class))).thenReturn(expected);

        final MvcResult mvcResult = mvc.perform(post(API_VERSION + "/recipes")
                .with(csrf())
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(expected)))
            .andExpect(status().isCreated())
            .andReturn();

        final RecipeDTO recipeDTO = TestUtils.mvcResultToObject(mvcResult, RecipeDTO.class);

        assertThat(recipeDTO).isEqualTo(expected);

    }

    @Test
    void updateRecipe_shouldReturn200() throws Exception {
        final RecipeDTO expected = make(
            recipeDTOWithTitleAndVisibility("apple pie", true).but(with(recipeIdDTO, 1L)));
        when(service.save(any(RecipeDTO.class))).thenReturn(expected);

        final MvcResult mvcResult = mvc.perform(put(API_VERSION + "/recipes")
                .with(csrf())
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(expected)))
            .andExpect(status().isOk())
            .andReturn();

        final RecipeDTO recipeDTO = TestUtils.mvcResultToObject(mvcResult, RecipeDTO.class);

        assertThat(recipeDTO).isEqualTo(expected);

    }

    @Test
    void updateRecipe_shouldReturn400() throws Exception {
        final RecipeDTO expected = make(recipeDTOWithTitleAndVisibility("apple pie", true));
        expected.setId(null);

        when(service.save(any(RecipeDTO.class))).thenReturn(expected);

        final MvcResult mvcResult = mvc.perform(put(API_VERSION + "/recipes")
                .with(csrf())
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(expected)))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    void deleteRecipe_shouldReturn204() throws Exception {
        doNothing().when(service).delete(any(Long.class));
        mvc.perform(delete(API_VERSION + "/recipes/{id}", 1L)
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    void assignRecipeToUser_shouldReturn201() throws Exception {
        final RecipeDTO expected = make(recipeDTOWithTitleAndVisibility("apple pie", true));
        when(service.assignRecipeToUser(expected.getId())).thenReturn(expected);

        final MvcResult mvcResult = mvc.perform(post(API_VERSION + "/recipes/{id}", expected.getId())
                .with(csrf()))
            .andExpect(status().isCreated())
            .andReturn();

        final RecipeDTO recipeDTO = TestUtils.mvcResultToObject(mvcResult, RecipeDTO.class);

        assertThat(recipeDTO).isEqualTo(expected);
    }

    @Test
    void addIngredientToShoppingList_shouldReturn201() throws Exception {
        final RecipeDTO expected = make(recipeDTOWithTitleAndVisibility("apple pie", true));
        doNothing().when(service).addIngredientsToShoppingList(any(RecipeDTO.class));

        final MvcResult mvcResult = mvc.perform(post(API_VERSION + "/recipes:move", expected)
                .with(csrf())
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(expected)))
            .andExpect(status().isCreated())
            .andReturn();

        verify(service, times(1)).addIngredientsToShoppingList(any(RecipeDTO.class));
    }
}
