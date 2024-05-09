package lu.oop.server.app.services;

import lu.oop.server.app.models.messages.IMessageModel;

import java.util.List;
import java.sql.Timestamp;

public interface IMessageService {
  public void create(String text, Long senderId, Long receiverId);
  public List<IMessageModel> getConversation(Long firstUserId, Long secondUserId, Timestamp dateTimeFrom);
}
  
