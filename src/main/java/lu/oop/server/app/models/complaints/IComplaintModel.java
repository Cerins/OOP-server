package lu.oop.server.app.models.complaints;

import lu.oop.server.app.models.users.UserModel;
import java.sql.Timestamp;

public interface IComplaintModel {
    void setTitle(String title);
    void setText(String text);
    void setComplaintant(UserModel complaintant);
    void setDefendant(UserModel defendant);
    void setClosedAt(Timestamp time);
}
