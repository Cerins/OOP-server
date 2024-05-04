package lu.oop.server.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import lu.oop.server.app.models.users.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

}
