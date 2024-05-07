package lu.oop.server.app.models.messages;
import lu.oop.server.app.models.users.*;
import java.sql.Timestamp;

public interface IMessageModel {
    String getText();
    Timestamp getTime();

    void setText(String text);
    void setSender(UserModel sender);
    void setReceiver(UserModel receiver);
}