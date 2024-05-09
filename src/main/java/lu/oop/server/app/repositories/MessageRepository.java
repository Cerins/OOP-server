package lu.oop.server.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lu.oop.server.app.models.messages.IMessageModel;
import lu.oop.server.app.models.messages.MessageModel;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
  @Query(value = "SELECT m FROM MessageModel m WHERE (m.sender.id = ?1 AND m.receiver.id = ?2) OR (m.sender.id = ?2 AND m.receiver.id = ?1) ORDER BY m.time")
  List<IMessageModel> findConversationQuery(Long firstUserId,
                                            Long secondUserId);
}
