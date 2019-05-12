package ovh.miroslaw.shoppinglist.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.builders.TestUtils;
import ovh.miroslaw.shoppinglist.domain.Authority;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.security.AuthoritiesConstants;
import ovh.miroslaw.shoppinglist.service.dto.UserDTO;
import ovh.miroslaw.shoppinglist.service.mapper.UserMapper;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@SpringBootTest
class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_PASSWORD = "passjohndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_LANGKEY = "en";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it, if they test an entity which has a
     * required relationship to the User entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    /**
     * Setups the database with one user.
     */
    public static User initTestUser(UserRepository userRepository, EntityManager em) {
        User user = createEntity(em);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        return user;
    }

    @BeforeEach
    public void initTest() {
        user = initTestUser(userRepository, em);
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        User User = new User();
        User.setLogin(DEFAULT_LOGIN);
        User.setPassword(DEFAULT_PASSWORD);
        User.setEmail(DEFAULT_EMAIL);
        User.setActivated(true);
        User.setLangKey(DEFAULT_LANGKEY);
        User.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        restUserMockMvc
            .perform(
                post(API_VERSION + "/users").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(User))
            )
            .andExpect(status().isCreated());

        // Validate the User in the database
        assertPersistedUsers(
            users -> {
                assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
                User testUser = users.get(users.size() - 1);
                assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
                assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
                assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
            }
        );
    }

    @Test
    @Transactional
    void createUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        User User = new User();
        User.setId(DEFAULT_ID);
        User.setLogin(DEFAULT_LOGIN);
        User.setPassword(DEFAULT_PASSWORD);
        User.setEmail(DEFAULT_EMAIL);
        User.setActivated(true);
        User.setLangKey(DEFAULT_LANGKEY);
        User.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc
            .perform(
                post(API_VERSION + "/users").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(User))
            )
            .andExpect(status().isNotFound());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        User User = new User();
        User.setLogin(DEFAULT_LOGIN); // this login should already be used
        User.setPassword(DEFAULT_PASSWORD);
        User.setEmail("anothermail@localhost");
        User.setActivated(true);
        User.setLangKey(DEFAULT_LANGKEY);
        User.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        // Create the User
        restUserMockMvc
            .perform(
                post(API_VERSION + "/users").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(User))
            )
            .andExpect(status().isNotFound());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        User User = new User();
        User.setLogin("anotherlogin");
        User.setPassword(DEFAULT_PASSWORD);
        User.setEmail(DEFAULT_EMAIL); // this email should already be used
        User.setActivated(true);
        User.setLangKey(DEFAULT_LANGKEY);
        User.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        // Create the User
        restUserMockMvc
            .perform(
                post(API_VERSION + "/users").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(User))
            )
            .andExpect(status().isNotFound());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Disabled
    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get all the users
        final ResultActions result = restUserMockMvc
            .perform(get(API_VERSION + "/users?sort=id,desc").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get the user
        restUserMockMvc
            .perform(get(API_VERSION + "/users/{login}", user.getLogin()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANGKEY));
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get(API_VERSION + "/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateUserExistingEmail() throws Exception {
        // Initialize the database with 2 users
        userRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        User User = new User();
        User.setId(updatedUser.getId());
        User.setLogin(updatedUser.getLogin());
        User.setPassword(updatedUser.getPassword());
        User.setEmail("jhipster@localhost"); // this email should already be used by anotherUser
        User.setLangKey(updatedUser.getLangKey());
        User.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        restUserMockMvc
            .perform(
                put(API_VERSION + "/users").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(User))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void updateUserExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        User user = new User();
        user.setId(updatedUser.getId());
        user.setLogin("jhipster"); // this login should already be used by anotherUser
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());
        user.setLangKey(updatedUser.getLangKey());
        user.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        restUserMockMvc
            .perform(
                put(API_VERSION + "/users").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(user))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc
            .perform(delete(API_VERSION + "/users/{login}", user.getLogin()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeDelete - 1));
    }

    @Test
    void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setAuthorities(Set.of(AuthoritiesConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    void testAuthorityEquals() {
        Authority authorityA = new Authority();
        assertThat(authorityA).isNotEqualTo(null).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isZero();
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isEqualTo(authorityB).hasSameHashCodeAs(authorityB);
    }

    private void assertPersistedUsers(Consumer<List<User>> userAssertion) {
        userAssertion.accept(userRepository.findAll());
    }
}
