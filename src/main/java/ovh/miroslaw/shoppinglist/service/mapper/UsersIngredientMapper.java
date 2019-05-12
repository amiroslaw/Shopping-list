package ovh.miroslaw.shoppinglist.service.mapper;

import ovh.miroslaw.shoppinglist.domain.*;

import org.mapstruct.*;
import ovh.miroslaw.shoppinglist.service.dto.UsersIngredientDTO;

/**
 * Mapper for the entity User with association with recipe and ingredients lists
 */
@Mapper(componentModel = "spring", uses = {RecipeMapper.class, IngredientMapper.class})
public interface UsersIngredientMapper extends EntityMapper<UsersIngredientDTO, User> {
    UsersIngredientDTO toDto(User user);

    @Mapping(target = "activated", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "login", ignore = true)
    @Mapping(target = "langKey", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "resetKey", ignore = true)
    @Mapping(target = "resetDate", ignore = true)
    User toEntity(UsersIngredientDTO userDTO);

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
