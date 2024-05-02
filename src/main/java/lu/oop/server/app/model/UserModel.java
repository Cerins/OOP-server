package lu.oop.server.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "person")
public class UserModel {
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

//    @Column(name = "login", nullable = false, length = 50)
//    private String login;

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

    @Column(name = "role", nullable = false)
    @JsonIgnore
    private Integer role;

    @Column(name = "subject", length = 50)
    @JsonIgnore
    private String subject;

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

//    public String getLogin() {
//        return login;
//    }
//
//    public void setLogin(String login) {
//        this.login = login;
//    }

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

    public Integer getRole() {
        return role;
    }

    @JsonProperty("role")
    public String getRoleName() {
        if(role == 0) {
            return "user";
        }
        if(role == 1) {
            return "teacher";
        }
        if(role == 2) {
            return "parent";
        }
        if(role == 3) {
            return "admin";
        }
        return null;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
