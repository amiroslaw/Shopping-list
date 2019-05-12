INSERT INTO USER(ACTIVATED, CREATED_DATE, EMAIL, LANG_KEY, LOGIN, PASSWORD_HASH)
-- password 'passff'
values (TRUE, '2023-01-02 15:30:36.81589', 'email@gmail.com', 'en-US', 'hulio',
        '$2a$10$.OIfzJu517qtO5ghBe17q.jsCGtWcqdRwi2GdU5G/0D9f2sJH2kmO');
INSERT INTO USER(ACTIVATED, CREATED_DATE, EMAIL, LANG_KEY, LOGIN, PASSWORD_HASH)
values (TRUE,'2023-01-02 15:30:36.81589', 'admin@gmail.com', 'en-US', 'admin', '$2a$10$.OIfzJu517qtO5ghBe17q.jsCGtWcqdRwi2GdU5G/0D9f2sJH2kmO');


insert into authority (name)
    values ('ROLE_USER');
insert into authority (name)
    values ('ROLE_ADMIN');
insert into user_authority (user_id, authority_name)
    values (1, 'ROLE_USER');
insert into user_authority (user_id, authority_name)
    values (2, 'ROLE_ADMIN');

insert into ingredient_amount (id, amount, unit)
values (1, 1, 'PIECE');
insert into ingredient_amount (id, amount, unit)
values (2, 1, 'TABLESPOON');
insert into ingredient_amount (id, amount, unit)
values (3, 300, 'ML');
insert into ingredient_amount (id, amount, unit)
values (4, 400, 'GRAMS');

insert into ingredient_name (id, name, popularity)
    values (1, 'bread', 1);
INSERT INTO INGREDIENT_NAME (id, NAME, POPULARITY)
    values (2, 'peanut butter', 1);
INSERT INTO INGREDIENT_NAME (id, NAME, POPULARITY)
    values (3, 'oil', 1);
insert into ingredient_name (id, name, popularity)
    values (4, 'carrots', 1);
insert into ingredient_name (id, name, popularity)
    values (5, 'egg', 1);
insert into ingredient_name (id, name, popularity)
    values (6, 'noodles', 1);
insert into ingredient_name (id, name, popularity)
    values (7, 'chicken', 1);
insert into ingredient_name (id, name, popularity)
    values (8, 'parmesan', 1);


-- INGREDIENTS
insert into ingredient (id, amount_id, name_id) values (1, 1, 1);
insert into ingredient (id, amount_id, name_id) values (2, 1, 2);
insert into ingredient (id, amount_id, name_id) values (3, 1, 3);
insert into ingredient (id, amount_id, name_id) values (4, 1, 4);
insert into ingredient (id, amount_id, name_id) values (5, 1, 5);
insert into ingredient (id, amount_id, name_id) values (6, 4, 6);
insert into ingredient (id, amount_id, name_id) values (7, 4, 7);
insert into ingredient (id, amount_id, name_id) values (8, 4, 8);

-- RECIPE
insert into recipe (id, title, description, difficulty, img_url, visible)
    values (1, 'sandwich', 'sandwich with peanut butter', 'EASY', 'https://www.tasteofhome.com/wp-content/uploads/2019/10/peanut-butter-and-mayo-on-white-bread-ad-edit-2-scaled.jpg?resize=700,467', TRUE);
insert into recipe (id, title, description, difficulty, img_url, visible)
    values (2, 'Chicken Soup', 'Best thing on a sick day.', 'EASY', 'https://www.justthefuckingrecipe.net/wp-content/uploads/2020/03/chicknood.jpg', TRUE);
insert into recipe (id, title, description, difficulty, img_url, visible)
    values (3, 'scrambled eggs', 'Perfect scrambled eggs recipe', 'EASY', 'https://upload.wikimedia.org/wikipedia/commons/c/cb/Potato_galettes_with_quail_eggs.jpg', TRUE);
insert into recipe (id, title, description, difficulty, img_url, visible)
    values (4, 'SPAGHETTI CARBONARA', 'This recipe for Spaghetti Carbonara was adapted from several websites.', 'EASY', 'https://www.justthefuckingrecipe.net/wp-content/uploads/2020/04/Spaghetti-Carbonara.jpg', FALSE );

-- USER INGREDIENTS and RECIPES
insert into recipe_ingredient (recipe_id, ingredient_id) values (1,1);
insert into recipe_ingredient (recipe_id, ingredient_id) values (1,2);

insert into recipe_ingredient (recipe_id, ingredient_id) values (2,4);
insert into recipe_ingredient (recipe_id, ingredient_id) values (2,5);
insert into recipe_ingredient (recipe_id, ingredient_id) values (2,6);
insert into recipe_ingredient (recipe_id, ingredient_id) values (2,7);

insert into recipe_ingredient (recipe_id, ingredient_id) values (3,3);
insert into recipe_ingredient (recipe_id, ingredient_id) values (3,5);

insert into recipe_ingredient (recipe_id, ingredient_id) values (4,5);
insert into recipe_ingredient (recipe_id, ingredient_id) values (4,6);
insert into recipe_ingredient (recipe_id, ingredient_id) values (4,8);

insert into user_ingredient_ingredient (user_id, ingredient_id) values (1, 3);
insert into user_ingredient_ingredient (user_id, ingredient_id) values (1, 4);
insert into user_ingredient_ingredient (user_id, ingredient_id) values (1, 6);
insert into user_ingredient_ingredient (user_id, ingredient_id) values (2, 8);

insert into shopping_list_ingredient (shopping_ingredient_id, ingredient_id) values (1, 3);
insert into shopping_list_ingredient (shopping_ingredient_id, ingredient_id) values (1, 4);

insert into shopping_purchased_ingredient (shopping_purchased_id, ingredient_id) values (1, 5);
insert into shopping_purchased_ingredient (shopping_purchased_id, ingredient_id) values (1, 6);

insert into user_recipes (user_id, recipes_id) values (2, 1);
insert into user_recipes (user_id, recipes_id) values (1, 2);
insert into user_recipes (user_id, recipes_id) values (1, 3);
insert into user_recipes (user_id, recipes_id) values (1, 4);
