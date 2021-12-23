package hexlet.code.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<ObjectError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exc) {
        return exc.getAllErrors();
    }

}
