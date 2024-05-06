package lu.oop.server.app.models.messages;
import lu.oop.server.app.models.users.*;
import java.sql.Time;

public interface IMessageModel {
    String getText();
    Time getTime();

    void setText(String text);
    void setSender(UserModel sender);
    void setReciever(UserModel reciever);
}