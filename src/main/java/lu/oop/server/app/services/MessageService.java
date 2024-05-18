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
import java.util.Date;
import java.util.LinkedList;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

    public void create(String text, Long senderId, Long receiverId, Long respondsToId, byte[] file, String fileName){

        Optional<UserModel> mbySender = userRepository.findById(senderId);
        Optional<UserModel> mbyReceiver = userRepository.findById(receiverId);
        Optional<MessageModel> mbyRespondsTo = messageRepository.findById(respondsToId);

        if (mbySender.isEmpty() || mbyReceiver.isEmpty()) {
            throw new IllegalArgumentException("Sender or receiver not found");
        }
        
        UserModel sender = mbySender.get();
        UserModel receiver = mbyReceiver.get();

        if (file != null) {
            FileModel fileModel = new FileModel();
            if(fileName == null){
                fileName = "file-" + new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            }
            fileModel.setCreator(sender);
            fileModel.setFile(file);
            fileModel.setCreator(sender);
            fileModel.setName(fileName);
            fileRepository.save(fileModel);
    
            FileMessageModel fileMessage = new FileMessageModel();
            fileMessage.setText(text);
            fileMessage.setSender(sender);
            fileMessage.setReceiver(receiver);
            if(mbyRespondsTo.isPresent()){
                fileMessage.setRespondsTo(mbyRespondsTo.get());
            }
            fileMessage.attachFile(fileModel);
    
            messageRepository.save(fileMessage);
        } else {
            MessageModel message = new MessageModel();
            message.setText(text);
            message.setSender(sender);
            message.setReceiver(receiver);
            if(mbyRespondsTo.isPresent()){
                message.setRespondsTo(mbyRespondsTo.get());
            }
            messageRepository.save(message);
        }
    }

    public List<IMessageModel> getConversation(Long firstUserId, Long secondUserId, Timestamp dateTimeFrom){
        List<IMessageModel> conversationMessages = new LinkedList<>();
        
        conversationMessages = messageRepository.findConversationQuery(firstUserId, secondUserId, dateTimeFrom);
        
        return conversationMessages;
    }

    public List<byte[]> downloadFiles(Long id){
        Optional<MessageModel> mbyMessage = messageRepository.findById(id);

        if (mbyMessage.isEmpty()) {
            throw new IllegalArgumentException("No message found");
        }

        IMessageModel message = mbyMessage.get();

        if(message instanceof IFileMessageModel){
            IFileMessageModel fileMessage = (IFileMessageModel) message;
            List<FileModel> files = fileMessage.getFiles();
            List<byte[]> data = new LinkedList<>();
            for(FileModel file: files){
                data.add(file.getFile());
              }
          return data;
        }

        return new LinkedList<byte[]>();
    }
}
