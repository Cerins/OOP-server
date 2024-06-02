package lu.oop.server.api.handlers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.api.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class StandardErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request)  {
        RequestException e = new RequestException(
                HttpStatus.FORBIDDEN,
                "invalid role",
                "most likely that the user is not in the correct role"
        );
        e.log();
        return new ResponseEntity<>(e.toResponse(request), e.getCode());
    }
    // If we see the request exception
    @ExceptionHandler(RequestException.class)
    public final ResponseEntity<ErrorResponse> handleRequestException(RequestException e, HttpServletRequest request) {
        // Then we log
        e.log();
        // And return the standard response based on the exception info
        return new ResponseEntity<>(e.toResponse(request), e.getCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorResponse> handleAny(DataIntegrityViolationException e, HttpServletRequest request) {
        // TODO send error in a pretty format
        RequestException re = new RequestException(
                HttpStatus.BAD_REQUEST,
                "constraint violation",
                "item does not meet the criteria or already exists",
                e.getMessage()
        );
        re.log();
        return new ResponseEntity<>(re.toResponse(request), re.getCode());
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAny(Exception e, HttpServletRequest request) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        RequestException re = new RequestException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "server error",
                exceptionAsString
        );
        re.log();
        // Then we log
        // And return the standard response based on the exception info
        return new ResponseEntity<>(re.toResponse(request), re.getCode());
    }
}
