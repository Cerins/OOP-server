package lu.oop.server.app.models.messages;

import java.sql.Timestamp;
import java.util.List;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lu.oop.server.app.models.files.FileModel;
import lu.oop.server.app.models.users.*;

@Entity
@Table(name = "message")
@Inheritance(strategy = InheritanceType.JOINED)
public class MessageModel implements IMessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "text", nullable = false, length = 50)
    @JsonProperty("text")
    private String text;

    @Column(name = "time", columnDefinition = "TIMESTAMP")
    @JsonProperty("time")
    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "senderId")
    @JsonIgnore()
    private UserModel sender;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    @JsonIgnore()
    private UserModel receiver;

    @ManyToOne
    @JoinColumn(name = "respondsToId")
    @JsonIgnore()
    private MessageModel respondsTo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore()
    @JoinTable(
    name = "message_file", 
    joinColumns = @JoinColumn(name = "message_id"), 
    inverseJoinColumns = @JoinColumn(name = "file_id"))
    List<FileModel> files = new LinkedList<FileModel>();

    //Custom Json
    @JsonProperty("senderId")
    public Long getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    @JsonProperty("receiverId")
    public Long getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }

    @JsonProperty("respondsToId")
    public Long getRespondsToId() {
        return respondsTo != null ? respondsTo.getId() : null;
    }

    @JsonProperty("hasFiles")
    public Boolean hasFiles() {
        return !files.isEmpty();
    }



    // Constructors
    public MessageModel() {
        this.time = new Timestamp(System.currentTimeMillis());
        this.id = null;
        this.respondsTo = null;
    }

    // Getters and Setters
    public Long getId(){
        return id;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTime() {
        return time;
    }

    public Long getSender(){
        return sender.getId();
    }

    public Long getReceiver(){
        return receiver.getId();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public void setRespondsTo(MessageModel responds){
        this.respondsTo = responds;
    }

}
