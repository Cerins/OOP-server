package lu.oop.server.app.models.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "friendship")
public class FriendshipModel {

    public static final int STATUS_INIT = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_REJECTED = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    @JsonIgnore
    private UserModel requestor;

    @ManyToOne
    @JoinColumn(name = "responder_id", nullable = false)
    @JsonIgnore
    private UserModel responder;

    @Column(name = "status", nullable = false)
    @JsonIgnore
    private int status;

    // Constructors
    public FriendshipModel() {
    }

    public FriendshipModel(UserModel requestor, UserModel responder, int status) {
        this.requestor = requestor;
        this.responder = responder;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getRequestor() {
        return requestor;
    }

    public void setRequestor(UserModel requestor) {
        this.requestor = requestor;
    }

    public UserModel getResponder() {
        return responder;
    }

    public void setResponder(UserModel responder) {
        this.responder = responder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserModel getOther(UserModel self) {
        UserModel requester = getRequestor();
        if(requester.getId().equals(self.getId())) {
            return getResponder();
        } else {
            return requester;
        }
    }
}