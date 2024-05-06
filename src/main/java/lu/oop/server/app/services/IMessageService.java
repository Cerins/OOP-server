package lu.oop.server.app.services;

import lu.oop.server.app.models.messages.IMessageModel;

import java.util.Optional;

public interface IMessageService {
  public Optional<IMessageModel> getById(Long id);
  public void create(String text, Long senderId, Long recieverId);
}
  
