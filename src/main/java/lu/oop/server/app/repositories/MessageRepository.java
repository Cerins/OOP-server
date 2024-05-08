package lu.oop.server.app.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lu.oop.server.app.models.messages.MessageModel;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
  @Query(value = "SELECT m FROM MessageModel m WHERE (m.sender.id = ?1 AND m.receiver.id = ?2) OR (m.sender.id = ?2 AND m.receiver.id = ?1) ORDER BY m.time")
  List<MessageModel> findConversationQuery(Long firstUserId,
                                            Long secondUserId);

  @Query(value = "SELECT DISTINCT id FROM (SELECT m.receiver.id AS id FROM MessageModel m WHERE m.sender.id = ?1 UNION SELECT n.sender.id AS id FROM MessageModel n WHERE n.receiver.id = ?1) AS ids WHERE id != ?1")
  Set<Integer> getUserConversationQuery(Long userId);
}
