package lu.oop.server.app.models.users;

import lu.oop.server.app.models.tags.ITagModel;

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

    Long getAvatarId();

    void setAvatarId(Long avatarId);

    String getRoleName();

    String getSubject();

    void setLogin(String login);

    String getLogin();

    ITagModel[] getTags();

    void addTag(ITagModel tag);

}