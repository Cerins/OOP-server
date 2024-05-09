package lu.oop.server.api.controllers;

import lu.oop.server.app.services.IMessageService;
import lu.oop.server.app.models.messages.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.sql.Timestamp;


@RequestMapping("/messages")
@RestController
public class MessageController {

    private IMessageService messageService;

    public static class MessageRequest {
        private String text;
        private int senderId;
        private int receiverId;

        public String getText(){
            return text;
        }
        public int getSenderId(){
            return senderId;
        }
        public int getReceiverId(){
            return receiverId;
        }
    }

    public static class ConversationRequest {
        private int user1Id;
        private int user2Id;
        private Timestamp dateTimeFrom;

        public int getUser1Id(){
            return user1Id;
        }
        public int getUser2Id(){
            return user2Id;
        }
        public Timestamp getDateTimeFrom(){
            return dateTimeFrom;
        }
    }

    @Autowired
    MessageController(IMessageService MessageService) {
        this.messageService = MessageService;
    }

    @PostMapping()
    public ResponseEntity<IMessageModel> createMessage(@RequestBody MessageRequest messageRequest) {
        try {
            String text = messageRequest.getText();
            Long senderId = Long.valueOf(messageRequest.getSenderId());
            Long receiverId = Long.valueOf(messageRequest.getReceiverId());
            
            if(text == null){
                text = "";
            }

            messageService.create(text, senderId, receiverId);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<IMessageModel>> getMessageById(@RequestBody ConversationRequest conversationRequest) {
        Long user1Id = Long.valueOf(conversationRequest.getUser1Id());
        Long user2Id = Long.valueOf(conversationRequest.getUser2Id());
        Timestamp dateTimeFrom = conversationRequest.getDateTimeFrom();

        List<IMessageModel> conversation = messageService.getConversation(user1Id, user2Id, dateTimeFrom);
        return ResponseEntity.ok(conversation);
    }
}
