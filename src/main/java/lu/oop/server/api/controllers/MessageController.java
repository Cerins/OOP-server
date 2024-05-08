package lu.oop.server.api.controllers;

import lu.oop.server.app.services.IMessageService;
import lu.oop.server.app.models.messages.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


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

    @Autowired
    MessageController(IMessageService MessageService) {
        this.messageService = MessageService;
    }

    @PostMapping("/")
    public ResponseEntity<IMessageModel> createMessage(@RequestBody MessageRequest messageRequest) {
        try {
            String text = messageRequest.getText();
            Long senderId = Long.valueOf(messageRequest.getSenderId());
            Long receiverId = Long.valueOf(messageRequest.getReceiverId());

            messageService.create(text, senderId, receiverId);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/conversation/{firstUserId}/{secondUserId}")
    public ResponseEntity<List<MessageModel>> getMessageById(@PathVariable Long firstUserId, @PathVariable Long secondUserId) {
        List<MessageModel> conversation = messageService.getConversation(firstUserId, secondUserId);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<Set<Integer>> getMessageById(@PathVariable Long userId) {
        Set<Integer> conversation = messageService.getConversations(userId);
        return ResponseEntity.ok(conversation);
    }
    
}
