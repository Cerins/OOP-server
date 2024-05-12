package lu.oop.server.app.models.files;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

import org.aspectj.bridge.Message;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lu.oop.server.app.models.messages.MessageModel;
import lu.oop.server.app.models.users.*;

@Entity
@Table(name = "message")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @JsonProperty("name")
    private String text;

    @Column(name = "data", nullable =  false, columnDefinition = "BYTEA")
    private Blob data;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    @JsonProperty("createdBy")
    private UserModel createdBy;

    @ManyToMany
    @JoinTable(
    name = "message_file", 
    joinColumns = @JoinColumn(name = "fileId"), 
    inverseJoinColumns = @JoinColumn(name = "messageId"))
    List<MessageModel> messages;

    public FileModel() {
      this.id = null;
    }

    public void setFile(Blob data){
      this.data = data;
    }

    public void setCreator(UserModel user){
      this.createdBy = user;
    }

    public void attachToMessage(MessageModel message){
      this.messages.add(message);
    }

    public Blob getFile(){
      return this.data;
    }
}
