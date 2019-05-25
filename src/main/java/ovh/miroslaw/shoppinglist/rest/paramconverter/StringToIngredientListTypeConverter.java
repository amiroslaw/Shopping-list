package ovh.miroslaw.shoppinglist.rest.paramconverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import ovh.miroslaw.shoppinglist.rest.errors.BadRequestException;

public class StringToIngredientListTypeConverter implements Converter<String, IngredientListType>  {
    private final Logger log = LoggerFactory.getLogger(StringToIngredientListTypeConverter.class);
    @Override
    public IngredientListType convert(String from) {
        try {
            return IngredientListType.valueOf(from);
        } catch (BadRequestException e) {
            log.error("can not convert param to ingredient list type");
            return null;
        }
    }
}
