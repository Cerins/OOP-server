package lu.oop.server.app.models.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class ParentModel extends UserModel implements  IParentModel{

    public boolean childGrounded() {
        return true;
    }

    @Override
    public String getRoleName() {
        return "parent";
    }
}
