package lu.oop.server.app.models.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.List;


import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.files.FileModel;
import lu.oop.server.app.models.messages.MessageModel;

import lu.oop.server.app.models.tags.ITagModel;
import lu.oop.server.app.models.tags.TagModel;
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

    @Column(name = "email", nullable = false, length = 50, unique = true)
    @JsonIgnore
    private String email;

    @Column(name = "login", nullable = false, length = 30, unique = true)
    @JsonProperty("login")
    private String login;

    @Column(name = "password", nullable = false, length = 255)
    @JsonIgnore
    private String password;

    @Column(name = "description", columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;

    @Column(name = "avatarId")
    @JsonProperty("avatarID")
    private Long avatarId;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MessageModel> sentMessages = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MessageModel> recievedMessages = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FileModel> files = new HashSet<>();

    @OneToMany(mappedBy="complaintant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ComplaintModel> complaints = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "user_tag",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagModel> tags = new HashSet<>();
    
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

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public List<ComplaintModel> getComplaints() {
        return complaints;
    }

    @JsonProperty("role")
    abstract public String getRoleName();

    @JsonProperty("tags")
    public TagModel[] getTags() {
//        System.out.println(this.tags);
        return  this.tags.toArray(new TagModel[0]);
    };

    public void addTag(ITagModel tag) {
        this.tags.add((TagModel) tag);
    }

    @JsonProperty("subject")
    public String getSubject() {
        return null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
