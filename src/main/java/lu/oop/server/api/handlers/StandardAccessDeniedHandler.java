package lu.oop.server.api.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lu.oop.server.api.exceptions.RequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler, which handles the cases when the user does not have correct roles
 */
@Component
public class StandardAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, IOException {

        RequestException e = new RequestException(
                HttpStatus.FORBIDDEN,
                "invalid role",
                "most likely that the user is not in the correct role"
        );
        e.log();
        e.toResponse(request).writeToResponse(response);
    }
}