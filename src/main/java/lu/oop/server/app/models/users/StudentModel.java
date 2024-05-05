package lu.oop.server.app.models.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("0")
public class StudentModel extends UserModel implements  IStudentModel {
    @Override
    public String getRoleName() {
        return "student";
    }
}
