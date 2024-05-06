package lu.oop.server.api.controllers;

import lu.oop.server.app.services.IMessageService;
import lu.oop.server.app.models.messages.*;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RequestMapping("/messages")
@RestController
public class MessageController {

    private IMessageService messageService;

    public class MessageRequest {
        private String text;
        private Long senderId;
        private Long recieverId;

        public String getText(){
            return text;
        }
        public Long getSenderId(){
            return senderId;
        }
        public Long getReceiverId(){
            return recieverId;
        }
    }

    @Autowired
    MessageController(IMessageService MessageService) {
        this.messageService = MessageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IMessageModel> getMessageById(@PathVariable Long id) {
        Optional<IMessageModel> mbyMessage = messageService.getById(id);
        if(mbyMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        IMessageModel message = mbyMessage.get();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/")
    public ResponseEntity<IMessageModel> createMessage(@RequestBody MessageRequest messageRequest) {
        try {
            String text = messageRequest.getText();
            Long senderId = messageRequest.getSenderId();
            Long receiverId = messageRequest.getReceiverId();

            messageService.create(text, senderId, receiverId);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
