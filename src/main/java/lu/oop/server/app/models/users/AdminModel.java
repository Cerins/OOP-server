package lu.oop.server.app.models.users;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lu.oop.server.app.models.complaints.ComplaintModel;

@Entity
@DiscriminatorValue("3")
public class AdminModel extends UserModel implements IAdminModel {

    @OneToMany(mappedBy="defendant")
    private List<ComplaintModel> assignedComplaints;

    @Override
    public String getRoleName() {
        return "admin";
    }

    @Override
    public List<ComplaintModel> getAssignedComplaints() {
        return assignedComplaints;
    }
}
