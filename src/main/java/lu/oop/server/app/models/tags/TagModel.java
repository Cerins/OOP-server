package lu.oop.server.app.models.tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lu.oop.server.app.models.users.UserModel;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag", uniqueConstraints={
        @UniqueConstraint(columnNames = {"type", "name"})
})
public class TagModel implements ITagModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    @JsonProperty("type")
    private String type;

    @Column(name = "name", nullable = false, length = 50)
    @JsonProperty("name")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<UserModel> users = new HashSet<>();

    public TagModel(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public TagModel() {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
