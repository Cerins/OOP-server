package lu.oop.server.app.services;

import java.util.List;
import java.util.Optional;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.users.UserModel;
import lu.oop.server.app.repositories.ComplaintRepository;
import lu.oop.server.app.repositories.UserRepository;

public class ComplaintService implements IComplaintService{
  private ComplaintRepository complaintRepository;
  private UserRepository userRepository;

  @Autowired
  public ComplaintService(UserRepository userRepository, ComplaintRepository complaintRepository) {
      this.complaintRepository = complaintRepository;
      this.userRepository = userRepository;
  }

  @Override
  public void createComplaint(String title, String text, Long complaintantId) {
    Optional<UserModel> mbyComplaintant = userRepository.findById(complaintantId);

    if(mbyComplaintant.isEmpty()){
        throw new IllegalArgumentException("Sender or receiver not found");
    }

    ComplaintModel complaint = new ComplaintModel();
    complaint.setTitle(title);
    complaint.setText(text);
    complaint.setComplaintant(mbyComplaintant.get());

    complaintRepository.save(complaint);
  }

  @Override
  public void assignComplaint(Long defendantId, Long complaintId) {
    Optional<UserModel> mbyDefendant = userRepository.findById(defendantId);
    Optional<ComplaintModel> mbyComplaint = complaintRepository.findById(complaintId);

    if(mbyDefendant.isEmpty() || mbyComplaint.isEmpty()){
        throw new IllegalArgumentException("Defendant or complaint not found");
    }

    ComplaintModel complaint = mbyComplaint.get();
    complaint.setDefendant(mbyDefendant.get());

    complaintRepository.save(complaint);
  }

  @Override
  public void closeComplaint(Long complaintId) {
    Optional<ComplaintModel> mbyComplaint = complaintRepository.findById(complaintId);

    if(mbyComplaint.isEmpty()){
        throw new IllegalArgumentException("Complaint not found");
    }

    ComplaintModel complaint = mbyComplaint.get();
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    complaint.setClosedAt(currentTime);
    
    complaintRepository.save(complaint);
  }

  @Override
  public List<ComplaintModel> getUnasignedComplaints() {
    List<ComplaintModel> unassigned = complaintRepository.getUnassignedComplaints();
    return unassigned;
  }
  
}
