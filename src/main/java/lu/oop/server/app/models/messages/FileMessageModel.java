package lu.oop.server.app.models.messages;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lu.oop.server.app.models.files.FileModel;

@Entity
@Table(name = "file_message")
public class FileMessageModel extends MessageModel implements IFileMessageModel {
    public FileMessageModel(){
      files = new LinkedList<>();
    }

    public List<FileModel> getFiles(){
      return files;
    }

    public void attachFile(FileModel file){
      files.add(file);
    };
}
