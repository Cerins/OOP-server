package lu.oop.server.app.models.messages;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lu.oop.server.app.models.users.*;

@Entity
@Table(name = "message")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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
    private UserModel sender;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private UserModel receiver;
    


    // Constructors
    public MessageModel() {
        this.time = new Timestamp(System.currentTimeMillis());
        this.id = null;
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

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }
}
