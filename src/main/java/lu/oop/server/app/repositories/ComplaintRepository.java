package lu.oop.server.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lu.oop.server.app.models.complaints.ComplaintModel;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<ComplaintModel, Long> {
  @Query(value = "SELECT c FROM ComplaintModel c WHERE c.closedAt IS NULL")
    List<ComplaintModel> getUnassignedComplaints();
}
