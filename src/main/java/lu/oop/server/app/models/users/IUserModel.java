package lu.oop.server.app.models.users;

import java.util.Set;

import lu.oop.server.app.models.messages.MessageModel;

public interface IUserModel {
    Long getId();

    void setId(Long id);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getPhone();

    void setPhone(String phone);

    String getEmail();

    void setEmail(String email);

    void setPassword(String plaintext);

    String getPasswordHash();

    boolean passwordMatches(String plaintext);

    String getDescription();

    void setDescription(String description);

    Integer getAvatarId();

    void setAvatarId(Integer avatarId);

    String getRoleName();

    String getSubject();

    Set<MessageModel> fetchRecievedMessages();

}