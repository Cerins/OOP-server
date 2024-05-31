package lu.oop.server.api.controllers;

import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.api.utils.JwtUtil;
import lu.oop.server.app.models.users.IStudentModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.StudentModel;
import lu.oop.server.app.services.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private  IUserModel usr;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;



    @Test
    void login_UserNotFound_ShouldThrowRequestError() {
        String email = "user@oop.test";
        String password = "baka";
        when(userService.getByLogin(email)).thenReturn(Optional.empty());
        AuthController.AuthLoginReq req = new AuthController.AuthLoginReq(
                email,
                password
        );
        RequestException e = assertThrows(RequestException.class, ()->authController.login(
            req
        ));
        assertEquals(e.getCode().value(), 400);
        assertEquals(e.getError(), "wrong login or password");
        assertEquals(e.getLogMessage(), "user wrong login");
    }
    @Test
    void login_WrongPassword_ShouldThrowRequestError() {
        String email = "user@oop.test";
        String password = "baka";
        when(userService.getByLogin(email)).thenReturn(Optional.of(usr));
        when(usr.passwordMatches(password)).thenReturn(false);
        AuthController.AuthLoginReq req = new AuthController.AuthLoginReq(
                email,
                password
        );
        RequestException e = assertThrows(RequestException.class, ()->authController.login(
                req
        ));
        assertEquals(e.getCode().value(), 400);
        assertEquals(e.getError(), "wrong login or password");
        assertEquals(e.getLogMessage(), "user wrong password");
    }

    @Test
    void login_Success_ShouldReturnToken() throws RequestException {
        Long id = 1L;
        String email = "user@oop.test";
        String password = "baka";
        String token = "DA KEY";
        when(userService.getByLogin(email)).thenReturn(Optional.of(usr));
        when(usr.passwordMatches(password)).thenReturn(true);
        when(usr.getId()).thenReturn(id);
        when(jwtUtil.generateToken(id)).thenReturn(token);
        AuthController.AuthLoginReq req = new AuthController.AuthLoginReq(
                email,
                password
        );
        ResponseEntity<AuthController.AuthLoginRes> res = authController.login(
                req
        );
        assertEquals(res.getStatusCode().value(), 200);
        assertEquals(res.getBody().getToken(), token);
    }

    @Test
    void register() {
        // TODO
    }

    @Test
    void logout_ShouldReturnOk() {
        ResponseEntity res = authController.logout();
        assertEquals(res.getStatusCode().value(), 200);
    }
}