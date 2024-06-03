package lu.oop.server.api.controllers;

import lu.oop.server.app.services.IMessageService;
import lu.oop.server.app.services.IUserService;
import lu.oop.server.app.models.messages.*;
import lu.oop.server.app.models.users.IUserModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.sql.Timestamp;


@RequestMapping("/messages")
@RestController
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    private IMessageService messageService;
    private IUserService userService;

    public static class MessageRequest {
        private String text;
        private int senderId;
        private int receiverId;
        private int respondsToId;
        private byte[] file;
        private String fileName;

        public String getText(){
            return text;
        }
        public int getSenderId(){
            return senderId;
        }
        public int getReceiverId(){
            return receiverId;
        }
        public int getRespondsToId(){
            return respondsToId;
        }
        public byte[] getFile(){
            return file;
        }
        public String getFileName(){
            return fileName;
        }
        public void setText(String text) {
            this.text = text;
        }
        public void setSenderId(int senderId) {
            this.senderId = senderId;
        }
        public void setReceiverId(int receiverId) {
            this.receiverId = receiverId;
        }
        public void setRespondsToId(int respondsToId) {
            this.respondsToId = respondsToId;
        }
        public void setFile(byte[] file) {
            this.file = file;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
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
        public void setUser1Id(Integer id){
            this.user1Id = id;
        }
        public void setUser2Id(Integer id){
            this.user1Id = id;
        }
    }

    @Autowired
    MessageController(IMessageService MessageService, IUserService UserService) {
        this.messageService = MessageService;
        this.userService = UserService;
    }

    @PostMapping()
    public ResponseEntity<IMessageModel> createMessage(@RequestBody MessageRequest messageRequest) {
        try {
            String text = messageRequest.getText();
            Long senderId = Long.valueOf(messageRequest.getSenderId());
            Long receiverId = Long.valueOf(messageRequest.getReceiverId());
            Long respondsToId = Long.valueOf(messageRequest.getRespondsToId());
            byte[] file = messageRequest.getFile();
            String fileName = messageRequest.getFileName();

            IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
            if(!loggedInUser.getId().equals(senderId)) {
                // Can only send message from yourself
                logger.warn("User attempted to send message as other user");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if(loggedInUser.getRoleName().equals("teacher")) {
                List<Integer> conversatons = userService.getConversations(senderId);
                if(!conversatons.contains(messageRequest.getReceiverId())){
                    // Teachers cant initiate the conversation
                    logger.warn("Teacher attempted to initiate a conversation");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
            
            if(text == null){
                text = "";
            }

            messageService.create(text, senderId, receiverId, respondsToId, file, fileName);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<IMessageModel>> getConversation(@PathVariable Long senderId, @PathVariable Long receiverId, @RequestParam(name = "dateTimeFrom", required = false) Timestamp dateTimeFrom) {

        IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        if(!loggedInUser.getId().equals(senderId) && !loggedInUser.getId().equals(receiverId)) {
            // Can see your own conversations
            logger.warn("User attempted to see conversation he isnt part of");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<IMessageModel> conversation = messageService.getConversation(senderId, receiverId, dateTimeFrom);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/{id}/attachments")
    public ResponseEntity<List<byte[]>> getFiles(@PathVariable Long id) {

        IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IMessageModel message = messageService.getById(id);
    
        if(!loggedInUser.getId().equals(message.getReceiver()) && !loggedInUser.getId().equals(message.getSender())) {
            // Can only get files you can see
            logger.warn("User attempted to download a file they dont have permission to see");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<byte[]> files = messageService.downloadFiles(id);
        return ResponseEntity.ok(files);
    }
}
