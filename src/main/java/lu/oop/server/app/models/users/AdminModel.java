package lu.oop.server.app.models.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class AdminModel extends UserModel implements  IStudentModel {

    @Override
    public String getRoleName() {
        return "admin";
    }
}
