package lu.oop.server.app.repositories;

import lu.oop.server.app.models.users.ITeacherModel;
import lu.oop.server.app.models.users.IUserModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import lu.oop.server.app.models.users.UserModel;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query(value = "SELECT usr FROM UserModel usr WHERE usr.email = ?1")
    Optional<IUserModel> findOneByEmail(String email);
    @Query(value = "SELECT DISTINCT id FROM (SELECT m.receiver.id AS id FROM MessageModel m WHERE m.sender.id = ?1 UNION SELECT n.sender.id AS id FROM MessageModel n WHERE n.receiver.id = ?1) AS ids WHERE id != ?1")
    List<Integer> getUserConversationQuery(Long userId);
    @Query(value = "SELECT usr FROM UserModel usr WHERE usr.login = ?1")
    Optional<IUserModel> findOneByLogin(String login);
    @Query(value = "SELECT usr FROM UserModel usr WHERE usr.login LIKE %?1%")
    List<IUserModel> getListByLogin(String login);
    @Query(value  = "SELECT * FROM person WHERE role = 2", nativeQuery = true)
    List<ITeacherModel> getAllTeachers();
}
