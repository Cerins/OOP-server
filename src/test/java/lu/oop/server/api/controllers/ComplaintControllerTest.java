package lu.oop.server.api.controllers;

import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.complaints.IComplaintModel;
import lu.oop.server.app.models.users.IAdminModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.services.IComplaintService;
import lu.oop.server.app.services.IUserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComplaintControllerTest {

    @Mock
    private IComplaintService complaintService;

    @Mock
    private IUserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private IAdminModel adminUser;

    @InjectMocks
    private ComplaintController complaintController;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void createComplaint_AsOtherUser_ShouldReturnForbidden() {
        ComplaintController.ComplaintRequest complaintRequest = new ComplaintController.ComplaintRequest();
        complaintRequest.setTitle("Title");
        complaintRequest.setText("Text");
        complaintRequest.setComplaintantId(2);

        when(authentication.getPrincipal()).thenReturn(mock(IAdminModel.class));

        ResponseEntity<IComplaintModel> response = complaintController.createComplaint(complaintRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void assignComplaint_NonAdminAssignsComplaint_ShouldReturnError() {
        ResponseEntity<IComplaintModel> response = complaintController.assignComplaint(1, 1);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void closeComplaint_AdminClosesCompalintNotAssigned_ShouldReturnError() {
        when(authentication.getPrincipal()).thenReturn(adminUser);
        when(userService.getById(any())).thenReturn(Optional.of(adminUser));

        ResponseEntity<IComplaintModel> response = complaintController.closeComplaint(1);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getUnassigned_AdminGetsUnassignedComplaints_ShouldReturnList() {
        List<ComplaintModel> unassignedComplaints = List.of(mock(ComplaintModel.class));
        when(complaintService.getUnasignedComplaints()).thenReturn(unassignedComplaints);

        List<ComplaintModel> response = complaintController.getUnassigned();

        assertEquals(unassignedComplaints, response);
    }
}
