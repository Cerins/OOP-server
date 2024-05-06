package lu.oop.server.app.services;

import lu.oop.server.app.models.messages.*;
import lu.oop.server.app.models.users.*;
import lu.oop.server.app.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService implements IMessageService {
    private MessageRepository messageRepository;
    private UserService userService;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }


    public Optional<IMessageModel> getById(Long id) {
        return messageRepository.findById(id).map(u -> u);
    }

    public void create(String text, Long senderId, Long recieverId){
        MessageModel message = new MessageModel();
        message.setText(text);
        Optional<IUserModel> sender = userService.getById(senderId);
        if(!sender.isEmpty()){
            message.setSender((UserModel) sender.get());
        }
        Optional<IUserModel> reciever = userService.getById(senderId);
        if(!reciever.isEmpty()){
            message.setReciever((UserModel) reciever.get());
        }
        
        messageRepository.save(message);
    }
}
