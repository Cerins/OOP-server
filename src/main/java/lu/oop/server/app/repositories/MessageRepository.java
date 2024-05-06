package lu.oop.server.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import lu.oop.server.app.models.messages.MessageModel;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {

}
