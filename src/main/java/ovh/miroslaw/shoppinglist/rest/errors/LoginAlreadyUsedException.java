package ovh.miroslaw.shoppinglist.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginAlreadyUsedException extends RuntimeException {

    public LoginAlreadyUsedException() {
        super("Login name already used!");
    }
}
