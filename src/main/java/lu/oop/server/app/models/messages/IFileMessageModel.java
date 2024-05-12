package lu.oop.server.app.models.messages;

import java.sql.Blob;
import java.util.List;

import lu.oop.server.app.models.files.FileModel;

public interface IFileMessageModel extends IMessageModel {
  public List<Blob> downloadFiles();
  public void attachFile(FileModel file);
}
