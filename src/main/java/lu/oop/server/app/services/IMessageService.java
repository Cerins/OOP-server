package lu.oop.server.app.services;

import lu.oop.server.app.models.messages.MessageModel;

import java.util.List;
import java.util.Set;

public interface IMessageService {
  public void create(String text, Long senderId, Long receiverId);
  public List<MessageModel> getConversation(Long firstUserId, Long secondUserId);
  public Set<Integer> getConversations(Long userId);
}
  
