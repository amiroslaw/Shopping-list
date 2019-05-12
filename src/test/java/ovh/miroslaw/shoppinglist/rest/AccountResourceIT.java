package ovh.miroslaw.shoppinglist.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.builders.TestUtils;
import ovh.miroslaw.shoppinglist.config.Constants;
import ovh.miroslaw.shoppinglist.domain.Authority;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.AuthorityRepository;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.security.AuthoritiesConstants;
import ovh.miroslaw.shoppinglist.service.UserService;
import ovh.miroslaw.shoppinglist.service.dto.PasswordChangeDTO;
import ovh.miroslaw.shoppinglist.service.dto.UserDTO;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ovh.miroslaw.shoppinglist.config.Constants.API_VERSION;

/**
 * Integration tests for the AccountResource REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(value = AccountResourceIT.TEST_USER_LOGIN)
//@WithMockUser
@SpringBootTest
class AccountResourceIT {

    static final String TEST_USER_LOGIN = "hulio";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc restAccountMockMvc;

    @Test
    @WithAnonymousUser
    void testNonAuthenticatedUser() throws Exception {
        restAccountMockMvc
            .perform(get(API_VERSION + "/authenticate").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void testAuthenticatedUser() throws Exception {
        restAccountMockMvc
            .perform(
                get(API_VERSION + "/authenticate")
                    .with(
                        request -> {
                            request.setRemoteUser(TEST_USER_LOGIN);
                            return request;
                        }
                    )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(TEST_USER_LOGIN));
    }

    @Test
    void testGetExistingAccount() throws Exception {
        restAccountMockMvc
            .perform(get(API_VERSION + "/account").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.login").value(TEST_USER_LOGIN))
            .andExpect(jsonPath("$.email").value("email@gmail.com"))
            .andExpect(jsonPath("$.langKey").value("en-US"))
            .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.USER));
    }

    @Test
    @WithAnonymousUser
    void testGetUnknownAccount() throws Exception {
        restAccountMockMvc
            .perform(get(API_VERSION + "/account"))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void testRegisterInvalidLogin() throws Exception {
        User invalidUser = new User();
        invalidUser.setLogin("funky-log(n"); // <-- invalid
        invalidUser.setPassword("password");
        invalidUser.setEmail("funky@example.com");
        invalidUser.setActivated(true);
        invalidUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        invalidUser.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        restAccountMockMvc
            .perform(post(API_VERSION + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByEmailIgnoreCase("funky@example.com");
        assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterInvalidEmail() throws Exception {
        User invalidUser = new User();
        invalidUser.setLogin("bob");
        invalidUser.setPassword("password");
        invalidUser.setEmail("invalid"); // <-- invalid
        invalidUser.setActivated(true);
        invalidUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        invalidUser.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        restAccountMockMvc
            .perform(post(API_VERSION + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByLogin("bob");
        assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterInvalidPassword() throws Exception {
        User invalidUser = new User();
        invalidUser.setLogin("bob");
        invalidUser.setPassword("123"); // password with only 3 digits
        invalidUser.setEmail("bob@example.com");
        invalidUser.setActivated(true);
        invalidUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        invalidUser.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        restAccountMockMvc
            .perform(post(API_VERSION + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByLogin("bob");
        assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterNullPassword() throws Exception {
        User invalidUser = new User();
        invalidUser.setLogin("bob");
        invalidUser.setPassword(null); // invalid null password
        invalidUser.setEmail("bob@example.com");
        invalidUser.setActivated(true);
        invalidUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        invalidUser.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));

        restAccountMockMvc
            .perform(post(API_VERSION + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByLogin("bob");
        assertThat(user).isEmpty();
    }


    @Test
    @Transactional
    @WithMockUser("save-account")
    void testSaveAccount() throws Exception {
        User user = new User();
        user.setLogin("save-account");
        user.setEmail("save-account@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        userRepository.saveAndFlush(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("not-used");
        userDTO.setEmail("save-account@example.com");
        userDTO.setActivated(false);
        userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.ADMIN));

        restAccountMockMvc
            .perform(post(API_VERSION + "/account").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isOk());

        User updatedUser = userRepository.findOneWithAuthoritiesByLogin(user.getLogin()).orElse(null);
        assertThat(updatedUser.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(updatedUser.getLangKey()).isEqualTo(userDTO.getLangKey());
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(updatedUser.getAuthorities()).isEmpty();
    }

    @Test
    @Transactional
    @WithMockUser("save-invalid-email")
    void testSaveInvalidEmail() throws Exception {
        User user = new User();
        user.setLogin("save-invalid-email");
        user.setEmail("save-invalid-email@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);

        userRepository.saveAndFlush(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("not-used");
        userDTO.setEmail("invalid email");
        userDTO.setActivated(false);
        userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.ADMIN));

        restAccountMockMvc
            .perform(post(API_VERSION + "/account").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertThat(userRepository.findOneByEmailIgnoreCase("invalid email")).isNotPresent();
    }

    @Test
    @Transactional
    @WithMockUser("save-existing-email")
    void testSaveExistingEmail() throws Exception {
        User user = new User();
        user.setLogin("save-existing-email");
        user.setEmail("save-existing-email@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        userRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setLogin("save-existing-email2");
        anotherUser.setEmail("save-existing-email2@example.com");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);

        userRepository.saveAndFlush(anotherUser);

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("not-used");
        userDTO.setEmail("save-existing-email2@example.com");
        userDTO.setActivated(false);
        userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.ADMIN));

        restAccountMockMvc
            .perform(post(API_VERSION + "/account").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("save-existing-email").orElse(null);
        assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");
    }

    @Test
    @Transactional
    @WithMockUser("save-existing-email-and-login")
    void testSaveExistingEmailAndLogin() throws Exception {
        User user = new User();
        user.setLogin("save-existing-email-and-login");
        user.setEmail("save-existing-email-and-login@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        userRepository.saveAndFlush(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("not-used");
        userDTO.setEmail("save-existing-email-and-login@example.com");
        userDTO.setActivated(false);
        userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.ADMIN));

        restAccountMockMvc
            .perform(post(API_VERSION + "/account").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isOk());

        User updatedUser = userRepository.findOneByLogin("save-existing-email-and-login").orElse(null);
        assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email-and-login@example.com");
    }

    @Test
    @Transactional
    @WithMockUser("change-password-wrong-existing-password")
    void testChangePasswordWrongExistingPassword() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-wrong-existing-password");
        user.setEmail("change-password-wrong-existing-password@example.com");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(
                post(API_VERSION + "/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        TestUtils.convertObjectToJsonBytes(new PasswordChangeDTO("1" + currentPassword, "new password")))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-wrong-existing-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(currentPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser("change-password")
    void testChangePassword() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password");
        user.setEmail("change-password@example.com");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(
                post(API_VERSION + "/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, "new password")))
            )
            .andExpect(status().isOk());

        User updatedUser = userRepository.findOneByLogin("change-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser("change-password-too-small")
    void testChangePasswordTooSmall() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-small");
        user.setEmail("change-password-too-small@example.com");
        userRepository.saveAndFlush(user);

        String newPassword = RandomStringUtils.random(5);

        restAccountMockMvc
            .perform(
                post(API_VERSION + "/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, newPassword)))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-too-small").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @Transactional
    @WithMockUser("change-password-empty")
    void testChangePasswordEmpty() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-empty");
        user.setEmail("change-password-empty@example.com");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(
                post(API_VERSION + "/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, "")))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-empty").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void testRequestPasswordResetWrongEmail() throws Exception {
        restAccountMockMvc
            .perform(post(API_VERSION + "/account/reset-password/init").content("password-reset-wrong-email@example.com"))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser("change-password-too-long")
    void testChangePasswordTooLong() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-long");
        user.setEmail("change-password-too-long@example.com");
        userRepository.saveAndFlush(user);

        String newPassword = RandomStringUtils.random(51);

        restAccountMockMvc
            .perform(
                post(API_VERSION + "/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, newPassword)))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-too-long").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

}

