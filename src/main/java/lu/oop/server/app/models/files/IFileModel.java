package lu.oop.server.app.models.files;

import lu.oop.server.app.models.messages.MessageModel;
import lu.oop.server.app.models.users.UserModel;

public interface IFileModel {
  byte[] getFile();

  void setFile(byte[] data);
  void setCreator(UserModel user);
  void attachToMessage(MessageModel message);
}
