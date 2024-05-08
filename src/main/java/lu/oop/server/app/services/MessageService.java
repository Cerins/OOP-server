package lu.oop.server.app.services;

import lu.oop.server.app.models.messages.*;
import lu.oop.server.app.models.users.*;
import lu.oop.server.app.repositories.MessageRepository;
import lu.oop.server.app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class MessageService implements IMessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public void create(String text, Long senderId, Long receiverId){

        Optional<UserModel> mbySender = userRepository.findById(senderId);
        Optional<UserModel> mbyReceiver = userRepository.findById(receiverId);

        if (mbySender.isEmpty() || mbyReceiver.isEmpty()) {
            throw new IllegalArgumentException("Sender or receiver not found");
        }

        MessageModel message = new MessageModel();
        message.setText(text);
        message.setSender(mbySender.get());
        message.setReceiver(mbyReceiver.get());
        messageRepository.save(message);
    }

    public List<MessageModel> getConversation(Long firstUserId, Long secondUserId){
        List<MessageModel> conversationMessages = messageRepository.findConversationQuery(firstUserId, secondUserId);
        return conversationMessages;
    }

    public Set<Integer> getConversations(Long userId){
        Set<Integer> conversations = messageRepository.getUserConversationQuery(userId);
        return conversations;
    }
}
