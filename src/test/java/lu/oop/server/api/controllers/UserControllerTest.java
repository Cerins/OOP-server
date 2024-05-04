package lu.oop.server.api.controllers;

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
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void getUserById_UserNotFound_ShouldReturnNotFound() {
        Long usrId = 1l;
        when(userService.getById(usrId)).thenReturn(Optional.empty());
        ResponseEntity<IUserModel> res = userController.getUserById(usrId);
        assertEquals(res.getStatusCode().value(), 404);
    }

    @Test
    public void getUserById_UserFound_ShouldReturnUser() {
        Long usrId = 1l;
        StudentModel st = new StudentModel();
        when(userService.getById(usrId)).thenReturn(Optional.of(st));
        ResponseEntity<IUserModel> res = userController.getUserById(usrId);
        assertEquals(res.getStatusCode().value(), 200);
        assertInstanceOf(IStudentModel.class, res.getBody());
    }
}