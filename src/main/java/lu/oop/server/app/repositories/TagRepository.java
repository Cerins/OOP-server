package lu.oop.server.app.repositories;

import lu.oop.server.app.models.tags.ITagModel;
import lu.oop.server.app.models.tags.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagModel, Long> {
    Optional<ITagModel> findByNameAndType(String name, String type);
    @Query("SELECT tag from TagModel tag")
    List<ITagModel> all();
}
