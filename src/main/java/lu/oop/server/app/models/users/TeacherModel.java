package lu.oop.server.app.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class TeacherModel extends UserModel implements ITeacherModel {
    @Column(name = "subject", length = 50)
    @JsonIgnore
    private String subject;

    @Override
    public String getRoleName() {
        return "teacher";
    }
    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
