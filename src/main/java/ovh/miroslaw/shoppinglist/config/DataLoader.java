package ovh.miroslaw.shoppinglist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ovh.miroslaw.shoppinglist.domain.Authority;
import ovh.miroslaw.shoppinglist.domain.Ingredient;
import ovh.miroslaw.shoppinglist.domain.Recipe;
import ovh.miroslaw.shoppinglist.domain.User;
import ovh.miroslaw.shoppinglist.domain.enumeration.Difficulty;
import ovh.miroslaw.shoppinglist.repository.*;
import ovh.miroslaw.shoppinglist.service.RecipeService;
import ovh.miroslaw.shoppinglist.service.UserService;

import java.util.*;


@Component
public class DataLoader implements ApplicationRunner {
    private final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private IngredientRepository ingredientRepository;
    private RecipeRepository recipeRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @Autowired
    DataLoader(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        User normalUser = createUser("Hulio", "pass", "hulio", "hulio@gmail.com", true, "ROLE_USER");

        Ingredient pasta = createIngredient("makaron");
        Ingredient sos = createIngredient("sos");
        Ingredient tomato = createIngredient("pomidor");

        addPurchasedIngredientsToUser(normalUser, pasta, sos, tomato, pasta);

        Map<Ingredient, Float> spaghettiIngredients = Map.of(pasta, 0.5F, tomato, 1F, sos, 0.3F);
        addUserIngredientsToUser(normalUser, spaghettiIngredients);
        addShoppingListToUser(normalUser, spaghettiIngredients);

        Recipe spaghetti = createRecipe("spaghetti", "spaghetti italiano", "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwiluqeAtZbiAhVK_SoKHf1KDecQjRx6BAgBEAU&url=https%3A%2F%2Fwww.bbc.com%2Ffood%2Frecipes%2Fmicrowave_spaghetti_44920&psig=AOvVaw2L2f4GD-Gpfkc4fVea-8hP&ust=1557764994301009",
            true, Difficulty.EASY, normalUser, spaghettiIngredients);

        userRepository.save(normalUser);

        repoTest();
    }

    private void repoTest() {
        log.debug("all recipe");
        System.out.println(recipeService.findAll());

        log.debug("all recipe eager");
        System.out.println(recipeRepository.findAllWithEagerIngredients().get(0).getIngredients());

        log.debug("users");
        List<User> users = userRepository.findAll();
        System.out.println(users);

        log.debug("shoppinglist");
        System.out.println(userService.findUserShoppingList(users.get(0).getId()));

        log.debug("purchased ing");
        System.out.println(userRepository.findPurchasedIngredients(users.get(0).getId()));
    }

    private void addUserIngredientsToUser(User user, Map<Ingredient, Float> ingredients){
        log.debug("addUserIngredientsToUser");
        user.setUserIngredients(ingredients);
        userRepository.save(user);
    }

    private void addShoppingListToUser(User user, Map<Ingredient, Float> ingredients){
        log.debug("addShoppingListToUser");
        user.setShoppingList(ingredients);
        userRepository.save(user);
    }

    private void addPurchasedIngredientsToUser(User user, Ingredient... ingredients) {
        log.debug("addPurchasedIngredientsToUser");
        user.setPurchasedIngredients(new HashSet<Ingredient>(Arrays.asList(ingredients)));
        userRepository.save(user);
    }

    private Ingredient createIngredient(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setPopularity(1);
        return ingredientRepository.save(ingredient);
    }

    private Recipe createRecipe(String title, String description, String imgUrl, boolean visible, Difficulty difficulty, User user, Map<Ingredient, Float> ingredients) {
        log.debug("createRecipe");
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setImgUrl(imgUrl);
        recipe.setVisible(visible);
        recipe.setDifficulty(difficulty);
        recipe.setIngredients(ingredients);
        recipe.addUser(user);
        user.getRecipes().add(recipe);
        return recipeRepository.save(recipe);
    }

