package ovh.miroslaw.shoppinglist.rest.errors;

import org.hibernate.PersistentObjectException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorAdvice {

    @ResponseBody
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(EmptyResultDataAccessException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(PersistentObjectException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(PersistentObjectException ex) {
        return ex.getMessage();
    }

    // for validation with dto
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String customHttpStatus(ConstraintViolationException ex) {
        return ex.getMessage();
    }

}
