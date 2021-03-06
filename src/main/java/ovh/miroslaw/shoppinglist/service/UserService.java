package ovh.miroslaw.shoppinglist.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import ovh.miroslaw.shoppinglist.config.Constants;
import ovh.miroslaw.shoppinglist.domain.Authority;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.repository.AuthorityRepository;
import ovh.miroslaw.shoppinglist.repository.UserRepository;
import ovh.miroslaw.shoppinglist.security.SecurityUtils;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;
import ovh.miroslaw.shoppinglist.service.dto.RecipeDTO;
import ovh.miroslaw.shoppinglist.service.dto.UserDTO;
import ovh.miroslaw.shoppinglist.service.mapper.IngredientMapper;
import ovh.miroslaw.shoppinglist.service.mapper.RecipeMapper;
import ovh.miroslaw.shoppinglist.service.mapper.UserMapper;
import ovh.miroslaw.shoppinglist.service.mapper.UsersIngredientMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.miroslaw.shoppinglist.service.util.RandomUtil;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UsersIngredientMapper usersIngredientMapper;
    private final IngredientMapper ingredientMapper;
    private final RecipeMapper recipeMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, UsersIngredientMapper usersIngredientMapper, IngredientMapper ingredientMapper, RecipeMapper recipeMapper, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.usersIngredientMapper = usersIngredientMapper;
        this.ingredientMapper = ingredientMapper;
        this.recipeMapper = recipeMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> findRecipes(Long userId) {
        return userRepository.findRecipes(userId)
            .stream()
            .map(recipeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    //
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final AuthorityRepository authorityRepository;
//
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.authorityRepository = authorityRepository;
//    }
//
//    public Optional<User> activateRegistration(String key) {
//        log.debug("Activating user for activation key {}", key);
//        return userRepository.findOneByActivationKey(key)
//            .map(user -> {
//                // activate given user for the registration key.
//                user.setActivated(true);
//                user.setActivationKey(null);
//                log.debug("Activated user: {}", user);
//                return user;
//            });
//    }
//
//    public Optional<User> completePasswordReset(String newPassword, String key) {
//        log.debug("Reset user password for reset key {}", key);
//        return userRepository.findOneByResetKey(key)
//            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
//            .map(user -> {
//                user.setPassword(passwordEncoder.encode(newPassword));
//                user.setResetKey(null);
//                user.setResetDate(null);
//                return user;
//            });
//    }
//
//    public Optional<User> requestPasswordReset(String mail) {
//        return userRepository.findOneByEmailIgnoreCase(mail)
//            .filter(User::getActivated)
//            .map(user -> {
//                user.setResetKey(RandomUtil.generateResetKey());
//                user.setResetDate(Instant.now());
//                return user;
//            });
//    }
//
//    public User registerUser(UserDTO userDTO, String password) {
//        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
//            boolean removed = removeNonActivatedUser(existingUser);
//            if (!removed) {
//                throw new LoginAlreadyUsedException();
//            }
//        });
//        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
//            boolean removed = removeNonActivatedUser(existingUser);
//            if (!removed) {
//                throw new EmailAlreadyUsedException();
//            }
//        });
//        User newUser = new User();
//        String encryptedPassword = passwordEncoder.encode(password);
//        newUser.setLogin(userDTO.getLogin().toLowerCase());
//        // new user gets initially a generated password
//        newUser.setPassword(encryptedPassword);
//        newUser.setFirstName(userDTO.getFirstName());
//        newUser.setLastName(userDTO.getLastName());
//        newUser.setEmail(userDTO.getEmail().toLowerCase());
//        newUser.setImageUrl(userDTO.getImageUrl());
//        newUser.setLangKey(userDTO.getLangKey());
//        // new user is not active
//        newUser.setActivated(false);
//        // new user gets registration key
//        newUser.setActivationKey(RandomUtil.generateActivationKey());
//        Set<Authority> authorities = new HashSet<>();
//        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
//        newUser.setAuthorities(authorities);
//        userRepository.save(newUser);
//        log.debug("Created Information for User: {}", newUser);
//        return newUser;
//    }
//
//    private boolean removeNonActivatedUser(User existingUser){
//        if (existingUser.getActivated()) {
//             return false;
//        }
//        userRepository.delete(existingUser);
//        userRepository.flush();
//        return true;
//    }
//
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName  last name of user
     * @param email     email id of user
     * @param langKey   language key
     * @param imageUrl  image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::userToUserDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }
    //
    //    public void changePassword(String currentClearTextPassword, String newPassword) {
    //        SecurityUtils.getCurrentUserLogin()
    //            .flatMap(userRepository::findOneByLogin)
    //            .ifPresent(user -> {
    //                String currentEncryptedPassword = user.getPassword();
    //                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
    //                    throw new InvalidPasswordException();
    //                }
    //                String encryptedPassword = passwordEncoder.encode(newPassword);
    //                user.setPassword(encryptedPassword);
    //                log.debug("Changed password for User: {}", user);
    //            });
    //    }
    //
    //    @Transactional(readOnly = true)
    //    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
    //        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    //    }
    //

//
//    /**
//     * Not activated users should be automatically deleted after 3 days.
//     * <p>
//     * This is scheduled to get fired everyday, at 01:00 (am).
//     */
//    @Scheduled(cron = "0 0 1 * * ?")
//    public void removeNotActivatedUsers() {
//        userRepository
//            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
//            .forEach(user -> {
//                log.debug("Deleting not activated user {}", user.getLogin());
//                userRepository.delete(user);
//            });
//    }
//
}
