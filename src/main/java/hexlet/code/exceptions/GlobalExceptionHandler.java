package hexlet.code.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public List<ObjectError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exc) {
        return exc.getAllErrors();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public String notFoundExceptionHandler(NotFoundException exc) {
        return exc.getMessage();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public String badCredentialsExceptionHandler(BadCredentialsException exc) {
        return exc.getMessage();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public String accessDeniedExceptionHandler(AccessDeniedException exc) {
        return exc.getMessage();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public String duplicateKeyExceptionHandler(DuplicateKeyException exc) {
        return exc.getMessage();
    }

}
