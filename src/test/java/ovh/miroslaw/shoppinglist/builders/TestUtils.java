package ovh.miroslaw.shoppinglist.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;
import ovh.miroslaw.shoppinglist.service.dto.IngredientDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T mvcResultToObject(MvcResult result, Class<T> type) throws IOException {
        return mapper.readValue(result.getResponse().getContentAsString(), type);
    }

    public static <T> List<T> mvcResultToList(MvcResult result, Class<IngredientDTO> elementClass) throws IOException {
        CollectionType listType =
            mapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);
        return mapper.readValue(result.getResponse().getContentAsString(), listType);
    }

    public static HttpEntity getRequest(String jwtToken, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return new HttpEntity<>(body, headers);
    }

    public static HttpEntity getRequest(String jwtToken) {
        return getRequest(jwtToken, null);
    }


    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

}
