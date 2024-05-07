package lu.oop.server.app.models.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lu.oop.server.app.models.messages.MessageModel;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//{
//    firstName: string;
//    lastName: string;
//    age: number | null;
//    b: number | undefined; // Forbidden
//
//}

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.INTEGER)
abstract public class UserModel implements IUserModel {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "firstName", nullable = false, length = 50)
    @JsonProperty("firstName")
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    @JsonProperty("lastName")
    private String lastName;

    @Column(name = "phone", nullable = false, length = 50)
    @JsonIgnore
    private String phone;

    @Column(name = "email", nullable = false, length = 50)
    @JsonIgnore
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    @JsonIgnore
    private String password;

    @Column(name = "description", columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;

    @Column(name = "avatarId")
    @JsonProperty("avatarID")
    private Integer avatarId;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MessageModel> sentMessages = new HashSet<>();

    @OneToMany(mappedBy = "reciever", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MessageModel> recievedMessages = new HashSet<>();
    
    // Constructors
    public UserModel() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String plaintext) {
        this.password = encoder.encode(plaintext);
    }

    @JsonIgnore()
    public String getPasswordHash() {
        return this.password;
    }

    public boolean passwordMatches(String plaintext) {
        return encoder.matches(plaintext, this.password);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    @JsonProperty("role")
    abstract public String getRoleName();

    @JsonProperty("subject")
    public String getSubject() {
        return null;
    }
}
