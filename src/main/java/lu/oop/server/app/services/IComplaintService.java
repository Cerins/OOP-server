package lu.oop.server.app.services;

import java.util.List;

import lu.oop.server.app.models.complaints.ComplaintModel;

public interface IComplaintService {
  public void createComplaint(String title, String text, Long complaintantId);
  public void assignComplaint(Long defendantId, Long complaintId);
  public void closeComplaint(Long complaintId);
  public List<ComplaintModel> getUnasignedComplaints();
}
