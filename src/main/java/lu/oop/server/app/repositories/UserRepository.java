package lu.oop.server.app.repositories;

import lu.oop.server.app.models.users.IUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import lu.oop.server.app.models.users.UserModel;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query(value = "SELECT usr FROM UserModel usr WHERE usr.email = ?1")
    Optional<IUserModel> findOneByEmail(String email);
}
