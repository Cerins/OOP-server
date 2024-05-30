package lu.oop.server.app.models.users;

import java.util.List;
import lu.oop.server.app.models.complaints.ComplaintModel;

public interface IAdminModel extends IUserModel {
  public List<ComplaintModel> getAssignedComplaints();
}
