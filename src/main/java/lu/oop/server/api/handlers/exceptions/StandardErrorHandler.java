package lu.oop.server.api.handlers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.api.responses.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StandardErrorHandler {
    // If we see the request exception
    @ExceptionHandler(RequestException.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(RequestException e, HttpServletRequest request) {
        // Then we log
        e.log();
        // And return the standard response based on the exception info
        return new ResponseEntity<>(e.toResponse(request), e.getCode());
    }
}
