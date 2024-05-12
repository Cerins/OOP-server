package lu.oop.server.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lu.oop.server.app.models.users.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query(value = "SELECT DISTINCT id FROM (SELECT m.receiver.id AS id FROM MessageModel m WHERE m.sender.id = ?1 UNION SELECT n.sender.id AS id FROM MessageModel n WHERE n.receiver.id = ?1) AS ids WHERE id != ?1")
  List<Integer> getUserConversationQuery(Long userId);
  
}
