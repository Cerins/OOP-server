package lu.oop.server.api.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lu.oop.server.api.exceptions.RequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler, which handles unsuccessful authentications
 */
@Component
public class StandardAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        RequestException e = new RequestException(
                HttpStatus.UNAUTHORIZED,
                "unauthorized",
                "most likely that the token was not supplied or is malformed"
        );
        e.log();
        e.toResponse(request).writeToResponse(response);

    }
}
