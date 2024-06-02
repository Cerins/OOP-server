package lu.oop.server.api.controllers;

import lu.oop.server.api.controllers.MessageController.MessageRequest;
import lu.oop.server.api.controllers.MessageController.ConversationRequest;
import lu.oop.server.app.models.messages.IMessageModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.services.IMessageService;
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

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private IMessageService messageService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private IUserModel loggedInUser;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(loggedInUser);
    }

    @Test
    public void createMessage_UserNotSender_ShouldReturnForbidden() {
        when(loggedInUser.getId()).thenReturn(2L);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setSenderId(1);

        ResponseEntity<IMessageModel> response = messageController.createMessage(messageRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void createMessage_TeacherInitiatesConversation_ShouldReturnForbidden() {
        when(loggedInUser.getId()).thenReturn(1L);
        when(loggedInUser.getRoleName()).thenReturn("teacher");
        when(userService.getConversations(1L)).thenReturn(Collections.emptyList());

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setSenderId(1);
        messageRequest.setReceiverId(2);

        ResponseEntity<IMessageModel> response = messageController.createMessage(messageRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getConversation_UserNotPartOfConversation_ShouldReturnForbidden() {
        when(loggedInUser.getId()).thenReturn(3L);

        ConversationRequest conversationRequest = new ConversationRequest();
        conversationRequest.setUser1Id(1);
        conversationRequest.setUser2Id(2);

        ResponseEntity<List<IMessageModel>> response = messageController.getConversation(conversationRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getFiles_UserNotSenderOrReceiver_ShouldReturnForbidden() {
        when(loggedInUser.getId()).thenReturn(3L);

        IMessageModel message = mock(IMessageModel.class);
        when(messageService.getById(1L)).thenReturn(message);
        when(message.getSender()).thenReturn(1L);
        when(message.getReceiver()).thenReturn(2L);

        ResponseEntity<List<byte[]>> response = messageController.getFiles(1L);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
