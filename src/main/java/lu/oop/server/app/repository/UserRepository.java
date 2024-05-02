package lu.oop.server.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import lu.oop.server.app.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

}
