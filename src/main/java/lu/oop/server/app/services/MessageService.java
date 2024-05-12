package lu.oop.server.app.services;

import lu.oop.server.app.models.files.FileModel;
import lu.oop.server.app.models.messages.*;
import lu.oop.server.app.models.users.*;
import lu.oop.server.app.repositories.FileRepository;
import lu.oop.server.app.repositories.MessageRepository;
import lu.oop.server.app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import java.sql.Blob;
import java.sql.Timestamp;

@Service
public class MessageService implements IMessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, FileRepository fileRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    public void create(String text, Long senderId, Long receiverId, Blob file){

        Optional<UserModel> mbySender = userRepository.findById(senderId);
        Optional<UserModel> mbyReceiver = userRepository.findById(receiverId);

        if (mbySender.isEmpty() || mbyReceiver.isEmpty()) {
            throw new IllegalArgumentException("Sender or receiver not found");
        }

        MessageModel message = new MessageModel();

        message.setText(text);
        message.setSender(mbySender.get());
        message.setReceiver(mbyReceiver.get());

        if (file != null ){
            FileMessageModel fileMessage = (FileMessageModel) message;
            FileModel fileModel = new FileModel();
            fileModel.setCreator(mbySender.get());
            fileModel.setFile(file);
            fileRepository.save(fileModel);
            fileMessage.attachFile(fileModel);
            messageRepository.save(fileMessage);
        } 
        else{
            messageRepository.save(message);
        }
    }

    public List<IMessageModel> getConversation(Long firstUserId, Long secondUserId, Timestamp dateTimeFrom){
        List<IMessageModel> conversationMessages = new LinkedList<>();
        
        conversationMessages = messageRepository.findConversationQuery(firstUserId, secondUserId, dateTimeFrom);
        
        return conversationMessages;
    }

    public List<Blob> downloadFiles(Long id){
        Optional<MessageModel> mbyMessage = messageRepository.findById(id);

        if (mbyMessage.isEmpty()) {
            throw new IllegalArgumentException("No message found");
        }

        IMessageModel message = mbyMessage.get();

        if(message instanceof IFileMessageModel){
            IFileMessageModel fileMessage = (IFileMessageModel) message;
            return fileMessage.downloadFiles();
        }
        return new LinkedList<Blob>();
    }
}
