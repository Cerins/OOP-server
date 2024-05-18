package lu.oop.server.app.models.messages;

import java.util.List;

import lu.oop.server.app.models.files.FileModel;

public interface IFileMessageModel extends IMessageModel {
  public List<FileModel> getFiles();
  public void attachFile(FileModel file);
}
