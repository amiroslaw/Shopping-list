package ovh.miroslaw.shoppinglist.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException() {
        super("Email address not registered");
    }
}
