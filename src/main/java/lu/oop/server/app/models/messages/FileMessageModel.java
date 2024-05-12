package lu.oop.server.app.models.messages;

import java.sql.Blob;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lu.oop.server.app.models.files.FileModel;

public class FileMessageModel extends MessageModel implements IFileMessageModel {
    @ManyToMany
    @JoinTable(
    name = "message_file", 
    joinColumns = @JoinColumn(name = "messageId"), 
    inverseJoinColumns = @JoinColumn(name = "fileId"))
    List<FileModel> attachments;


    public List<Blob> downloadFiles(){
      List<FileModel> files = attachments;
      List<Blob> data = new LinkedList<>();
      for(FileModel file: files){
        data.add(file.getFile());
      }
      return data;
      
    }
    public void attachFiles(List<FileModel> files){
      for(FileModel file: files){
        attachments.add(file);
      }
    };
}
