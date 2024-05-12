package lu.oop.server.app.models.files;

import java.sql.Blob;

import lu.oop.server.app.models.messages.MessageModel;
import lu.oop.server.app.models.users.UserModel;

public interface IFileModel {
  Blob getFile();

  void setFile(Blob data);
  void setCreator(UserModel user);
  void attachToMessage(MessageModel message);
}