    private User createUser(String name, String pass, String login, String email, boolean isActivated, String user_role) {
        User user = new User();
        user.setFirstName(name);
        user.setPassword(passwordEncoder.encode(pass));
        user.setLogin(login);
        user.setEmail(email);
        user.setActivated(isActivated);
        Authority auth = new Authority();
        auth.setName(user_role);
        authorityRepository.save(auth);
        user.setAuthorities(new HashSet<Authority>(Collections.singleton(auth)));
        return userRepository.save(user);
    }

    private void loadData() {
//        Translation translation = Translation.builder().source("W końcu mam akt zgonu mojego ojca. Moja siostra była na tyle uprzejma, że wysłała jej zdjęcie, a ona prześle jej kopię pocztą. Myślałem, że urodził się w Hammond w stanie Indiana w jedynym szpitalu w okolicy.")
//                .source("Jednak certyfikat mówi, że urodził się w Illinois, po drugiej stronie granicy państwowej, więc musiało to być porody domowe. Wprowadziłem zmianę w drzewie genealogicznym, aby zachować dokładność.")
//                .articleTranslation("I finally have a death certificate of my father. My sister was kind enough to email a photo of it and she will send a copy of it through the mail. I thought he was born in Hammond, Indiana, in the only hospital in the area.")
//                .articleTranslation("However, the certificate says he was born in Illinois, just across the state border so it must have been a home birth. I made the change in the family tree to keep it accurate.")
//                .build();
//        translationRepository.save(translation);
//        Dictionary dictionary = Dictionary.builder().build();
//
//        Date date = Calendar.getInstance().getTime();
//        Collection<Role> roleUser = Arrays.asList(new Role("ROLE_USER"));
//        User user = User.builder().name("qwer").password("qwer").roles(roleUser).dictionary(dictionary).build();
//        user = userService.createUser(user);
//
//        User user2 = User.builder().name("user").password("user").roles(roleUser).build();
//        user2 = userService.createUser(user2);
//        User userEmpty = User.builder().name("empty").password("empty").roles(roleUser).build();
//        userService.createUser(userEmpty);
//
//        Notebook publicNotebook = Notebook.builder().title("public").description("public notebook for no register user").user(user2).build();
//        notebookService.createOrUpdateNotebook(publicNotebook);
//
//        Notebook notebook = Notebook.builder().title("DB notebook").description("first notebook for user").user(user).build();
//        Notebook notebook2 = Notebook.builder().title("DB notebook2").description("second notebook for user")
//                .user(user).build();
//        notebookService.createOrUpdateNotebook(notebook);
//        notebookService.createOrUpdateNotebook(notebook2);
//
//        Notebook notebookTest = Notebook.builder().title("test").description("first notebook for qwer").user(user2).build();
//        Notebook notebookTest2 = Notebook.builder().title("test private").description("second notebook for qwer").user(user2).build();
//        notebookService.createOrUpdateNotebook(notebookTest);
//        notebookService.createOrUpdateNotebook(notebookTest2);
//
//        Article article1 = Article.builder().title("DB article1")
//                .firstLanguage(Arrays.asList("a", "b")).secondLanguage(Arrays.asList("c", "d"))
//                .tag("tag1")
//                .creationDate(date)
//                .hidden(false)
//                .notebook(notebook)
//                .build();
//        Article article2 = Article.builder().title("DB article2")
//                .firstLanguage(Arrays.asList("ac", "bc")).secondLanguage(Arrays.asList("cc", "dc"))
//                .tag("tag2")
//                .creationDate(date)
//                .hidden(false)
//                .notebook(notebook2)
//                .build();
//        Article articlePrivate = Article.builder().title("DB article private")
//                .firstLanguage(Arrays.asList("ac", "bc")).secondLanguage(Arrays.asList("cc", "dc"))
//                .tag("tag2")
//                .creationDate(date)
//                .hidden(true)
//                .notebook(notebook)
//                .build();
//        articleService.createArticle(article1);
//        articleService.createArticle(article2);
//        articleService.createArticle(articlePrivate);
    }

}
