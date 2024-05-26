package lu.oop.server.app.models.users;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lu.oop.server.app.models.complaints.ComplaintModel;

@Entity
@DiscriminatorValue("3")
public class AdminModel extends UserModel implements IAdminModel {

    @OneToMany(mappedBy="defendant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ComplaintModel> assignedComplaints = new LinkedList<>();

    @Override
    public String getRoleName() {
        return "admin";
    }

    @Override
    public List<ComplaintModel> getAssignedComplaints() {
        return assignedComplaints;
    }
}
