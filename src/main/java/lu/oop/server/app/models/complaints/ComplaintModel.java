package lu.oop.server.app.models.complaints;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lu.oop.server.app.models.users.UserModel;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "complaint")
public class ComplaintModel implements IComplaintModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    @JsonProperty("title")
    private String title;

    @Column(name = "text", nullable = false, length = 50)
    @JsonProperty("text")
    private String text;

    @Column(name = "createdAt", nullable =  false, columnDefinition = "TIMESTAMP")
    @JsonProperty("createdAt")
    private Timestamp createdAt;

    @Column(name = "closedAt", columnDefinition = "TIMESTAMP")
    @JsonProperty("closedAt")
    private Timestamp closedAt;

    @ManyToOne
    @JoinColumn(name = "complainantId")
    @JsonProperty("complainantId")
    private UserModel complaintant;

    @ManyToOne
    @JoinColumn(name = "defendantId")
    @JsonProperty("defendantId")
    private UserModel defendant;

    public ComplaintModel() {
      this.id = null;
      this.defendant = null;
      this.createdAt = new Timestamp(System.currentTimeMillis());
      this.closedAt = null;
    }

    @Override
    public void setTitle(String title) {
      this.title = title;
    }

    @Override
    public void setText(String text) {
      this.text = text;
    }

    @Override
    public void setComplaintant(UserModel complaintant) {
      this.complaintant = complaintant;
    }

    @Override
    public void setDefendant(UserModel defendant) {
      this.defendant = defendant;
    }

    @Override
    public void setClosedAt(Timestamp time) {
      this.closedAt = time;
    }
}
