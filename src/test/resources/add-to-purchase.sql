insert into ingredient_name (id, name, popularity)
    values (9, 'apple', 1);
insert into ingredient_name (id, name, popularity)
    values (10, 'onion', 1);
insert into ingredient_name (id, name, popularity)
    values (11, 'garlic', 1);

insert into ingredient (id, amount_id, name_id) values (10, 1, 9);
insert into ingredient (id, amount_id, name_id) values (11, 1, 10);
insert into ingredient (id, amount_id, name_id) values (12, 1, 11);

insert into shopping_purchased_ingredient (shopping_purchased_id, ingredient_id) values (1, 10);
insert into shopping_purchased_ingredient (shopping_purchased_id, ingredient_id) values (1, 11);
insert into shopping_purchased_ingredient (shopping_purchased_id, ingredient_id) values (1, 12);
