package lu.oop.server.app.models.tags;

public interface ITagModel {
    Long getId();

    void setId(Long id);

    void setType(String type);

    String getType();

    void setName(String name);

    String getName();
}
