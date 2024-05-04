package lu.oop.server.app.models.users;

public interface ITeacherModel extends IUserModel {
    String getSubject();

    void setSubject(String subject);
}
